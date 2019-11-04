package org.infiniteam.autoservice.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class RegularRepairOrder extends RepairOrder {

    private int kilometers;

    private boolean malfunctionsObserved;

}
