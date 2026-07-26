package com.asian.auto.hub.security.config;

public class AppConstants {


    public static final String[] AUTH_PUBLIC_URLS = {
            "/api/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**"
    };

    public static final String[] AUTH_ADMIN_URLS= {
            "/api/user/**"
    };

    public static final String[] AUTH_GUEST_URLS= {

    };

    public static final String ADMIN_ROLE = "ADMIN";
    public static final String GUEST_ROLE = "GUEST";

//    other project-related constants


}
