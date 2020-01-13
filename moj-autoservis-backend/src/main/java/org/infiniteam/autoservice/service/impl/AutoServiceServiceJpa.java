package org.infiniteam.autoservice.service.impl;

import org.infiniteam.autoservice.model.AutoService;
import org.infiniteam.autoservice.model.ServiceEmployee;
import org.infiniteam.autoservice.repository.AutoServiceRepository;
import org.infiniteam.autoservice.repository.ServiceEmployeeRepository;
import org.infiniteam.autoservice.repository.ServiceLaborRepository;
import org.infiniteam.autoservice.repository.VehiclePartRepository;
import org.infiniteam.autoservice.service.AutoServiceService;
import org.infiniteam.autoservice.service.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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



    @Override
    public List<AutoService> findAll() {
        return autoServiceRepository.findAll();
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
        return autoServiceRepository.save(autoService);
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


}
