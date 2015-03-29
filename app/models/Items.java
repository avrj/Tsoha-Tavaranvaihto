package models;

import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.db.DB;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by avrj on 27.3.2015.
 */
public class Items {
    private static Connection connection = DB.getConnection();

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at FROM Item;";

        try {
            statement = connection.prepareStatement(sql);

            result = statement.executeQuery();

            while (result.next()) {
                items.add(new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at")));
            }


        } catch (Exception e) { Logger.error(e.toString()); }

        try { statement.close(); } catch (SQLException e) { Logger.error(e.toString());  }

        try { result.close(); } catch (SQLException e) { Logger.error(e.toString()); }

        return items;
    }

    public List<Item> getLatestItems(int count) {
        List<Item> items = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at FROM Item ORDER BY created_at DESC LIMIT " + count + ";";

        try {
            statement = connection.prepareStatement(sql);

            result = statement.executeQuery();

            while (result.next()) {
                items.add(new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at")));
            }


        } catch (Exception e) { Logger.error(e.toString()); }

        try { statement.close(); } catch (SQLException e) { Logger.error(e.toString());  }

        try { result.close(); } catch (SQLException e) { Logger.error(e.toString()); }

        return items;
    }

    public List<Item> getItemsByCustomerId(Long id) {
        List<Item> items = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at FROM Item WHERE customer_id = ?;";

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, id);

            result = statement.executeQuery();

            while (result.next()) {
                items.add(new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at")));
            }


        } catch (Exception e) { Logger.error(e.toString()); }

        try { statement.close(); } catch (SQLException e) { Logger.error(e.toString());  }

        try { result.close(); } catch (SQLException e) { Logger.error(e.toString()); }

        return items;
    }

    public Item getItemById(Long id) {
        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at FROM Item WHERE id = ?;";

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, id);

            result = statement.executeQuery();

            if (result.next()) {
                return new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at"));
            }


        } catch (Exception e) { Logger.error(e.toString()); }

        try { statement.close(); } catch (SQLException e) { Logger.error(e.toString());  }

        try { result.close(); } catch (SQLException e) { Logger.error(e.toString()); }

        return null;
    }

    /*
        TODO: Olio parametrina erillisten kenttien sijaan
     */
    public int createItem(int customer_id, int category_id, String title, String description, String vaihdossa) {
        String sql = "INSERT INTO Item (customer_id, category_id, title, description, vaihdossa, created_at) VALUES(?, ?, ?, ?, ?, ?);";

        PreparedStatement statement = null;

        int result = 0;

        try {
            statement = connection.prepareStatement(sql);

            statement.setInt(1, customer_id);
            statement.setInt(2, category_id);
            statement.setString(3, title);
            statement.setString(4, description);
            statement.setString(5, vaihdossa);

            java.util.Date date = new java.util.Date(System.currentTimeMillis());
            java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());

            statement.setTimestamp(6, timestamp);

            result = statement.executeUpdate();
        } catch (Exception e) { Logger.error(e.toString()); }

        try { statement.close(); } catch (SQLException e) { Logger.error(e.toString()); }

        return result;

    }

    /*
        TODO: Olio parametrina erillisten kenttien sijaan
     */
    public int updateItem(Long id, int category, String title, String description, String vaihdossa) {
        /*
            TODO: edited_at
         */
        String sql = "UPDATE Item SET category_id = ?, title = ?, description = ?, vaihdossa = ? WHERE id = ?;";

        PreparedStatement statement = null;

        int result = 0;

        try {
            statement = connection.prepareStatement(sql);

            statement.setInt(1, category);
            statement.setString(2, title);
            statement.setString(3, description);
            statement.setString(4, vaihdossa);
            statement.setLong(5, id);

            result = statement.executeUpdate();
        } catch (Exception e) { Logger.error(e.toString()); }

        try { statement.close(); } catch (SQLException e) { Logger.error(e.toString()); }

        return result;
    }

    public int deleteItem(Long id) {
        /*
            TODO: vastatarjouksien poisto yms.
         */

        String sql = "DELETE FROM Item WHERE id = ?;";

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
}
