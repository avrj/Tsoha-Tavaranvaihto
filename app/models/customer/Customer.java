package models.customer;

import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.db.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by avrj on 9.3.2015.
 */
public class Customer {
    private static Connection connection = DB.getConnection();

    private String username, email, password;

    public Customer(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Customer(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public static ArrayList<Customer> getCustomers() {
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

        } catch (Exception e) {
        }

        try {
            statement.close();
        } catch (SQLException e) {
        }

        try {
            result.close();
        } catch (SQLException e) {
        }

        return customers;
    }

    public static Customer getCustomerById(Long id) {
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

        } catch (Exception e) {
        }

        try {
            statement.close();
        } catch (SQLException e) {
        }

        try {
            result.close();
        } catch (SQLException e) {
        }

        return null;
    }

    public static Long getLockedItemsCountByCustomerId(Long customer_id) {
        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT COUNT(id) FROM Item WHERE customer_id = ? AND locked_customer_id IS NOT NULL;";
        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, customer_id);

            result = statement.executeQuery();

            if (result.next()) {
                return result.getLong(1);
            }

        } catch (Exception e) {
        }

        try {
            statement.close();
        } catch (SQLException e) {
        }

        try {
            result.close();
        } catch (SQLException e) {
        }

        return 0L;
    }

    /*
        TODO: Olio parametrina erillisten kenttien sijaan
    */
    public boolean save() {
        String sql = "INSERT INTO Customer (username, email, password) VALUES(?, ?, ?);";

        PreparedStatement statement = null;

        int result = 0;

        try {
            statement = connection.prepareStatement(sql);

            statement.setString(1, this.username);
            statement.setString(2, this.email);
            statement.setString(3, BCrypt.hashpw(this.password, BCrypt.gensalt()));

            result = statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            statement.close();
        } catch (SQLException e) {
        }

        if(result > 0)
            return true;
        else
            return false;

    }

    public static boolean deleteCustomer(Long id) {
        String sql = "DELETE FROM Customer WHERE id = ?;";

        PreparedStatement statement = null;

        int result = 0;

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, id);

            result = statement.executeUpdate();
        } catch (Exception e) {
            Logger.error(e.toString());
        }

        try {
            statement.close();
        } catch (SQLException e) {
            Logger.error(e.toString());
        }

        if(result > 0)
            return true;
        else
            return false;
    }

    public static int authenticate(String username, String password) {

        PreparedStatement statement = null;

        ResultSet result = null;

        int tulos = 0;
        String sql = "SELECT id, username, password FROM Customer WHERE username = ?";
        try {
            statement = connection.prepareStatement(sql);

            statement.setString(1, username);

            result = statement.executeQuery();

            if (result.next()) {
                if (result.getString("username").equals(username) && BCrypt.checkpw(password, result.getString("password"))) {
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

    public static boolean changePassword(Long id, String password) {
        String sql = "UPDATE Customer SET password = ? WHERE id = ?;";

        PreparedStatement statement = null;

        int result = 0;

        try {
            statement = connection.prepareStatement(sql);

            statement.setString(1, BCrypt.hashpw(password, BCrypt.gensalt()));
            statement.setLong(2, id);

            result = statement.executeUpdate();
        } catch (Exception e) {
            Logger.error(e.toString());
        }

        try {
            statement.close();
        } catch (SQLException e) {
            Logger.error(e.toString());
        }

        if(result > 0)
            return true;
        else
            return false;
    }
}
