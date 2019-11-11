package org.infiniteam.autoservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class AutoService {

    @Id @GeneratedValue
    private Long autoServiceId;

    @Column(nullable = false)
    private String shopName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String oib;

    private double regularServicePrice;

    public Long getAutoServiceId() {
        return autoServiceId;
    }

    public void setAutoServiceId(Long autoServiceId) {
        this.autoServiceId = autoServiceId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }

    public double getRegularServicePrice() {
        return regularServicePrice;
    }

    public void setRegularServicePrice(double regularServicePrice) {
        this.regularServicePrice = regularServicePrice;
    }
}
