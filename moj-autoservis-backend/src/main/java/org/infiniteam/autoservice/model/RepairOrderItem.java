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

}
