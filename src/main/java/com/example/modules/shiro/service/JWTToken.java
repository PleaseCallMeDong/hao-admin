package com.example.modules.shiro.service;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * JSON Web Token
 *
 * @author MrBird
 */
public class JWTToken implements AuthenticationToken {

    private String token;

    private String expiresAt;

    public JWTToken(String token) {
        this.token = token;
    }

    public JWTToken(String token, String expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }
}
