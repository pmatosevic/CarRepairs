package org.infiniteam.autoservice.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class ServiceEmployee extends User {

    @ManyToOne
    private AutoService autoService;

}
