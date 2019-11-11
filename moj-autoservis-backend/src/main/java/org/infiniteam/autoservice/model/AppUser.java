package org.infiniteam.autoservice.model;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class AppUser {

    @Id @GeneratedValue
    private Long userId;

    @Column(unique = true, nullable = false)
    @Size(min = 1, max = 20)
    private String username;

    @Column(nullable = false, length = 200)
    private String passwordHash;

    @Column
    private String email;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

}
