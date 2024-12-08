package com.kshrd.kroya_api.util;

import com.kshrd.kroya_api.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


public class AuthHelper {
    private static Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static UserEntity getUser() {
        return (UserEntity) getAuth().getPrincipal();
    }

    public static String getUsername() {
        return getUser().getUsername();
    }

    //    public static String getFullName(){
//        return getUser().getFirstname() + " " + getUser().getLastname();
//    }
    public static void reload() {
        SecurityContext context = SecurityContextHolder.getContext();
        context.getAuthentication().setAuthenticated(false);
    }
}
