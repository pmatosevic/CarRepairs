package org.infiniteam.autoservice.security;

import org.infiniteam.autoservice.model.AppUser;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private AppUser appUser;

    public CurrentUser(String username, String password, Collection<? extends GrantedAuthority> authorities, AppUser appUser) {
        super(username, password, authorities);
        this.appUser = appUser;
    }

    public AppUser getAppUser() {
        return appUser;
    }
}
