package org.infiniteam.autoservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class AutoService {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String shopName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String oib;

    private double regularServicePrice;

}
