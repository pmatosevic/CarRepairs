package org.infiniteam.autoservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class RegularRepairOrder extends RepairOrder {

    @Column
    private int kilometers;

    @Column
    private boolean repairRecommended;

    @Column
    private String observedMalfunctions;

}
