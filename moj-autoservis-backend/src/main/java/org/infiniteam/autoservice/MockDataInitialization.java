package org.infiniteam.autoservice;

import org.infiniteam.autoservice.model.*;
import org.infiniteam.autoservice.repository.AutoServiceRepository;
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
    private AutoServiceRepository autoServiceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener
    public void appReady(ApplicationReadyEvent e) {
        VehicleOwner user = new VehicleOwner();
        user.setUsername("user1");
        user.setFirstName("Pero");
        user.setLastName("Perić");
        user.setPasswordHash(passwordEncoder.encode("user1"));

        AutoService autoService = new AutoService();
        autoService.setShopName("Najbolji auto servis");
        autoService.setAddress("Adresa");
        autoService.setOib("123456789");
        autoService.setLatitude(45.81);
        autoService.setLongitude(15.98);

        ServiceEmployee employee = new ServiceEmployee();
        employee.setUsername("employee");
        employee.setFirstName("Pero");
        employee.setLastName("Perić");
        employee.setPasswordHash(passwordEncoder.encode("employee"));
        employee.setEmployeeType(ServiceEmployeeType.REGULAR_EMPLOYEE);
        employee.setAutoService(autoService);

        ServiceEmployee boss = new ServiceEmployee();
        boss.setUsername("boss");
        boss.setFirstName("Pero");
        boss.setLastName("Perić");
        boss.setPasswordHash(passwordEncoder.encode("boss"));
        boss.setEmployeeType(ServiceEmployeeType.SERVICE_ADMINISTRATOR);
        boss.setAutoService(autoService);

        Administrator admin = new Administrator();
        admin.setUsername("admin");
        admin.setFirstName("Pero");
        admin.setLastName("Perić");
        admin.setPasswordHash(passwordEncoder.encode("admin"));

        try {
            autoServiceRepository.save(autoService);
            userRepository.save(user);
            userRepository.save(employee);
            userRepository.save(boss);
            userRepository.save(admin);
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }
}
