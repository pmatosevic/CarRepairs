package org.infiniteam.autoservice.service;

import org.infiniteam.autoservice.model.AppUser;
import org.infiniteam.autoservice.model.VehicleOwner;

import java.util.List;

public interface VehicleOwnerService {

    VehicleOwner fetch(long userId);

    List<VehicleOwner> findAll();

    void delete(VehicleOwner vehicleOwner);

}
