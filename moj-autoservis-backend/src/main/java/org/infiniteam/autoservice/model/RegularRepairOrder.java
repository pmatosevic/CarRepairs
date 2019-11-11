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

    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    public boolean isRepairRecommended() {
        return repairRecommended;
    }

    public void setRepairRecommended(boolean repairRecommended) {
        this.repairRecommended = repairRecommended;
    }

    public String getObservedMalfunctions() {
        return observedMalfunctions;
    }

    public void setObservedMalfunctions(String observedMalfunctions) {
        this.observedMalfunctions = observedMalfunctions;
    }
}
