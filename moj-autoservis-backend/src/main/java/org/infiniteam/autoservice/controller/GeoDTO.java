package org.infiniteam.autoservice.controller;

public class GeoDTO {

    private String name;
    private double[] location;

    public GeoDTO() {
    }

    public GeoDTO(String name, double latitude, double longitude) {
        this.name = name;
        this.location = new double[] {latitude, longitude};
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }
}
