package org.infiniteam.autoservice.model;

import javax.persistence.*;

@Entity
public class ServiceLabor {

    @Id @GeneratedValue
    private Long serviceLaborId;

    @ManyToOne
    private AutoService autoService;

    @Column(nullable = false)
    private String serviceName;

    @Column(nullable = false)
    private double price;

    public Long getServiceLaborId() {
        return serviceLaborId;
    }

    public void setServiceLaborId(Long serviceLaborId) {
        this.serviceLaborId = serviceLaborId;
    }

    public AutoService getAutoService() {
        return autoService;
    }

    public void setAutoService(AutoService autoService) {
        this.autoService = autoService;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
