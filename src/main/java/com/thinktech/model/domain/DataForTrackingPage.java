package com.thinktech.model.domain;

import java.time.LocalDate;
import java.util.Date;

public class DataForTrackingPage {
    //private int userId;
    private int trackingItemId;
    private String trackingItemName;
    private int distance;
    private double emission;
    private boolean changeable;
    private String trackingDate;
    private int idTrackRecord;
    private int idJourney;
    private String authUserId;
    public DataForTrackingPage() {}

    public DataForTrackingPage(/*int userId,*/ int trackingItemId, String trackingItemName, int distance,
                                               double emission, boolean changeable, String trackingDate,
                                               int idTrackRecord, int idJourney, String authUserId) {
        //this.userId = userId;
        this.trackingItemId = trackingItemId;
        this.trackingItemName = trackingItemName;
        this.distance = distance;
        this.emission = emission;
        this.changeable = changeable;
        this.trackingDate = trackingDate;
        this.idTrackRecord = idTrackRecord;
        this.idJourney = idJourney;
        this.authUserId = authUserId;
    }

    /*public int getUserId() {
        return userId;
    }*/

    public int getTrackingItemId() {
        return trackingItemId;
    }

    public String getTrackingItemName() {
        return trackingItemName;
    }

    public int getDistance() {
        return distance;
    }

    public double getEmission() {
        return emission;
    }

    public boolean isChangeable() {
        return changeable;
    }

    public String getTrackingDate() {
        return trackingDate;
    }

    public int getIdTrackRecord() {
        return idTrackRecord;
    }

    public int getIdJourney() {
        return idJourney;
    }

    public String getAuthUserId() {return authUserId;}

    public void setIdJourney(int idJourney) {
        this.idJourney = idJourney;
    }

    public void setEmission(double emission) {
        this.emission = emission;
    }
}
