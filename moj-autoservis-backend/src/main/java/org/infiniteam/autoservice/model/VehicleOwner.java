package org.infiniteam.autoservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class VehicleOwner extends User {

    @Column
    private String oib;

}
