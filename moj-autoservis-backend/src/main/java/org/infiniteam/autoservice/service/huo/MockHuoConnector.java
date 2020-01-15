package org.infiniteam.autoservice.service.huo;

import org.infiniteam.autoservice.service.impl.VehicleData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class MockHuoConnector implements HuoConnector {

    private static List<String> MODELS = List.of(
        "Tesla Cybertruck"
    );

    private static List<String> VINS = List.of(
        "4T1BG28K81U790207"
    );

    private Random rand = new Random();

    @Override
    public VehicleData fetchVehicleData(String licencePlate) throws HuoServiceException {
        String model = MODELS.get(rand.nextInt(MODELS.size()));
        String vin = UUID.randomUUID().toString().replace("-", "");
        return new VehicleData(licencePlate, vin, model);
    }

}
