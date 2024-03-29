package org.infiniteam.autoservice.repository;

import org.infiniteam.autoservice.model.AutoService;
import org.infiniteam.autoservice.model.RepairOrder;
import org.infiniteam.autoservice.model.ServiceJobStatus;
import org.infiniteam.autoservice.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairOrderRepository extends JpaRepository<RepairOrder, Long> {

    List<RepairOrder> findAllByServiceJobStatusAndAutoService(ServiceJobStatus serviceJobStatus, AutoService autoService);

    List<RepairOrder> findAllByVehicle(Vehicle vehicle);

    boolean existsByVehicleAndServiceJobStatus(Vehicle vehicle, ServiceJobStatus serviceJobStatus);

}
