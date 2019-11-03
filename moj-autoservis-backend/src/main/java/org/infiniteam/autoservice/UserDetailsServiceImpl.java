package org.infiniteam.autoservice;

import org.infiniteam.autoservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Username is not found in the DB.")
        );

        return new org.springframework.security.core.userdetails.User(
                username, user.getPasswordHash(), userAuthorities(user)
        );
    }

    private List<GrantedAuthority> userAuthorities(User user) {
        return new ArrayList<>();
    }
}
