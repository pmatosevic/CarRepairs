package org.infiniteam.autoservice.service;

import org.infiniteam.autoservice.model.AutoService;
import org.infiniteam.autoservice.model.ServiceEmployee;

import java.util.List;
import java.util.Optional;

public interface AutoServiceService {

    List<AutoService> findAllActive();

    AutoService fetch(long autoServiceId);

    AutoService create(AutoService autoService);

    AutoService update(AutoService autoService);

    Optional<AutoService> findById(long autoServiceId);

    void softDelete(AutoService autoService);

    boolean existsByOib(String oib);

    void createServiceWithOwner(AutoService newAutoService, ServiceEmployee owner);

}
