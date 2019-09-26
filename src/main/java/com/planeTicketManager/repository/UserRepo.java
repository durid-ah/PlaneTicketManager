package com.planeTicketManager.repository;

import com.planeTicketManager.helpers.StringHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.sql.PooledConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepo {

    PooledConnection pconn;
    Connection conn;
    PreparedStatement pstmt;
    ResultSet rs;


    public JSONArray createUser(User newUder) {
        // TODO: Implement this query
//        INSERT INTO users(first_name, last_name, phone_number, email)
//        VALUES (
//                'Durid',
//                'Ahmad',
//                2087018678,
//                'ahmad.durid96@gmail.com');
    }

    public JSONArray getUserFlights (int userId) throws IOException, SQLException {
        JSONArray jsonArray = new JSONArray();


        String query = "SELECT   first_name," +
                "last_name" +
                "flight.ARR_TIME," +
                "flight.DEP_TIME," +
                "arrival.airport_code AS arr_airport_code," +
                "arrival.airport_name AS arr_airport," +
                "departure.airport_code AS dep_airport_code," +
                "departure.airport_name AS dep_airport," +
                "flight.flight_id," +
                "FROM users " +
                "INNER JOIN flight_manifesto AS manifesto" +
                "ON users.user_id = manifesto.user_id" +
                "INNER JOIN flight" +
                "ON manifesto.flight_id = flight.flight_id" +
                "INNER JOIN airport_codes AS arrival" +
                "ON flight.arr_airport_id = arrival.airport_id" +
                "INNER JOIN airport_codes AS departure" +
                "ON flight.dep_airport_id = departure.airport_id " +
                "WHERE user_id = ?";

        try {
            pconn = DataSourceSingleton.getPooledConnection();
            conn = pconn.getConnection();
            pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, userId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("firstName", rs.getString(1));
                obj.put("lastName", rs.getString(2));
                obj.put("arrivalTime", rs.getDate(3));
                obj.put("departureTime", rs.getDate(4));
                obj.put("arrAirportCode", rs.getString(5));
                obj.put("arrAirport", rs.getString(6));
                obj.put("depAirportCode", rs.getString(7));
                obj.put("depAirport", rs.getString(8));
                obj.put("flightId", rs.getInt(9));
                jsonArray.put(obj);
            }

        } catch (Exception e) {
            JSONObject obj = new JSONObject();
            obj.put("err", e.getMessage());
            jsonArray.put(obj);
        } finally {
            rs.close();
            pstmt.close();
            conn.close();
            pconn.close();
        }

        return jsonArray;
    }
}
