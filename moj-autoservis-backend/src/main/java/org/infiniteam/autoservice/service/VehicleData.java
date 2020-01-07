package org.infiniteam.autoservice.service;

public class VehicleData {

    private String licencePlate;
    private String vinNumber;
    private String model;

    public VehicleData(String licencePlate, String vinNumber, String model) {
        this.licencePlate = licencePlate;
        this.vinNumber = vinNumber;
        this.model = model;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public String getModel() {
        return model;
    }
}
