package org.infiniteam.autoservice.service;

import org.infiniteam.autoservice.model.AppUser;
import org.infiniteam.autoservice.model.ServiceEmployee;
import org.infiniteam.autoservice.repository.UserRepository;
import org.springframework.util.Assert;

public class Utility {

    private static String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    public static void checkOib(String oib) {
        // Check last digit
        if (oib.length() != 11) {
            throw new IllegalArgumentException("OIB nema ispravan oblik.");
        }
    }

    public static void checkEMail(String email) {
        if (!email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("E-mail nema ispravan oblik.");
        }
    }

    public static void checkUsername(String username) {
        if (username.isBlank()) {
            throw new IllegalArgumentException("Korisničko ime ne može biti prazno.");
        }
    }

    public static void checkPassword(String password) {
        if (password.length() <= 4) {
            throw new IllegalArgumentException("Lozinka treba imati minimalno 5 znakova.");
        }
    }

    public static void validateModifiableData(AppUser user) {
        checkEMail(user.getEmail());
        Assert.hasText(user.getFirstName(), "Ime ne smije biti prazno.");
        Assert.hasText(user.getLastName(), "Prezime ne smije biti prazno.");
    }

    public static void validateExists(AppUser user, UserRepository userRepository) {
        String username = user.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new UsernameExistsException("Korisničko ime već postoji.");
        }
    }
}
