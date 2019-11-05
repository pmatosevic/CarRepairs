package org.infiniteam.autoservice.model;

import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Entity
public class ServiceEmployee extends User {

    @ManyToOne
    private AutoService autoService;

    @Enumerated(EnumType.STRING)
    @NonNull
    private ServiceEmployeeType employeeType = ServiceEmployeeType.REGULAR_EMPLOYEE;

}
