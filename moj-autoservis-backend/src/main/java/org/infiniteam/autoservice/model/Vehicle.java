package org.infiniteam.autoservice.model;

import org.infiniteam.autoservice.service.impl.VehicleData;

import javax.persistence.*;

@Entity
public class Vehicle {

    @Id @GeneratedValue
    private Long vehicleId;

    @Column(nullable = false)
    private String licencePlate;

    @ManyToOne
    private VehicleOwner owner;

    @Column(nullable = false)
    private String vinNumber;

    private String vehicleModel;

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public VehicleOwner getOwner() {
        return owner;
    }

    public void setOwner(VehicleOwner owner) {
        this.owner = owner;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public Vehicle() {
    }

    public Vehicle(VehicleData vehicleData, VehicleOwner owner) {
        this.licencePlate = vehicleData.getLicencePlate();
        this.vinNumber = vehicleData.getVinNumber();
        this.vehicleModel = vehicleData.getModel();
        this.owner = owner;
    }
}
