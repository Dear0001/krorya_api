package com.kshrd.kroya_api.service.Feedback;

import com.kshrd.kroya_api.enums.ItemType;
import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.payload.Feedback.FeedbackRequest;
import com.kshrd.kroya_api.payload.Feedback.FeedbackResponse;
import jakarta.validation.Valid;

public interface FeedbackService {

    BaseResponse<FeedbackResponse> addFeedback(@Valid FeedbackRequest feedbackRequest, ItemType itemType);

    BaseResponse<FeedbackResponse> updateFeedback(Long feedbackId, @Valid FeedbackRequest feedbackRequest);

    BaseResponse<String> deleteFeedback(Long feedbackId);

    BaseResponse<?> getAllFeedbacksByFoodId(Long foodId, ItemType itemType);

    BaseResponse<FeedbackResponse> getFeedback(Long foodId, ItemType itemType);
}
