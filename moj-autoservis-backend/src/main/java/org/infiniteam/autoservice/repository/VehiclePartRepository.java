package org.infiniteam.autoservice.repository;

import org.infiniteam.autoservice.model.AutoService;
import org.infiniteam.autoservice.model.VehiclePart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehiclePartRepository extends JpaRepository<VehiclePart, Long> {

    List<VehiclePart> findAllByAutoService(AutoService autoService);

}
