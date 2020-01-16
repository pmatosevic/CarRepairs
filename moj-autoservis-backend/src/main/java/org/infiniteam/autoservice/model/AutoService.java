package org.infiniteam.autoservice.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AutoService {

    @Id @GeneratedValue
    private Long autoServiceId;

    @Column(nullable = false)
    private String shopName;

    @Column(nullable = false)
    private String address;

    @Column(unique = true)
    private String oib;

    private double regularServicePrice;

    @OneToMany(mappedBy = "autoService")
    private List<ServiceLabor> serviceLabors;

    @OneToMany(mappedBy = "autoService")
    private List<VehiclePart> vehicleParts;

    @OneToMany(mappedBy = "autoService")
    private List<RepairOrder> repairOrders;

    @OneToMany(mappedBy = "autoService")
    private List<ServiceEmployee> employees = new ArrayList<>();

    @Column
    private boolean active = true;

    @Column
    private double latitude;

    @Column
    private double longitude;

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

    public void addEmployee(ServiceEmployee employee) {
        employee.setAutoService(this);
        employees.add(employee);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
