package org.infiniteam.autoservice.service;

import org.infiniteam.autoservice.model.AppUser;
import org.infiniteam.autoservice.model.VehicleOwner;

import java.util.Optional;

public interface UserService {

    Optional<AppUser> findById(long userId);

    AppUser fetch(long userId);

    void changePassword(AppUser user, String oldPassword, String newPassword);

    AppUser modify(AppUser appUser);

    boolean existsByUsername(String username);

    Optional<AppUser> findByUsername(String username);

    void validate(AppUser user);

}
