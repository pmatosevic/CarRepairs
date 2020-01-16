package org.infiniteam.autoservice.repository;

import org.infiniteam.autoservice.model.Vehicle;
import org.infiniteam.autoservice.model.VehicleOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findAllByOwner(VehicleOwner owner);

    boolean existsByLicencePlateAndOwner(String licencePlate, VehicleOwner vehicleOwner);

}
