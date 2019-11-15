package org.infiniteam.autoservice.model;

import javax.persistence.*;

@Entity
public class RepairOrderItem {

    @Id @GeneratedValue
    private Long itemId;

    @ManyToOne
    private RepairingRepairOrder repairOrder;

    @Column(nullable = false)
    private String name;

    @Column
    private double price;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public RepairingRepairOrder getRepairOrder() {
        return repairOrder;
    }

    public void setRepairOrder(RepairingRepairOrder repairOrder) {
        this.repairOrder = repairOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
