package org.infiniteam.autoservice.repository;

import org.infiniteam.autoservice.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> getByUsername(String username);

}
