package org.infiniteam.autoservice.service;

import org.infiniteam.autoservice.service.impl.VehicleData;

public interface HuoService {

    VehicleData fetchVehicleData(String licencePlate) throws HuoServiceException;

}
