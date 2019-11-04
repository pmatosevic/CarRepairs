package org.infiniteam.autoservice.model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class RepairingRepairOrder extends RepairOrder {

    @ManyToMany
    private List<VehiclePart> changedParts;

    @ManyToMany
    private List<ServiceLabor> serviceLabors;

}
