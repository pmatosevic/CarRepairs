package org.infiniteam.autoservice.repository;

import org.infiniteam.autoservice.model.AutoService;
import org.infiniteam.autoservice.model.ServiceEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.dnd.Autoscroll;
import java.util.List;
import java.util.Optional;

public interface ServiceEmployeeRepository extends JpaRepository<ServiceEmployee, Long> {

    Optional<ServiceEmployee> findByUsername(String username);

    List<ServiceEmployee> findAllByAutoService(AutoService autoService);

}
