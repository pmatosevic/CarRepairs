package org.infiniteam.autoservice.model;

import javax.persistence.*;

@Entity
public class ServiceLabor {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private AutoService autoService;

    @Column(nullable = false)
    private String serviceName;

    @Column(nullable = false)
    private double price;

}
