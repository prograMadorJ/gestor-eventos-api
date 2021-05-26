package com.desafiobackend.gestoreventosapi.config.security;

public class SecurityConstants {

    public static final String SECRET = "SECRET_KEY";
    public static final long EXPIRATION_TIME = 900_000; // 15 mins
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/users";
    public static final String SIGN_IN_URL = "/api/auth";
    public static final String WEB_APP_URL = "/app/**";
    public static final String WEB_APP_HOME_URL = "/app/login";
}
