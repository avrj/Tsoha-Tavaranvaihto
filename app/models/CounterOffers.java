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
public class CounterOffers {
    private static Connection connection = DB.getConnection();

    public List<CounterOffer> getCounterOffersForItem(Long item_id) {
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

        } catch (Exception e) {  }

        try { statement.close(); } catch (SQLException e) {  }

        try { result.close(); } catch (SQLException e) {  }

        return counterOffers;
    }

    public Long getCounterOffersCountForItem(Long item_id) {
        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT COUNT(*) FROM CounterOffer WHERE item_id = ?;";
        try {
            statement = connection.prepareStatement(sql);
            statement.setLong(1, item_id);

            result = statement.executeQuery();

            if (result.next()) {
                return result.getLong("COUNT(*)");
            }

        } catch (Exception e) {  }

        try { statement.close(); } catch (SQLException e) {  }

        try { result.close(); } catch (SQLException e) {  }

        return 0L;
    }

    public int createCounterOffer(Long customer_id, Long item_id, String description) {
        String sql = "INSERT INTO CounterOffer (customer_id, item_id, description) VALUES(?, ?, ?);";

        PreparedStatement statement = null;

        int result = 0;

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, customer_id);
            statement.setLong(2, item_id);
            statement.setString(3, description);

            result = statement.executeUpdate();
        } catch (Exception e) { Logger.error(e.toString()); }

        try { statement.close(); } catch (SQLException e) { Logger.error(e.toString()); }

        return result;

    }

    public CounterOffer getCounterOfferForItemByCustomerId(Long item_id, Long customer_id) {
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


        } catch (Exception e) { Logger.error(e.toString()); }

        try { statement.close(); } catch (SQLException e) { Logger.error(e.toString());  }

        try { result.close(); } catch (SQLException e) { Logger.error(e.toString()); }

        return null;
    }

    public int deleteCounterOffer(Long item_id, Long customer_id) {

        String sql = "DELETE FROM CounterOffer WHERE item_id = ? AND customer_id = ?;";

        PreparedStatement statement = null;

        int result = 0;

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, item_id);
            statement.setLong(2, customer_id);

            result = statement.executeUpdate();
        } catch (Exception e) { Logger.error(e.toString()); }

        try { statement.close(); } catch (SQLException e) { Logger.error(e.toString()); }

        return result;
    }

    public List<CounterOffer> getCounterOffersByCustomerId(Long customer_id) {
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

        } catch (Exception e) {  }

        try { statement.close(); } catch (SQLException e) {  }

        try { result.close(); } catch (SQLException e) {  }

        return counterOffers;
    }
}
