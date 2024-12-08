package com.kshrd.kroya_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDTO {
    private Long userId;
    private String fullName;
    private String phoneNumber;
    @Builder.Default
    private String profileImage = "default.jpg";
}
