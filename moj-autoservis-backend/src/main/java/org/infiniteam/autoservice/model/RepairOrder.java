package org.infiniteam.autoservice.model;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class RepairOrder {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Vehicle vehicle;

    @ManyToOne
    private AutoService autoService;

    @Enumerated(EnumType.STRING)
    private ServiceJobStatus serviceJobStatus = ServiceJobStatus.ACCEPTANCE_WAITING;

    @Column
    private Double price;

}
