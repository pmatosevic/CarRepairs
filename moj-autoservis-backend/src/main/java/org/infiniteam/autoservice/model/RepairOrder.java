package org.infiniteam.autoservice.model;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column
    private LocalDateTime creationTime;

    @Column
    private LocalDateTime finishTime;

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

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }
}
