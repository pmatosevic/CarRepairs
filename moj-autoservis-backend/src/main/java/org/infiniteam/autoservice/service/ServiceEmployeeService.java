package org.infiniteam.autoservice.service;

import org.infiniteam.autoservice.model.AutoService;
import org.infiniteam.autoservice.model.ServiceEmployee;

import java.util.List;
import java.util.Optional;

public interface ServiceEmployeeService {

    List<ServiceEmployee> findAllByAutoService(AutoService userAutoService);

    ServiceEmployee add(ServiceEmployee employee);

    Optional<ServiceEmployee> findByUsername(String username);

    void delete(ServiceEmployee employee);

}
