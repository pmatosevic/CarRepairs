package org.infiniteam.autoservice.service.impl;

import org.infiniteam.autoservice.model.AppUser;
import org.infiniteam.autoservice.repository.UserRepository;
import org.infiniteam.autoservice.service.EntityNotFoundException;
import org.infiniteam.autoservice.service.UserService;
import org.infiniteam.autoservice.service.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserServiceJpa implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<AppUser> findById(long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public AppUser fetch(long userId) {
        return findById(userId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Transactional
    public void changePassword(AppUser user, String oldPassword, String newPassword) {
        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Trenutna lozinka je netočna.");
        }
        Utility.checkPassword(newPassword);
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public AppUser modify(AppUser appUser) {
        validate(appUser);
        userRepository.saveAndFlush(appUser);
        return appUser;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Optional<AppUser> findByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public void validate(AppUser user) {
        Utility.validateModifiableData(user);
    }
}
