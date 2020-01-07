package org.infiniteam.autoservice.dto;

import org.infiniteam.autoservice.model.RepairOrderType;

public class OpenRepairOrderDto {

    private Long autoServiceId;
    private RepairOrderType repairOrderType;

    public Long getAutoServiceId() {
        return autoServiceId;
    }

    public RepairOrderType getRepairOrderType() {
        return repairOrderType;
    }
}
