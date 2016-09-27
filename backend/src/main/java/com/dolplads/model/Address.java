package com.dolplads.model;

import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by dolplads on 26/09/16.
 */
@Embeddable
@NoArgsConstructor
public class Address {
    @Size(max = 100)
    private String streetName;
    @Size(max = 100)
    private String cityName;
    @Size(max = 100)
    @NotNull
    private String country;

    public Address(String streetName, String cityName, String country) {
        this.streetName = streetName;
        this.cityName = cityName;
        this.country = country;
    }
}
