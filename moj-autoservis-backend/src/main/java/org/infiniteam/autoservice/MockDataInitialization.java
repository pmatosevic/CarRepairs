package org.infiniteam.autoservice;

import org.infiniteam.autoservice.model.*;
import org.infiniteam.autoservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MockDataInitialization {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener
    public void appReady(ApplicationReadyEvent e) {
        VehicleOwner user = new VehicleOwner();
        user.setUsername("user1");
        user.setPasswordHash(passwordEncoder.encode("user1"));

        ServiceEmployee employee = new ServiceEmployee();
        employee.setUsername("employee");
        employee.setPasswordHash(passwordEncoder.encode("employee"));
        employee.setEmployeeType(ServiceEmployeeType.REGULAR_EMPLOYEE);

        ServiceEmployee boss = new ServiceEmployee();
        boss.setUsername("boss");
        boss.setPasswordHash(passwordEncoder.encode("boss"));
        boss.setEmployeeType(ServiceEmployeeType.SERVICE_ADMINISTRATOR);

        Administrator admin = new Administrator();
        admin.setUsername("admin");
        admin.setPasswordHash(passwordEncoder.encode("admin"));

        userRepository.save(user);
        userRepository.save(employee);
        userRepository.save(boss);
        userRepository.save(admin);
    }
}
