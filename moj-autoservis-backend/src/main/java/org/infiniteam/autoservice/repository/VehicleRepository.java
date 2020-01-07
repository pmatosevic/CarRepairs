package org.infiniteam.autoservice.repository;

import org.infiniteam.autoservice.model.Vehicle;
import org.infiniteam.autoservice.model.VehicleOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    boolean existsByLicencePlateAndOwner(String licencePlate, VehicleOwner vehicleOwner);

}
