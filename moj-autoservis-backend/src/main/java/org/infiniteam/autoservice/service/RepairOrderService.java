package org.infiniteam.autoservice.service;

import org.infiniteam.autoservice.model.*;

import java.util.List;
import java.util.Optional;

public interface RepairOrderService {

    List<RepairOrder> findAll();

    RepairOrder fetch(long repairOrderId);

    RepairOrder create(AutoService autoService, Vehicle vehicle, RepairOrderType type);

    Optional<RepairOrder> findById(long repairOrderId);

    List<RepairOrder> findAllByVehicle(Vehicle vehicle);

    boolean existsByVehicleAndServiceJobStatus(Vehicle vehicle, ServiceJobStatus status);
}
