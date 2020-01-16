package org.infiniteam.autoservice.service.huo;

import org.infiniteam.autoservice.service.impl.VehicleData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class MockHuoConnector implements HuoConnector {

    private static List<String> MODELS = List.of(
        "Audi A1",
        "Ford Mustang",
        "Subaru Impreza",
        "BMW X3",
        "Infiniti QX60",
        "Mercedes CLS"
    );

    private Random rand = new Random();

    @Override
    public VehicleData fetchVehicleData(String licencePlate) throws HuoConnectorException {
        String model = MODELS.get(rand.nextInt(MODELS.size()));
        String vin = UUID.randomUUID().toString().replace("-", "").toUpperCase().substring(0, 18);
        String decodedLicence = decodeLicence(licencePlate);

        return new VehicleData(decodedLicence, vin, model);
    }

    private String decodeLicence(String licencePlate) throws HuoConnectorException {
        String city, number, chars;
        try {
            licencePlate = licencePlate.replace(" ", "");
            city = licencePlate.substring(0, 2);
            int pos = 2;
            while (Character.isDigit(licencePlate.charAt(pos))) pos++;
            number = licencePlate.substring(2, pos);
            chars = licencePlate.substring(pos);
        } catch (RuntimeException e) {
            throw new HuoConnectorException("Invalid licence plate.");
        }
        if (number.length() < 3 || number.length() > 4) throw new HuoConnectorException("Invalid licence plate.");
        return city + " " + number + " " + chars;
    }

}
