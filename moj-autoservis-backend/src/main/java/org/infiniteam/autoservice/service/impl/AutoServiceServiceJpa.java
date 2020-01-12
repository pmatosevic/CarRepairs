package org.infiniteam.autoservice.service.impl;

import org.infiniteam.autoservice.model.AutoService;
import org.infiniteam.autoservice.repository.AutoServiceRepository;
import org.infiniteam.autoservice.service.AutoServiceService;
import org.infiniteam.autoservice.service.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class AutoServiceServiceJpa implements AutoServiceService {

    @Autowired
    private AutoServiceRepository autoServiceRepository;

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


}
