package org.infiniteam.autoservice.service.impl;

import org.infiniteam.autoservice.model.AutoService;
import org.infiniteam.autoservice.model.ServiceLabor;
import org.infiniteam.autoservice.repository.ServiceLaborRepository;
import org.infiniteam.autoservice.service.EntityNotFoundException;
import org.infiniteam.autoservice.service.ServiceLaborService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class ServiceLaborServiceJpa implements ServiceLaborService {

    @Autowired
    private ServiceLaborRepository serviceLaborRepository;

    @Override
    public ServiceLabor fetch(long serviceLaborId) {
        return serviceLaborRepository.findById(serviceLaborId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<ServiceLabor> findAllByAutoService(AutoService autoService) {
        return serviceLaborRepository.findAllByAutoService(autoService);
    }

    @Override
    public ServiceLabor addOrModify(ServiceLabor serviceLabor) {
        Assert.hasText(serviceLabor.getServiceName(), "Part name shuld not be blank.");
        Assert.isTrue(serviceLabor.getPrice() >= 0, "Price should be non negative");

        return serviceLaborRepository.save(serviceLabor);
    }

    @Override
    public void delete(ServiceLabor labor) {
        serviceLaborRepository.delete(labor);
    }


}
