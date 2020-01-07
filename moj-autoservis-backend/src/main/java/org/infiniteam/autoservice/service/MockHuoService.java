package org.infiniteam.autoservice.service;

import java.util.List;
import java.util.Random;

public class MockHuoService implements HuoService {

    private static List<String> MODELS = List.of(
        "Tesla Cybertruck"
    );

    private static List<String> VINS = List.of(
        "4T1BG28K81U790207"
    );

    private Random rand = new Random();

    @Override
    public VehicleData fetchVehicleData(String licencePlate) {
        String model = MODELS.get(rand.nextInt(MODELS.size()));
        String vin = VINS.get(rand.nextInt(VINS.size()));
        return new VehicleData(licencePlate, vin, model);
    }

}
