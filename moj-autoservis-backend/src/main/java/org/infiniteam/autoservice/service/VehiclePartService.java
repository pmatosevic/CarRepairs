package org.infiniteam.autoservice.service;

import org.infiniteam.autoservice.model.AutoService;
import org.infiniteam.autoservice.model.VehiclePart;

import java.util.List;

public interface VehiclePartService {

    VehiclePart fetch(long vehiclePartId);

    List<VehiclePart> findAllByAutoService(AutoService autoService);

    VehiclePart addOrModify(VehiclePart vehiclePart);

    void delete(VehiclePart part);

}
