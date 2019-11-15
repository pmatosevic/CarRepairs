package org.infiniteam.autoservice.model;

import javax.persistence.*;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class RepairingRepairOrder extends RepairOrder {

    @Column
    private String malfunctions;

    @OneToMany(mappedBy = "repairOrder", fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RepairOrderItem> items;

    public String getMalfunctions() {
        return malfunctions;
    }

    public void setMalfunctions(String malfunctions) {
        this.malfunctions = malfunctions;
    }

    public List<RepairOrderItem> getItems() {
        return items;
    }

    public void setItems(List<RepairOrderItem> items) {
        this.items = items;
    }
}