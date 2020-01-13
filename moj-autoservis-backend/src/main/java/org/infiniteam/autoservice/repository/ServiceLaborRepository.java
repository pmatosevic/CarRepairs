package org.infiniteam.autoservice.repository;

import org.infiniteam.autoservice.model.AutoService;
import org.infiniteam.autoservice.model.ServiceLabor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceLaborRepository extends JpaRepository<ServiceLabor, Long> {

    List<ServiceLabor> findAllByAutoService(AutoService autoService);

    void deleteAllByAutoService(AutoService autoService);

}
