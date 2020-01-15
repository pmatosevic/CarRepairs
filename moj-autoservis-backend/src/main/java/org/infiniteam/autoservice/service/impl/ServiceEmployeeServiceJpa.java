package org.infiniteam.autoservice.service.impl;

import org.infiniteam.autoservice.model.AutoService;
import org.infiniteam.autoservice.model.ServiceEmployee;
import org.infiniteam.autoservice.model.ServiceEmployeeType;
import org.infiniteam.autoservice.repository.ServiceEmployeeRepository;
import org.infiniteam.autoservice.repository.UserRepository;
import org.infiniteam.autoservice.service.ServiceEmployeeService;
import org.infiniteam.autoservice.service.UsernameExistsException;
import org.infiniteam.autoservice.service.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceEmployeeServiceJpa implements ServiceEmployeeService {

    @Autowired
    private ServiceEmployeeRepository serviceEmployeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<ServiceEmployee> findAllByAutoService(AutoService autoService) {
        return serviceEmployeeRepository.findAllByAutoService(autoService);
    }

    @Override
    public ServiceEmployee add(ServiceEmployee employee) {
        if (userRepository.existsByUsername(employee.getUsername())) {
            throw new UsernameExistsException("Username already exists.");
        }
        employee.setPasswordHash(passwordEncoder.encode(employee.getPasswordHash()));
        Assert.notNull(employee.getAutoService(), "Employee has to have an autoservice.");
        return serviceEmployeeRepository.saveAndFlush(employee);
    }

    @Override
    public Optional<ServiceEmployee> findByUsername(String username) {
        return serviceEmployeeRepository.findByUsername(username);
    }

    @Override
    public void delete(ServiceEmployee employee) {
        Assert.isTrue(employee.getEmployeeType() != ServiceEmployeeType.SERVICE_ADMINISTRATOR,
                "Service owner cannot be deleted.");
        serviceEmployeeRepository.delete(employee);
    }
}
