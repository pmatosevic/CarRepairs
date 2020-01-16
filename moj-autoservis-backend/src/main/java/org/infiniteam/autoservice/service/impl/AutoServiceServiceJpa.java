package org.infiniteam.autoservice.service.impl;

import org.infiniteam.autoservice.model.AutoService;
import org.infiniteam.autoservice.model.ServiceEmployee;
import org.infiniteam.autoservice.model.ServiceEmployeeType;
import org.infiniteam.autoservice.repository.*;
import org.infiniteam.autoservice.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AutoServiceServiceJpa implements AutoServiceService {

    @Autowired
    private AutoServiceRepository autoServiceRepository;

    @Autowired
    private ServiceEmployeeRepository serviceEmployeeRepository;

    @Autowired
    private VehiclePartRepository vehiclePartRepository;

    @Autowired
    private ServiceLaborRepository serviceLaborRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    public List<AutoService> findAllActive() {
        return autoServiceRepository.findAllByActive(true);
    }

    @Override
    public AutoService fetch(long autoServiceId) {
        return findById(autoServiceId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public AutoService create(AutoService autoService) {
        return autoServiceRepository.save(autoService);
    }

    @Override
    public AutoService update(AutoService autoService) {
        return autoServiceRepository.saveAndFlush(autoService);
    }

    @Override
    public Optional<AutoService> findById(long autoServiceId) {
        return autoServiceRepository.findById(autoServiceId);
    }

    @Override
    @Transactional
    public void softDelete(AutoService autoService) {
        List<ServiceEmployee> employees = serviceEmployeeRepository.findAllByAutoService(autoService);
        serviceEmployeeRepository.deleteAll(employees);
        vehiclePartRepository.deleteAllByAutoService(autoService);
        serviceLaborRepository.deleteAllByAutoService(autoService);
        autoService.setOib(null);
        autoService.setActive(false);
    }

    @Override
    public boolean existsByOib(String oib) {
        return autoServiceRepository.existsByOib(oib);
    }

    @Override
    @Transactional
    public void createServiceWithOwner(AutoService newAutoService, ServiceEmployee owner) {
        Utility.checkOib(newAutoService.getOib());
        Assert.hasText(newAutoService.getShopName(), "Shop name should not be blank.");
        Assert.hasText(newAutoService.getAddress(), "Address should not be blank.");
        newAutoService.setRegularServicePrice(0.0);

        autoServiceRepository.saveAndFlush(newAutoService);

        owner.setEmployeeType(ServiceEmployeeType.SERVICE_ADMINISTRATOR);
        owner.setAutoService(newAutoService);
        owner.setPasswordHash(passwordEncoder.encode(owner.getPasswordHash()));
        userService.validate(owner);

        serviceEmployeeRepository.saveAndFlush(owner);
    }


}
