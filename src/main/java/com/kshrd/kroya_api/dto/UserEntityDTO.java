package com.kshrd.kroya_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntityDTO {
    private Integer id;
    private String fullName;
    private String email;
    @Builder.Default
    private String profileImage = "default.jpg";
    private String phoneNumber;
    private String password;
    private String role;
    private boolean isEmailVerified;
    private String emailVerifiedAt;
    private String createdAt;
}
