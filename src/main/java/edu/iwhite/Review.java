package edu.iwhite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class Review {

    private final String reviewID;
    private final String userID;
    private final String businessID;
    private final double stars;
    private final String text;

    public String getReviewID() {
        return reviewID;
    }

    public String getUserID() {
        return userID;
    }

    public String getBusinessID() {
        return businessID;
    }

    public double getStars() {
        return stars;
    }

    public String getText() {
        return text;
    }

    @JsonCreator
    public Review(
            @JsonProperty("review_id")   String reviewID,
            @JsonProperty("user_id")     String userID,
            @JsonProperty("business_id") String businessID,
            @JsonProperty("stars")       double stars,
            @JsonProperty("text")        String text
    ) {
        this.reviewID = reviewID;
        this.userID = userID;
        this.businessID = businessID;
        this.stars = stars;
        this.text = text;
    }
}