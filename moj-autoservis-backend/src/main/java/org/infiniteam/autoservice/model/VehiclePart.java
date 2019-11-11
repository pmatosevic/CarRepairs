package org.infiniteam.autoservice.model;

import javax.persistence.*;

@Entity
public class VehiclePart {

    @Id @GeneratedValue
    private Long partId;

    @ManyToOne
    private AutoService autoService;

    @Column
    private String partName;

    @Column
    private int estimatedDurationInKm;

    @Column(nullable = false)
    private double price;

}
