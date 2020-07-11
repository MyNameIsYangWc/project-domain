package com.chao.domain.model;

import com.chao.domain.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

public class User {

    private String username;
    private String password;
    private Collection authorities;
    private boolean enabled;
    private String fileId;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Collection getAuthorities() {
        return authorities;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(Collection authorities) {
        this.authorities = authorities;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        if(StringUtils.isNotBlank(this.password)){
            this.password=SecurityUtils.pwdSecurity(this.password);
        }
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", authorities=" + authorities +
                ", enabled=" + enabled +
                ", fileId='" + fileId + '\'' +
                '}';
    }
}