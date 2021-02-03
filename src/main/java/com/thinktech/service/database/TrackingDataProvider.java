package com.thinktech.service.database;

import com.thinktech.model.domain.DataForTrackingPage;

public class TrackingDataProvider extends CarbonDataProvider {
    public void AddJourney(DataForTrackingPage journey) throws Exception {
        try {
            connection = this.OpenConnection();

            callableStatement = connection.prepareCall("{CALL ADD_JOURNEY(?, ?, ?, ?, ?)}");
            callableStatement.setString("p_auth_user_id", journey.getAuthUserId());
            callableStatement.setInt("p_transport_id", journey.getTrackingItemId());
            callableStatement.setDate("p_journey_date", java.sql.Date.valueOf(journey.getTrackingDate()));
            callableStatement.setInt("p_distance_miles", journey.getDistance());
            callableStatement.setInt("p_journey_id", 0);

            callableStatement.executeUpdate();

            journey.setIdJourney(callableStatement.getInt("p_journey_id"));
            //System.out.println(journey);

        } catch (Exception e) {
            String message = String.format("Unable to add user to database %s", journey.getAuthUserId());
            throw new Exception(message, e);
        } finally {
            CloseConnection();
        }
    }
    public void UpdateJourney(DataForTrackingPage journey) throws Exception {
        try {
            connection = this.OpenConnection();

            callableStatement = connection.prepareCall("{CALL UPDATE_JOURNEY(?, ?)}");
            callableStatement.setInt("p_distance_miles", journey.getDistance());
            callableStatement.setInt("p_journey_id", journey.getIdJourney());
            System.out.println(callableStatement);
            callableStatement.executeUpdate();

            //System.out.println(journey);

        } catch (Exception e) {
            String message = String.format("Unable to add user to database %s", journey.getAuthUserId());
            throw new Exception(message, e);
        } finally {
            CloseConnection();
        }
    }

}
