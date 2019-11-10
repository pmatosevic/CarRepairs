package org.infiniteam.autoservice;

import org.infiniteam.autoservice.model.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private User user;

    public CurrentUser(String username, String password, Collection<? extends GrantedAuthority> authorities, User user) {
        super(username, password, authorities);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
