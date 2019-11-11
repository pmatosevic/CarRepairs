package org.infiniteam.autoservice.model;

import javax.persistence.*;

@Entity
public class Vehicle {

    @Id @GeneratedValue
    private Long vehicleId;

    @Column(nullable = false)
    private String licencePlate;

    @ManyToOne
    private VehicleOwner owner;

    @Column(nullable = false)
    private String vinNumber;

    private String vehicleModel;
}
