package edu.iwhite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class Business {

    private final String businessID;
    private final String name;
    private final double longitude;
    private final double latitude;

    public final String getBusinessID() {
        return businessID;
    }

    public String getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @JsonCreator
    public Business(
            @JsonProperty("business_id") String businessID,
            @JsonProperty("name")        String name,
            @JsonProperty("longitude")   double longitude,
            @JsonProperty("latitude")    double latitude
    ) {
        this.businessID = businessID;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return name;
    }
}