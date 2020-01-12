package org.infiniteam.autoservice.service;

import org.infiniteam.autoservice.model.Vehicle;
import org.infiniteam.autoservice.model.VehicleOwner;
import org.infiniteam.autoservice.service.impl.VehicleData;

import java.util.List;
import java.util.Optional;

public interface VehicleService {

    Vehicle fetch(long vehicleId);

    Optional<Vehicle> findById(long vehicleId);

    List<Vehicle> findAllByOwner(VehicleOwner owner);

    boolean existsByLicencePlateAndOwner(String licencePlate, VehicleOwner owner);

    Vehicle create(VehicleData vehicleData, VehicleOwner owner);

}
