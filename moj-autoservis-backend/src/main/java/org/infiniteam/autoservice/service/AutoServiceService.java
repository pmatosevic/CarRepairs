package org.infiniteam.autoservice.service;

import org.infiniteam.autoservice.model.AutoService;

import java.util.List;
import java.util.Optional;

public interface AutoServiceService {

    List<AutoService> findAll();

    AutoService fetch(long autoServiceId);

    AutoService create(AutoService autoService);

    AutoService update(AutoService autoService);

    Optional<AutoService> findById(long autoServiceId);

    void softDelete(AutoService autoService);
}
