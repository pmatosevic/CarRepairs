package org.infiniteam.autoservice.security;

import org.infiniteam.autoservice.model.*;
import org.infiniteam.autoservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Map<Class<? extends User>, List<GrantedAuthority>> authorities = Map.of(
            VehicleOwner.class, AuthorityUtils.createAuthorityList("ROLE_USER"),
            ServiceEmployee.class, AuthorityUtils.createAuthorityList("ROLE_SERVICE_EMPLOYEE"),
            Administrator.class, AuthorityUtils.createAuthorityList("ROLE_ADMIN")
    );

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Username is not found in the DB.")
        );

        return new CurrentUser(
                username, user.getPasswordHash(), userAuthorities(user), user
        );
    }

    private List<GrantedAuthority> userAuthorities(User user) {
        List<GrantedAuthority> authList = new ArrayList<>();
        authList.addAll(authorities.get(user.getClass()));
        if (user instanceof ServiceEmployee && ((ServiceEmployee)user).getEmployeeType() == ServiceEmployeeType.SERVICE_ADMINISTRATOR) {
            authList.addAll(AuthorityUtils.createAuthorityList("ROLE_SERVICE_ADMIN"));
        }
        return authList;
    }
}
