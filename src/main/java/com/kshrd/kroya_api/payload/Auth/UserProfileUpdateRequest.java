package com.kshrd.kroya_api.payload.Auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileUpdateRequest {
    private String profileImage;
    private String fullName;
//    private String password;
    private String phoneNumber;
}