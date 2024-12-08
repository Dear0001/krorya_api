package com.kshrd.kroya_api.payload.Feedback;

import com.kshrd.kroya_api.dto.UserDTO;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FeedbackResponse {
    private Long feedbackId;
    private UserDTO user;
    private Integer ratingValue;
    private String commentText;
    private LocalDateTime createdAt;
}
