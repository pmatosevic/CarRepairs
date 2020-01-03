package org.infiniteam.autoservice.rest;

import org.infiniteam.autoservice.model.AppUser;
import org.infiniteam.autoservice.security.SpringCurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/current-user")
public class CurrentUserController {

    @GetMapping("/")
    public AppUser getGroupMembers(Authentication authentication) {
        if (authentication.isAuthenticated()) {
            AppUser appUser = ((SpringCurrentUser) authentication.getPrincipal()).getAppUser();
            return appUser;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not logged in.");
        }
    }


}
