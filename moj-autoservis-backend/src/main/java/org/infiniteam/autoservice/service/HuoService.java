package org.infiniteam.autoservice.service;

public interface HuoService {

    VehicleData fetchVehicleData(String licencePlate) throws HuoServiceException;

}
