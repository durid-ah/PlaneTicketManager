package com.planeTicketManager.repository;

import com.planeTicketManager.helpers.StringHelper;
import com.planeTicketManager.models.FlightCriteria;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.sql.PooledConnection;
import java.io.IOException;
import java.sql.*;
import java.util.Calendar;

public class FlightRepo {

    PooledConnection pconn;
    Connection conn;
    PreparedStatement pstmt;
    ResultSet rs;

    public FlightRepo() {

    }

    //Get available flights and their details based on the criteria properties
    public JSONArray getAvailableFlights (FlightCriteria criteria) throws SQLException, IOException {
        JSONArray json = new JSONArray();

        Calendar arrivalDate = Calendar.getInstance();
        arrivalDate.setTime(criteria.getArrivalTime());
        int arrivalDay = arrivalDate.get(Calendar.DAY_OF_MONTH);
        int arrivalMonth = arrivalDate.get(Calendar.MONTH);

        Calendar departureDate = Calendar.getInstance();
        departureDate.setTime(criteria.getDepartureTime());
        int departureDay = departureDate.get(Calendar.DAY_OF_MONTH);
        int departureMonth = departureDate.get(Calendar.MONTH);

        String query =  "SELECT  flight.flight_id," +
                                "flight.ARR_TIME," +
                                "flight.DEP_TIME," +
                                "arrival.airport_code AS arr_airport_code," +
                                "arrival.airport_name AS arr_airport," +
                                "departure.airport_code AS dep_airport_code," +
                                "departure.airport_name AS dep_airport, " +
                                "fs.first_class_seats," +
                                "fs.first_class_booked," +
                                "fs.coach_seats," +
                                "fs.coach_booked " +
                        "FROM flight " +
                        "INNER JOIN flight_stats AS fs ON flight.flight_id = fs.flight_id " +
                        "INNER JOIN airport_codes AS arrival " +
                            "ON flight.arr_airport_id = arrival.airport_id " +
                        "INNER JOIN airport_codes AS departure " +
                            "ON flight.dep_airport_id = departure.airport_id " +
                        "WHERE (fs.first_class_booked < fs.first_class_seats " +
                            "OR fs.coach_booked < fs.coach_seats)"+
                            "AND DAY(flight.arr_time) = ? " +
                            "AND MONTH(flight.arr_time) = ? " +
                            "AND DAY(flight.dep_time) = ? " +
                            "AND MONTH(flight.dep_time) = ? " ;


        String arrivalAirport = criteria.getArrivalAirport();
        String departureAirport = criteria.getDepartureAirport();

        if (!StringHelper.isNullOrEmpty(arrivalAirport)) {
            query += "AND arrival.airport_name = ? ";
        }else if (!StringHelper.isNullOrEmpty(departureAirport)) {
            query += "AND departure.airport_name = ? ";
        }
        try {
            pconn = DataSourceSingleton.getPooledConnection();

            conn = pconn.getConnection();
            pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, arrivalDay);
            pstmt.setInt(2, arrivalMonth);
            pstmt.setInt(3, departureDay);
            pstmt.setInt(4, departureMonth);

            int parameterIndex = 5;

            //Add these parameters if they are not null or empty
            if (!StringHelper.isNullOrEmpty(arrivalAirport)) {
                pstmt.setString(parameterIndex++, arrivalAirport);
            } else if (!StringHelper.isNullOrEmpty(departureAirport)) {
                pstmt.setString(parameterIndex++, departureAirport);
            }

            rs = pstmt.executeQuery();

            //Store the queries into an array of JSON objects
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("id", rs.getInt(1));
                obj.put("arrivalTime", rs.getDate(2));
                obj.put("departureTime", rs.getDate(3));
                obj.put("arrAirportCode", rs.getString(4));
                obj.put("arrAirport", rs.getString(5));
                obj.put("depAirportCode", rs.getString(6));
                obj.put("depAirport", rs.getString(7));
                obj.put("firstClassSeats", rs.getInt(8));
                obj.put("firstClassBooked", rs.getInt(9));
                obj.put("coachSeats", rs.getInt(10));
                obj.put("coachBooked", rs.getInt(11));
                json.put(obj);
            }

        } catch (Exception e) {
           JSONObject obj = new JSONObject();
           obj.put("err", e.getMessage());
           json.put(obj);
        } finally {
            rs.close();
            pstmt.close();
            conn.close();
            pconn.close();

        }

        return json;
    }

}
