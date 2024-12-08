package com.kshrd.kroya_api.payload.Feedback;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequest {

    private Long foodId;

    private String ratingValue; // Optional field for rating

    private String commentText; // Optional field for comment
}
