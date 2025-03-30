package com.kshrd.kroya_api.controller;

import com.kshrd.kroya_api.entity.CredentialEntity;
import com.kshrd.kroya_api.payload.Auth.UserProfileUpdateRequest;
import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.payload.User.UserRequest;
import com.kshrd.kroya_api.service.User.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "https://krorya-eosin.vercel.app"})
public class UserController {

    private final UserService userService;
    
    @Operation(
            summary = "🍽️ Retrieve Current User's Food Listings",
            description = """
                    Fetches a list of food listings created by the currently authenticated user.
                    
                    **Response Summary**:
                    - **200**: ✅ Food listings retrieved successfully.
                    - **401**: 🚫 Unauthorized access.
                    """
    )
    @GetMapping("/foods")
    public BaseResponse<?> getFoodsByCurrentUser() {
        return userService.getFoodsByCurrentUser();
    }

    @Operation(
            summary = "🍽️ Retrieve Food Listings by User ID",
            description = """
                    Fetches a list of food listings for a specific user based on the provided user ID.
                    
                    **Path Variable**: **userId** (Integer): ID of the user whose food listings are to be fetched.
                    
                    **Response Summary**:
                    - **200**: ✅ Food listings retrieved successfully.
                    - **404**: 🚫 User not found.
                    """
    )
    @GetMapping("/foods/{userId}")
    public BaseResponse<?> getFoodsByUserId(@PathVariable Integer userId) {
        return userService.getFoodsByUserId(userId);
    }

    @Operation(
            summary = "✏️ Update User Profile",
            description = """
                    Updates the profile details of the currently authenticated user.
                    
                    **Request Body**: JSON object containing updated profile information.
                    
                    **Response Summary**:
                    - **200**: ✅ Profile updated successfully.
                    - **400**: 🚫 Invalid input data.
                    """
    )
    @PutMapping("/edit-profile")
    public BaseResponse<?> updateProfile(@RequestBody UserProfileUpdateRequest profileUpdateRequest) {
        return userService.updateProfile(profileUpdateRequest);
    }

//    @Operation(
//            summary = "🗑️ Delete User Account",
//            description = """
//                    Deletes the account of the currently authenticated user.
//
//                    **Response Summary**:
//                    - **200**: ✅ Account deleted successfully.
//                    - **401**: 🚫 Unauthorized access.
//                    """
//    )
//    @DeleteMapping("/delete-account")
//    public BaseResponse<?> deleteAccount() {
//        return userService.deleteAccount();
//    }

//    @Operation(
//            summary = "🔗 Connect to Webill Service",
//            description = """
//                    Establishes a connection with the Webill service using provided credentials.
//
//                    **Request Body**: JSON object containing Webill credentials.
//
//                    **Response Summary**:
//                    - **200**: ✅ Successfully connected to Webill.
//                    - **400**: 🚫 Invalid credentials.
//                    """
//    )
//    @PostMapping("/connectWebill")
//    public BaseResponse<?> connectWebill(@RequestBody CredentialEntity credentialEntity) {
//        return userService.connectWebill(credentialEntity);
//    }

//    @Operation(
//            summary = "🔌 Disconnect Webill Integration",
//            description = """
//                    Disconnects the user's integration with Webill.
//
//                    **Response Summary**:
//                    - **200**: ✅ Disconnected successfully.
//                    - **401**: 🚫 Unauthorized access.
//                    """
//    )
//    @DeleteMapping("/disconnectWebill")
//    public BaseResponse<?> disConnectWebill() {
//        return userService.disconnectWebill();
//    }

//    @Operation(
//            summary = "🔑 Retrieve User Credentials by User ID",
//            description = """
//                    Fetches Webill credentials for a specific user.
//
//                    **Path Variable**: **userId** (Integer): ID of the user whose credentials are to be fetched.
//
//                    **Response Summary**:
//                    - **200**: ✅ Credentials retrieved successfully.
//                    - **404**: 🚫 User or credentials not found.
//                    """
//    )
//    @GetMapping("/webill-acc-no/{userId}")
//    public BaseResponse<?> getWebillAccNoByUserId(@PathVariable("userId") Integer userId) {
//        return userService.getWebillAccNoByUserId(userId);
//    }

    @Operation(
            summary = "📱 Retrieve Device Token by User ID",
            description = """
                    Fetches the device token associated with a specific user.
                    
                    **Path Variable**: **userId** (Integer): ID of the user whose device token is to be fetched.
                    
                    **Response Summary**:
                    - **200**: ✅ Device token retrieved successfully.
                    - **404**: 🚫 Device token or user not found.
                    """
    )
    @GetMapping("/device-token/{userId}")
    public BaseResponse<?> getDeviceTokenByUserId(@PathVariable("userId") Integer userId) {
        return userService.getDeviceTokenByUserId(userId);
    }

    @Operation(
            summary = "📲 Register or Update Device Token",
            description = """
                    Registers or updates the device token for the currently authenticated user.
                    
                    **Request Parameter**: **deviceToken** (String): The device token to register or update.
                    
                    **Response Summary**:
                    - **200**: ✅ Device token registered/updated successfully.
                    - **400**: 🚫 Invalid device token format.
                    """
    )
    @PostMapping("/device-token")
    public BaseResponse<?> insertDeviceToken(@RequestParam String deviceToken) {
        return userService.insertDeviceToken(deviceToken);
    }

    @Operation(
            summary = "👤 Fetch User Profile Information",
            description = """
                    Retrieves the profile information of the currently authenticated user.
                    
                    **Response Summary**:
                    - **200**: ✅ User profile information retrieved successfully.
                    - **401**: 🚫 Unauthorized access.
                    """
    )
    @GetMapping("/profile")
    public BaseResponse<?> getUserInfo() {
        return userService.getUserInfo();
    }

    @Operation(
        summary = "👥 Retrieve All Users with Pagination",
        description = """
                Fetches a paginated list of all users.

                **Request Parameters**:
                - **page** (Integer): Page number (default is 0).
                - **size** (Integer): Number of users per page (default is 10).

                **Response Summary**:
                - **200**: ✅ Users retrieved successfully.
                - **401**: 🚫 Unauthorized access.
                """
)
@PreAuthorize("hasRole('ROLE_ADMIN')")
@GetMapping("/all")
public BaseResponse<?> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
    return userService.getAllUsers(page, size);
}

    @Operation(
        summary = "🗑️ Delete User by ID",
        description = """
                Deletes a user by their ID.

                **Path Variable**: **userId** (Integer): ID of the user to be deleted.

                **Response Summary**:
                - **200**: ✅ User deleted successfully.
                - **404**: 🚫 User not found.
                """
)
@PutMapping("/deleteUserById/{userId}")
public BaseResponse<?> updateUserById(@PathVariable Integer userId, @RequestBody UserRequest userRequest) {
    return userService.updateUserById(userId, userRequest);
}

    @Operation(
            summary = "🚪 Logout User",
            description = """
            Logs out the currently authenticated user by invalidating their session or token.

            **Response Summary**:
            - **200**: ✅ User logged out successfully.
            - **401**: 🚫 Unauthorized access.
            """
    )
    @PostMapping("/logout")
    public BaseResponse<?> logout(HttpServletRequest request) {
        return userService.logout(request);
    }
}

