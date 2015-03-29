package models;

import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
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

        String sql = "SELECT id, username, email FROM Customer;";
        try {
            statement = connection.prepareStatement(sql);

            result = statement.executeQuery();

            while (result.next()) {
                customers.add(new Customer(result.getString("username"), result.getString("email")));
            }

        } catch (Exception e) {  }

        try { statement.close(); } catch (SQLException e) {  }

        try { result.close(); } catch (SQLException e) {  }

        return customers;
    }

    public Customer getCustomerById(Long id) {
        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, username, email FROM Customer WHERE id = ?;";
        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, id);

            result = statement.executeQuery();

            if (result.next()) {
                return new Customer(result.getString("username"), result.getString("email"));
            }

        } catch (Exception e) {  }

        try { statement.close(); } catch (SQLException e) {  }

        try { result.close(); } catch (SQLException e) {  }

        return null;
    }

    /*
        TODO: Olio parametrina erillisten kenttien sijaan
    */
    public int createCustomer(String username, String email, String password) {
        String sql = "INSERT INTO Customer (username, email, password) VALUES(?, ?, ?);";

        PreparedStatement statement = null;

        int result = 0;

        try {
            statement = connection.prepareStatement(sql);

            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, BCrypt.hashpw(password, BCrypt.gensalt()));

            result = statement.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }

        try { statement.close(); } catch (SQLException e) {  }

        return result;

    }

    public int deleteCustomer(Long id) {
        String sql = "DELETE FROM Customer WHERE id = ?;";

        PreparedStatement statement = null;

        int result = 0;

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, id);

            result = statement.executeUpdate();
        } catch (Exception e) { Logger.error(e.toString()); }

        try { statement.close(); } catch (SQLException e) { Logger.error(e.toString()); }

        return result;
    }

    public int authenticate(String username, String password) {

        PreparedStatement statement = null;

        ResultSet result = null;

        int tulos = 0;
        String sql = "SELECT id, username, password FROM Customer WHERE username = ?";
        try {
            statement = connection.prepareStatement(sql);

            statement.setString(1, username);

            result = statement.executeQuery();

            if (result.next()) {
                if(result.getString("username").equals(username) && BCrypt.checkpw(password, result.getString("password"))) {
                    return result.getInt("id");
                }
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

        return 0;
    }

    public int changePassword(Long id, String password) {
        String sql = "UPDATE Customer SET password = ? WHERE id = ?;";

        PreparedStatement statement = null;

        int result = 0;

        try {
            statement = connection.prepareStatement(sql);

            statement.setString(1, BCrypt.hashpw(password, BCrypt.gensalt()));
            statement.setLong(2, id);

            result = statement.executeUpdate();
        } catch (Exception e) { Logger.error(e.toString()); }

        try { statement.close(); } catch (SQLException e) { Logger.error(e.toString()); }

        return result;
    }
}
