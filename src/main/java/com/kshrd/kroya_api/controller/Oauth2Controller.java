package com.kshrd.kroya_api.controller;

import com.kshrd.kroya_api.payload.Auth.OAuth2Request;
import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.service.Auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/oauth2")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "https://krorya-eosin.vercel.app"})
public class Oauth2Controller {

    private final AuthenticationService authenticationService;


    @PostMapping("/google")
    public BaseResponse<?> googleLogin(@RequestBody OAuth2Request oauthRequest) {
        return authenticationService.handleOAuth2Login(oauthRequest, "Google");
    }

    @PostMapping("/facebook")
    public BaseResponse<?> facebookLogin(@RequestBody OAuth2Request oauthRequest) {
        return authenticationService.handleOAuth2Login(oauthRequest, "Facebook");
    }
}