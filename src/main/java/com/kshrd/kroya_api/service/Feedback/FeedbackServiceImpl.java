package com.kshrd.kroya_api.service.Feedback;

import com.kshrd.kroya_api.dto.UserDTO;
import com.kshrd.kroya_api.entity.FeedbackEntity;
import com.kshrd.kroya_api.entity.FoodRecipeEntity;
import com.kshrd.kroya_api.entity.FoodSellEntity;
import com.kshrd.kroya_api.entity.UserEntity;
import com.kshrd.kroya_api.enums.ItemType;
import com.kshrd.kroya_api.exception.DuplicateFieldExceptionHandler;
import com.kshrd.kroya_api.exception.ForbiddenException;
import com.kshrd.kroya_api.exception.InvalidValueExceptionHandler;
import com.kshrd.kroya_api.exception.NotFoundExceptionHandler;
import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.payload.Feedback.FeedbackRequest;
import com.kshrd.kroya_api.payload.Feedback.FeedbackResponse;
import com.kshrd.kroya_api.repository.Feedback.FeedbackRepository;
import com.kshrd.kroya_api.repository.FoodRecipe.FoodRecipeRepository;
import com.kshrd.kroya_api.repository.FoodSell.FoodSellRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FoodRecipeRepository foodRecipeRepository;
    private final FoodSellRepository foodSellRepository;
    private final ModelMapper modelMapper;

    public BaseResponse<FeedbackResponse> addFeedback(FeedbackRequest feedbackRequest, ItemType itemType) {
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasRating = feedbackRequest.getRatingValue() != null;
        boolean hasComment = feedbackRequest.getCommentText() != null;

        // Ensure at least one field is provided
        if (!hasRating && !hasComment) {
            throw new InvalidValueExceptionHandler("Either ratingValue or commentText must be provided.");
        }

        FeedbackEntity feedbackEntity;
        Optional<FeedbackEntity> existingFeedback;

        // Determine item type and fetch existing feedback if it exists
        if (itemType == ItemType.FOOD_RECIPE) {
            FoodRecipeEntity recipe = foodRecipeRepository.findById(Math.toIntExact(feedbackRequest.getFoodId()))
                    .orElseThrow(() -> new NotFoundExceptionHandler("Recipe not found with ID: " + feedbackRequest.getFoodId()));

            existingFeedback = feedbackRepository.findByUserAndFoodRecipe(currentUser, recipe);
            feedbackEntity = existingFeedback.orElseGet(() -> FeedbackEntity.builder()
                    .user(currentUser)
                    .foodRecipe(recipe)
                    .createdAt(LocalDateTime.now())
                    .build());

            // Check if the recipe is associated with a FoodSell
            Optional<FoodSellEntity> linkedSell = foodSellRepository.findByFoodRecipe(recipe);
            if (linkedSell.isPresent()) {
                throw new InvalidValueExceptionHandler("This recipe is part of a Food Sell. Please select the Food Sell item instead.");
            }

            // Prevent feedback on own item
            if (recipe.getUser().getId().equals(currentUser.getId())) {
                throw new ForbiddenException("You cannot provide feedback on your own recipe");
            }

        } else if (itemType == ItemType.FOOD_SELL) {
            FoodSellEntity sell = foodSellRepository.findById(feedbackRequest.getFoodId())
                    .orElseThrow(() -> new NotFoundExceptionHandler("Food Sell not found with ID: " + feedbackRequest.getFoodId()));

            existingFeedback = feedbackRepository.findByUserAndFoodSell(currentUser, sell);
            feedbackEntity = existingFeedback.orElseGet(() -> FeedbackEntity.builder()
                    .user(currentUser)
                    .foodSell(sell)
                    .createdAt(LocalDateTime.now())
                    .build());

            // Prevent feedback on own item
            if (sell.getFoodRecipe().getUser().getId().equals(currentUser.getId())) {
                throw new ForbiddenException("You cannot provide feedback on your own sell item");
            }

        } else {
            throw new InvalidValueExceptionHandler("Invalid item type");
        }

        // Check if feedback already exists with both fields set
        if (existingFeedback.isPresent()) {
            boolean hasExistingRating = feedbackEntity.getRatingValue() != null;
            boolean hasExistingComment = feedbackEntity.getCommentText() != null;

            // Both rating and comment are already set; disallow further updates
            if (hasExistingRating && hasExistingComment) {
                throw new DuplicateFieldExceptionHandler("User has already provided both a rating and a comment for this item and cannot modify them further.");
            }

            // Prevent duplicate rating or comment based on existing fields
            if (hasRating && hasExistingRating) {
                throw new DuplicateFieldExceptionHandler("User has already rated this item. Please add a comment only.");
            }
            if (hasComment && hasExistingComment) {
                throw new DuplicateFieldExceptionHandler("User has already commented on this item. Please add a rating only.");
            }
        }

        // Validate rating value if provided
        if (hasRating) {
            int ratingValue = Integer.parseInt(feedbackRequest.getRatingValue());
            if (ratingValue < 1 || ratingValue > 5) {
                throw new InvalidValueExceptionHandler("Rating must be a whole number between 1 and 5.");
            }
            feedbackEntity.setRatingValue(ratingValue);
            // Update totalRaters and averageRating for FoodRecipe
            FoodRecipeEntity associatedRecipe = feedbackEntity.getFoodRecipe() != null ? feedbackEntity.getFoodRecipe() : feedbackEntity.getFoodSell().getFoodRecipe();
            updateRecipeRating(associatedRecipe, ratingValue);
            foodRecipeRepository.save(associatedRecipe);
        }

        // Set comment if provided
        if (hasComment) {
            feedbackEntity.setCommentText(feedbackRequest.getCommentText());
        }

        // Persist feedback entity (create or update)
        feedbackEntity = feedbackRepository.save(feedbackEntity);

        // Prepare response
        FeedbackResponse feedbackResponse = modelMapper.map(feedbackEntity, FeedbackResponse.class);
        feedbackResponse.setFeedbackId(feedbackEntity.getId());
        feedbackResponse.setUser(modelMapper.map(currentUser, UserDTO.class));

        return BaseResponse.<FeedbackResponse>builder()
                .statusCode(String.valueOf(HttpStatus.CREATED.value()))
                .payload(feedbackResponse)
                .message("Feedback added successfully")
                .build();
    }

    /**
     * Updates the average rating and total raters for a recipe when a new rating is added.
     */
    private void updateRecipeRating(FoodRecipeEntity recipe, int newRating) {
        int updatedTotalRaters = Optional.ofNullable(recipe.getTotalRaters()).orElse(0) + 1;
        double currentAverageRating = Optional.ofNullable(recipe.getAverageRating()).orElse(0.0);
        double updatedAverageRating = (currentAverageRating * (updatedTotalRaters - 1) + newRating) / updatedTotalRaters;
        recipe.setTotalRaters(updatedTotalRaters);
        recipe.setAverageRating(updatedAverageRating);
    }

    /**
     * Updates an existing feedback entry with a new rating or comment.
     */
    public BaseResponse<FeedbackResponse> updateFeedback(Long feedbackId, FeedbackRequest feedbackRequest) {
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FeedbackEntity feedbackEntity = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new NotFoundExceptionHandler("Feedback not found with ID: " + feedbackId));

        // Verify that the feedback belongs to the current user
        if (!feedbackEntity.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You cannot update feedback that you did not create.");
        }

        boolean hasRating = feedbackRequest.getRatingValue() != null;
        boolean hasComment = feedbackRequest.getCommentText() != null;

        // Ensure at least one field is provided for update
        if (!hasRating && !hasComment) {
            throw new InvalidValueExceptionHandler("Either ratingValue or commentText must be provided for update.");
        }

        // Update rating if provided
        if (hasRating) {
            int newRatingValue = Integer.parseInt(feedbackRequest.getRatingValue());
            if (newRatingValue < 1 || newRatingValue > 5) {
                throw new InvalidValueExceptionHandler("Rating must be a whole number between 1 and 5.");
            }

            // Adjust recipe rating with the updated rating value
            FoodRecipeEntity associatedRecipe = feedbackEntity.getFoodRecipe() != null ? feedbackEntity.getFoodRecipe() : feedbackEntity.getFoodSell().getFoodRecipe();
            updateRecipeRatingOnUpdate(associatedRecipe, feedbackEntity.getRatingValue(), newRatingValue);

            // Update feedback entity with new rating
            feedbackEntity.setRatingValue(newRatingValue);
            foodRecipeRepository.save(associatedRecipe);
        }

        // Update comment if provided
        if (hasComment) {
            feedbackEntity.setCommentText(feedbackRequest.getCommentText());
        }

        // Save the updated feedback entity
        feedbackEntity = feedbackRepository.save(feedbackEntity);

        // Prepare response
        FeedbackResponse feedbackResponse = modelMapper.map(feedbackEntity, FeedbackResponse.class);
        feedbackResponse.setFeedbackId(feedbackEntity.getId());
        feedbackResponse.setUser(modelMapper.map(currentUser, UserDTO.class));

        return BaseResponse.<FeedbackResponse>builder()
                .statusCode(String.valueOf(HttpStatus.OK.value()))
                .payload(feedbackResponse)
                .message("Feedback updated successfully")
                .build();
    }

    /**
     * Deletes an existing feedback entry.
     */
    public BaseResponse<String> deleteFeedback(Long feedbackId) {
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FeedbackEntity feedbackEntity = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new NotFoundExceptionHandler("Feedback not found with ID: " + feedbackId));

        // Verify that the feedback belongs to the current user
        if (!feedbackEntity.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You cannot delete feedback that you did not create.");
        }

        // Update the recipe rating and total raters if feedback includes a rating
        if (feedbackEntity.getRatingValue() != null) {
            FoodRecipeEntity associatedRecipe = feedbackEntity.getFoodRecipe() != null ? feedbackEntity.getFoodRecipe() : feedbackEntity.getFoodSell().getFoodRecipe();
            updateRecipeRatingOnDelete(associatedRecipe, feedbackEntity.getRatingValue());
            foodRecipeRepository.save(associatedRecipe);
        }

        // Delete the feedback entity
        feedbackRepository.delete(feedbackEntity);

        return BaseResponse.<String>builder()
                .statusCode(String.valueOf(HttpStatus.OK.value()))
                .message("Feedback deleted successfully")
                .payload("Feedback with ID " + feedbackId + " has been deleted")
                .build();
    }

    @Override
    public BaseResponse<?> getAllFeedbacksByFoodId(Long foodId, ItemType itemType) {
        log.info("Fetching all feedbacks for foodId: {} and itemType: {}", foodId, itemType);

        List<FeedbackEntity> feedbackEntities;

        if (itemType == ItemType.FOOD_RECIPE) {
            // Fetch all feedbacks for a specific FoodRecipe by ID
            FoodRecipeEntity recipe = foodRecipeRepository.findById(Math.toIntExact(foodId))
                    .orElseThrow(() -> new NotFoundExceptionHandler("Recipe not found with ID: " + foodId));

            feedbackEntities = feedbackRepository.findByFoodRecipe(recipe);

        } else if (itemType == ItemType.FOOD_SELL) {
            // Fetch all feedbacks for a specific FoodSell by ID
            FoodSellEntity sell = foodSellRepository.findById(foodId)
                    .orElseThrow(() -> new NotFoundExceptionHandler("Food Sell not found with ID: " + foodId));

            feedbackEntities = feedbackRepository.findByFoodSell(sell);

        } else {
            throw new InvalidValueExceptionHandler("Invalid item type");
        }

        // Check if there are no feedbacks for the provided foodId and itemType
        if (feedbackEntities.isEmpty()) {
            throw new NotFoundExceptionHandler("No feedbacks found for the specified food item.");
        }

        // Map feedback entities to FeedbackResponse
        List<FeedbackResponse> feedbackResponses = feedbackEntities.stream()
                .map(feedback -> {
                    FeedbackResponse response = modelMapper.map(feedback, FeedbackResponse.class);
                    response.setFeedbackId(feedback.getId());
                    UserDTO userDTO = modelMapper.map(feedback.getUser(), UserDTO.class);
                    response.setUser(userDTO);
                    return response;
                })
                .collect(Collectors.toList());

        return BaseResponse.builder()
                .statusCode(String.valueOf(HttpStatus.OK.value()))
                .payload(feedbackResponses)
                .message("Feedbacks fetched successfully")
                .build();
    }

    @Override
    public BaseResponse<FeedbackResponse> getFeedback(Long foodId, ItemType itemType) {
        // Get the currently authenticated user
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Fetching feedback for user: {} on item ID: {} of type: {}", currentUser.getEmail(), foodId, itemType);

        // Determine feedback based on item type
        Optional<FeedbackEntity> feedbackEntityOptional;
        if (itemType == ItemType.FOOD_RECIPE) {
            // Fetch FoodRecipe by ID
            FoodRecipeEntity recipe = foodRecipeRepository.findById(foodId.intValue())
                    .orElseThrow(() -> new NotFoundExceptionHandler("Recipe not found with ID: " + foodId));

            // Get feedback for the specified FoodRecipe and user
            feedbackEntityOptional = feedbackRepository.findByUserAndFoodRecipe(currentUser, recipe);

        } else if (itemType == ItemType.FOOD_SELL) {
            // Fetch FoodSell by ID
            FoodSellEntity sell = foodSellRepository.findById(foodId)
                    .orElseThrow(() -> new NotFoundExceptionHandler("Food Sell not found with ID: " + foodId));

            // Get feedback for the specified FoodSell and user
            feedbackEntityOptional = feedbackRepository.findByUserAndFoodSell(currentUser, sell);

        } else {
            throw new InvalidValueExceptionHandler("Invalid item type");
        }

        // If feedback is not present, throw an exception
        FeedbackEntity feedbackEntity = feedbackEntityOptional
                .orElseThrow(() -> new NotFoundExceptionHandler("No feedback found for the specified food item by the current user."));

        // Map feedback entity to FeedbackResponse
        FeedbackResponse feedbackResponse = modelMapper.map(feedbackEntity, FeedbackResponse.class);
        feedbackResponse.setFeedbackId(feedbackEntity.getId());

        // Map user details
        feedbackResponse.setUser(modelMapper.map(currentUser, UserDTO.class));

        // Return response
        return BaseResponse.<FeedbackResponse>builder()
                .statusCode(String.valueOf(HttpStatus.OK.value()))
                .payload(feedbackResponse)
                .message("Feedback fetched successfully")
                .build();
    }



    /**
     * Updates the recipe's average rating and total raters when an existing rating is updated.
     */
    private void updateRecipeRatingOnUpdate(FoodRecipeEntity recipe, int oldRating, int newRating) {
        int totalRaters = Optional.ofNullable(recipe.getTotalRaters()).orElse(0);
        if (totalRaters > 0) {
            double currentAverageRating = Optional.ofNullable(recipe.getAverageRating()).orElse(0.0);
            double updatedAverageRating = (currentAverageRating * totalRaters - oldRating + newRating) / totalRaters;
            recipe.setAverageRating(updatedAverageRating);
        }
    }

    /**
     * Updates the recipe's average rating and total raters when a rating is deleted.
     */
    private void updateRecipeRatingOnDelete(FoodRecipeEntity recipe, int deletedRating) {
        int totalRaters = Optional.ofNullable(recipe.getTotalRaters()).orElse(0);
        if (totalRaters > 1) {
            double currentAverageRating = Optional.ofNullable(recipe.getAverageRating()).orElse(0.0);
            double updatedAverageRating = (currentAverageRating * totalRaters - deletedRating) / (totalRaters - 1);
            recipe.setAverageRating(updatedAverageRating);
            recipe.setTotalRaters(totalRaters - 1);
        } else {
            // If there was only one rating, reset the recipe's rating data
            recipe.setAverageRating(0.0);
            recipe.setTotalRaters(0);
        }
    }
}