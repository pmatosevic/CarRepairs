package org.infiniteam.autoservice.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class AutoService {

    @Id @GeneratedValue
    private Long autoServiceId;

    @Column(nullable = false)
    private String shopName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String oib;

    private double regularServicePrice;

    @OneToMany
    private List<ServiceLabor> serviceLabors;

    @OneToMany
    private List<VehiclePart> vehicleParts;

    @OneToMany
    private List<RepairOrder> repairOrders;

    @OneToMany
    private List<ServiceEmployee> employees;

    public Long getAutoServiceId() {
        return autoServiceId;
    }

    public void setAutoServiceId(Long autoServiceId) {
        this.autoServiceId = autoServiceId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }

    public double getRegularServicePrice() {
        return regularServicePrice;
    }

    public void setRegularServicePrice(double regularServicePrice) {
        this.regularServicePrice = regularServicePrice;
    }

    public List<ServiceLabor> getServiceLabors() {
        return serviceLabors;
    }

    public void setServiceLabors(List<ServiceLabor> serviceLabors) {
        this.serviceLabors = serviceLabors;
    }

    public List<VehiclePart> getVehicleParts() {
        return vehicleParts;
    }

    public void setVehicleParts(List<VehiclePart> vehicleParts) {
        this.vehicleParts = vehicleParts;
    }

    public List<RepairOrder> getRepairOrders() {
        return repairOrders;
    }

    public void setRepairOrders(List<RepairOrder> repairOrders) {
        this.repairOrders = repairOrders;
    }

    public List<ServiceEmployee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<ServiceEmployee> employees) {
        this.employees = employees;
    }
}
