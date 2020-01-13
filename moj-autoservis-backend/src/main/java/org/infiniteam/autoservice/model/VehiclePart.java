package org.infiniteam.autoservice.model;

import javax.persistence.*;

@Entity
public class VehiclePart implements Product {

    @Id @GeneratedValue
    private Long partId;

    @ManyToOne
    private AutoService autoService;

    @Column
    private String partName = "";

    @Column
    private int estimatedDurationInKm;

    @Column(nullable = false)
    private double price;

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public AutoService getAutoService() {
        return autoService;
    }

    public void setAutoService(AutoService autoService) {
        this.autoService = autoService;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public int getEstimatedDurationInKm() {
        return estimatedDurationInKm;
    }

    public void setEstimatedDurationInKm(int estimatedDurationInKm) {
        this.estimatedDurationInKm = estimatedDurationInKm;
    }

    @Override
    public String getName() {
        return partName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
