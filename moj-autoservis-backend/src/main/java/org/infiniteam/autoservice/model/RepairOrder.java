package org.infiniteam.autoservice.model;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class RepairOrder {

    @Id @GeneratedValue
    private Long repairOrderId;

    @ManyToOne
    private Vehicle vehicle;

    @ManyToOne
    private AutoService autoService;

    @Enumerated(EnumType.STRING)
    private ServiceJobStatus serviceJobStatus = ServiceJobStatus.ACCEPTANCE_WAITING;

    @Column
    private Double price;

    public Long getRepairOrderId() {
        return repairOrderId;
    }

    public void setRepairOrderId(Long repairOrderId) {
        this.repairOrderId = repairOrderId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public AutoService getAutoService() {
        return autoService;
    }

    public void setAutoService(AutoService autoService) {
        this.autoService = autoService;
    }

    public ServiceJobStatus getServiceJobStatus() {
        return serviceJobStatus;
    }

    public void setServiceJobStatus(ServiceJobStatus serviceJobStatus) {
        this.serviceJobStatus = serviceJobStatus;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
