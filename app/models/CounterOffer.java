package models;

import play.Logger;
import play.db.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by avrj on 9.4.2015.
 */
public class CounterOffer {
    private static Connection connection = DB.getConnection();

    private Long customer_id, item_id;
    private String description;

    public CounterOffer(Long customer_id, Long item_id, String description) {
        this.customer_id = customer_id;
        this.item_id = item_id;
        this.description = description;
    }

    public Long getCustomerId() {
        return customer_id;
    }

    public Customer getCustomer() {
        return new Customers().getCustomerById(customer_id);
    }

    public Long getItemId() {
        return item_id;
    }

    public String getDescription() {
        return description;
    }

    public static List<CounterOffer> getCounterOffersForItem(Long item_id) {
        List<CounterOffer> counterOffers = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT customer_id, description FROM CounterOffer WHERE item_id = ?;";
        try {
            statement = connection.prepareStatement(sql);
            statement.setLong(1, item_id);

            result = statement.executeQuery();

            while (result.next()) {
                counterOffers.add(new CounterOffer(result.getLong("customer_id"), item_id, result.getString("description")));
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

        return counterOffers;
    }

    public static Long getCounterOffersCountForItem(Long item_id) {
        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT COUNT(*) FROM CounterOffer WHERE item_id = ?;";
        try {
            statement = connection.prepareStatement(sql);
            statement.setLong(1, item_id);

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

    public boolean save() {
        String sql = "INSERT INTO CounterOffer (customer_id, item_id, description) VALUES(?, ?, ?);";

        PreparedStatement statement = null;

        int result = 0;

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, this.customer_id);
            statement.setLong(2, this.item_id);
            statement.setString(3, this.description);

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

    public static CounterOffer getCounterOfferForItemByCustomerId(Long item_id, Long customer_id) {
        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT description FROM CounterOffer WHERE item_id = ? AND customer_id = ?;";

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, item_id);
            statement.setLong(2, customer_id);

            result = statement.executeQuery();

            if (result.next()) {
                return new CounterOffer(customer_id, item_id, result.getString("description"));
            }


        } catch (Exception e) {
            Logger.error(e.toString());
        }

        try {
            statement.close();
        } catch (SQLException e) {
            Logger.error(e.toString());
        }

        try {
            result.close();
        } catch (SQLException e) {
            Logger.error(e.toString());
        }

        return null;
    }

    public static boolean deleteCounterOffer(Long item_id, Long customer_id) {

        String sql = "DELETE FROM CounterOffer WHERE item_id = ? AND customer_id = ?;";

        PreparedStatement statement = null;

        int result = 0;

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, item_id);
            statement.setLong(2, customer_id);

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

    public static List<CounterOffer> getCounterOffersByCustomerId(Long customer_id) {
        List<CounterOffer> counterOffers = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT item_id, description FROM CounterOffer WHERE customer_id = ?;";
        try {
            statement = connection.prepareStatement(sql);
            statement.setLong(1, customer_id);

            result = statement.executeQuery();

            while (result.next()) {
                counterOffers.add(new CounterOffer(customer_id, result.getLong("item_id"), result.getString("description")));
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

        return counterOffers;
    }
}
