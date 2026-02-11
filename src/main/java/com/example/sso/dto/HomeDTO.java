package com.example.sso.dto;

import java.io.Serializable;

public class HomeDTO implements Serializable {
    private boolean userAuthenticated;
    private String username;

    private LoginVO loginVo;
    private UserDetailsDTO userDetails;

    public boolean isUserAuthenticated() { return userAuthenticated; }
    public void setUserAuthenticated(boolean userAuthenticated) { this.userAuthenticated = userAuthenticated; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LoginVO getLoginVo() { return loginVo; }
    public void setLoginVo(LoginVO loginVo) { this.loginVo = loginVo; }

    public UserDetailsDTO getUserDetails() { return userDetails; }
    public void setUserDetails(UserDetailsDTO userDetails) { this.userDetails = userDetails; }
}
