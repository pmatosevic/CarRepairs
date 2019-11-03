package org.infiniteam.autoservice.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public enum UserRole {
    VEHICLE_OWNER,
    EMPLOYEE,
    SERVICE_OWNER,
    ADMINISTRATOR
}
