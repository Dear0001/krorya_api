package com.kshrd.kroya_api.service.Cuisine;

import com.kshrd.kroya_api.entity.CuisineEntity;
import com.kshrd.kroya_api.exception.DuplicateFieldExceptionHandler;
import com.kshrd.kroya_api.exception.InvalidValueExceptionHandler;
import com.kshrd.kroya_api.exception.NotFoundExceptionHandler;
import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.payload.Category.PaginationMeta;
import com.kshrd.kroya_api.payload.Cuisine.CuisineRequest;
import com.kshrd.kroya_api.repository.Cuisine.CuisineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CuisineServiceImpl implements CuisineService {

    private final CuisineRepository cuisineRepository;
    private final ModelMapper modelMapper;

    @Override
    public BaseResponse<?> postCuisine(CuisineRequest cuisineRequest) {

        String cuisineName = cuisineRequest.getCuisineName();
        log.info("Received request to create a cuisine with name: {}", cuisineName);

        // Validate cuisine name to ensure it does not contain numbers, special characters, or blank spaces
        if (!cuisineName.matches("^[a-zA-Z\\s]+$")) {
            log.warn("Invalid cuisine name: '{}'. It must contain only letters and spaces.", cuisineName);
            throw new InvalidValueExceptionHandler("Cuisine name can only contain letters and spaces, without numbers or special characters.");
        }

        // Check if a cuisine with the same name already exists
        Optional<CuisineEntity> existingCuisine = cuisineRepository.findByCuisineName(cuisineName);
        if (existingCuisine.isPresent()) {
            log.warn("Cuisine with name '{}' already exists", cuisineName);
            throw new DuplicateFieldExceptionHandler("Cuisine name: '" + cuisineName + "' already exists.");
        }

        // Save the new cuisine to the database
        CuisineEntity cuisineEntity = modelMapper.map(cuisineRequest, CuisineEntity.class);
        cuisineRepository.save(cuisineEntity);
        log.info("Cuisine saved successfully with ID: {}", cuisineEntity.getId());

        // Build and return a successful response
        return BaseResponse.builder()
                .statusCode(String.valueOf(HttpStatus.OK.value()))
                .payload(cuisineEntity)
                .message("Cuisine has been created successfully")
                .build();
    }

    @Override
    public BaseResponse<?> getAllCuisine(Integer page, Integer size) {
        log.info("Received request to fetch all cuisines with page: {} and size: {}", page, size);

        // Create a Pageable object for pagination
        Pageable pageable = PageRequest.of(page, size);

        // Fetch cuisines from the database with pagination
        Page<CuisineEntity> cuisinePage = cuisineRepository.findAll(pageable);

        // Check if any cuisines are found
        if (cuisinePage.isEmpty()) {
            log.warn("No cuisines found in the system");
            throw new NotFoundExceptionHandler("No cuisines found in the system.");
        }

        // Prepare pagination details
        long totalCuisines = cuisinePage.getTotalElements();
        int totalPages = cuisinePage.getTotalPages();
        int currentPage = cuisinePage.getNumber();

        // Construct the "next" and "previous" links
        String nextLink = (currentPage + 1 < totalPages) ?
                String.format("/api/v1/cuisine/all?page=%d&size=%d", currentPage + 1, size) : null;
        String prevLink = (currentPage > 0) ?
                String.format("/api/v1/cuisine/all?page=%d&size=%d", currentPage - 1, size) : null;

        // Create the PaginationMeta object
        PaginationMeta paginationMeta = new PaginationMeta(totalCuisines, totalPages, currentPage, size, nextLink, prevLink);

        // Build and return the response with pagination metadata
        return BaseResponse.builder()
                .statusCode(String.valueOf(HttpStatus.OK.value()))
                .payload(cuisinePage.getContent())  // List of cuisines in the current page
                .paginationMeta(paginationMeta)  // Set pagination details
                .message("Cuisines retrieved successfully")
                .build();
    }


}
