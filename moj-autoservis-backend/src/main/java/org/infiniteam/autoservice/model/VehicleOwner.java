package org.infiniteam.autoservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class VehicleOwner extends AppUser {

    @Column
    private String oib;

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }
}
