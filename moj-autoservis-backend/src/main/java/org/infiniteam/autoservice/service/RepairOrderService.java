package org.infiniteam.autoservice.service;

import org.infiniteam.autoservice.model.*;

import java.util.List;
import java.util.Optional;

public interface RepairOrderService {

    List<RepairOrder> findAll();

    RepairOrder fetch(long repairOrderId);

    RegularRepairOrder fetchRegularRepairOrder(long repairOrderId);

    RepairingRepairOrder fetchRepairingRepairOrder(long repairOrderId);

    RepairOrder create(AutoService autoService, Vehicle vehicle, RepairOrderType type);

    Optional<RepairOrder> findById(long repairOrderId);

    List<RepairOrder> findAllByVehicle(Vehicle vehicle);

    boolean existsByVehicleAndServiceJobStatus(Vehicle vehicle, ServiceJobStatus status);

    List<RepairOrder> findAllByServiceJobStatusAndAutoService(ServiceJobStatus status, AutoService autoService);

    void setFinished(RepairOrder repairOrder);

    void updateRegularRepairOrder(RegularRepairOrder ro, int kilometers, boolean repairRecommended);

}
