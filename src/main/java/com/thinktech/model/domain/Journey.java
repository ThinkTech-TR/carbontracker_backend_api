package com.thinktech.model.domain;

import java.util.Date;

public class Journey {
    private int journey_id;
    private int user_id;
    private int transport_id;
    private Date journey_date;
    private int distance_miles;
    private String transport_type;

    public Journey(int journey_id, int user_id, int transport_id, Date journey_date, int distance_miles, String transport_type) {
        this.journey_id = journey_id;
        this.user_id = user_id;
        this.transport_id = transport_id;
        this.journey_date = journey_date;
        this.distance_miles = distance_miles;
        this.transport_type = transport_type;
    }

    public int getJourney_id() {
        return journey_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getTransport_id() {
        return transport_id;
    }

    public Date getJourney_date() {
        return journey_date;
    }

    public int getDistance_miles() {
        return distance_miles;
    }

    public String getTransport_type() {
        return  transport_type;
    }
}
