package org.infiniteam.autoservice.service;

import org.infiniteam.autoservice.model.AppUser;
import org.infiniteam.autoservice.model.ServiceEmployee;
import org.infiniteam.autoservice.repository.UserRepository;
import org.springframework.util.Assert;

public class Utility {

    public static boolean checkOib(String oib) {
        return true;
    }

    public static boolean checkEMail(String email) {
        return true;
    }

    public static boolean checkUsername(String username) {
        return true;
    }

    public static boolean checkPassword(String password) {
        return true;
    }

    public static void validateUser(AppUser user, UserRepository userRepository) {
        String username = user.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new UsernameExistsException("Username " + username + " already exists.");
        }
    }
}
