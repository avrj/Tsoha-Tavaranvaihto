package models;

import play.db.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by avrj on 22.3.2015.
 */
public class Customers {
    private static Connection connection = DB.getConnection();

    public ArrayList<Customer> getCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        int tulos = 0;
        String sql = "SELECT id, username, email FROM Customer";
        try {
            statement = connection.prepareStatement(sql);

            result = statement.executeQuery();

            if (result.next()) {
                customers.add(new Customer(result.getString("username"), result.getString("email")));
            }

        } catch (Exception e) {
        }

        try {
            result.close();
        } catch (SQLException e) {
        }

        try {
            result.close();
        } catch (SQLException e) {
        }

        return customers;
    }

    public int createCustomer(String username, String email, String password) {
        String sql = "INSERT INTO Customer (username, email, password) VALUES(?,?,?)";

        PreparedStatement statement = null;

        int result = 0;

        try {
            statement = connection.prepareStatement(sql);

            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, password);

            result = statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }
}
