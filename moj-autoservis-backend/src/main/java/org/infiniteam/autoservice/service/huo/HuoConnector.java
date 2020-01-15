package org.infiniteam.autoservice.service.huo;

import org.infiniteam.autoservice.service.impl.VehicleData;

public interface HuoConnector {

    VehicleData fetchVehicleData(String licencePlate) throws HuoConnectorException;

}
