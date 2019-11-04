package org.infiniteam.autoservice.model;

import javax.persistence.*;

@Entity
public class VehiclePart {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private AutoService autoService;

    @Column
    private int estimatedDurationInKm;

    @Column(nullable = false)
    private double price;

}
