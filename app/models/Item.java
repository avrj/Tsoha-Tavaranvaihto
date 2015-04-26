package models;

import models.customer.Customer;
import play.Logger;
import play.data.validation.Constraints;
import play.db.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by avrj on 27.3.2015.
 */
public class Item {
    private static Connection connection = DB.getConnection();

    public String vaihdossa;
    private Long item_id, customer_id, locked_customer_id, accepted_customer_id;

    @Constraints.Required
    @Constraints.MinLength(value = 3)
    public String title;

    @Constraints.Required
    @Constraints.MinLength(value = 3)
    public String description;

    @Constraints.Required
    public Long category_id;
    private java.sql.Timestamp timestamp, locked_at_timestamp, accepted_offer_at;

    public Item() {

    }

    public Item (String title, String description, String vaihdossa, Long category_id) {
        this.title = title;
        this.description = description;
        this.vaihdossa = vaihdossa;
        this.category_id = category_id;
    }

    public Item(Long customer_id, Long category_id, String title, String description, String vaihdossa) {
        this.customer_id = customer_id;
        this.category_id = category_id;
        this.title = title;
        this.description = description;
        this.vaihdossa = vaihdossa;
    }

    public Item (Long item_id, String title, String description, String vaihdossa, Long category_id, Long customer_id, java.sql.Timestamp created_at_timestamp, java.sql.Timestamp locked_at_timestamp, Long locked_customer_id, java.sql.Timestamp accepted_offer_at, Long accepted_customer_id) {
        this.title = title;
        this.description = description;
        this.vaihdossa = vaihdossa;
        this.category_id = category_id;
        this.customer_id = customer_id;
        this.timestamp = created_at_timestamp;
        this.item_id = item_id;
        this.locked_at_timestamp = locked_at_timestamp;
        this.locked_customer_id = locked_customer_id;
        this.accepted_offer_at = accepted_offer_at;
        this.accepted_customer_id = accepted_customer_id;
    }

    public Long getId() {
        return item_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getVaihdossa() {
        return vaihdossa;
    }

    public Long getCategoryId() {
        return category_id;
    }

    public Long getCustomerId() {
        return customer_id;
    }

    public java.sql.Timestamp getTimestamp() {
        return timestamp;
    }

    public Customer getOwner() {
        return Customer.getCustomerById(customer_id);
    }

    public Long getCounterOffersCount() {
        return CounterOffer.getCounterOffersCountForItem(item_id);
    }

    public CounterOffer getCounterOfferByCustomerId(Long customer_id) {
        return CounterOffer.getCounterOfferForItemByCustomerId(item_id, customer_id);
    }

    public java.sql.Timestamp getLockedAtTimestamp() {
        return locked_at_timestamp;
    }

    public java.sql.Timestamp getLockExpiresAtTimestamp() {
        Calendar cal = Calendar.getInstance();

        cal.setTime(locked_at_timestamp);
        cal.add(Calendar.DAY_OF_WEEK, 1);

        return new java.sql.Timestamp(cal.getTime().getTime());
    }

    public Long getLockedCustomerId() {
        return locked_customer_id;
    }

    public Customer getLockedCustomer() {
        return Customer.getCustomerById(locked_customer_id);
    }

    public java.sql.Timestamp getAcceptedOfferAtTimestamp() {
        return accepted_offer_at;
    }

    public Long getAcceptedOfferCustomerId() {
        return accepted_customer_id;
    }

    public java.sql.Timestamp getAcceptedOfferAt() {
        return accepted_offer_at;
    }

    public Customer getAcceptedOfferCustomer() {
        return Customer.getCustomerById(accepted_customer_id);
    }

    public static List<Item> getItems() {
        List<Item> items = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at, locked_at, locked_customer_id, accepted_offer_at, accepted_customer_id FROM Item ORDER BY created_at DESC;";

        try {
            statement = connection.prepareStatement(sql);

            result = statement.executeQuery();

            while (result.next()) {
                items.add(new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at"), result.getTimestamp("locked_at"), result.getLong("locked_customer_id"), result.getTimestamp("accepted_offer_at"), result.getLong("accepted_customer_id")));
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

        return items;
    }

    public static List<Item> getOpenItems() {
        List<Item> items = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at, locked_at, locked_customer_id, accepted_offer_at, accepted_customer_id FROM Item WHERE accepted_offer_at IS NULL ORDER BY created_at DESC;";

        try {
            statement = connection.prepareStatement(sql);

            result = statement.executeQuery();

            while (result.next()) {
                items.add(new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at"), result.getTimestamp("locked_at"), result.getLong("locked_customer_id"), result.getTimestamp("accepted_offer_at"), result.getLong("accepted_customer_id")));
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

        return items;
    }

    public static List<Item> getLatestItems(int count) {
        List<Item> items = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at, locked_at, locked_customer_id, accepted_offer_at, accepted_customer_id FROM Item WHERE accepted_offer_at IS NULL ORDER BY created_at DESC LIMIT " + count + ";";

        try {
            statement = connection.prepareStatement(sql);

            result = statement.executeQuery();

            while (result.next()) {
                items.add(new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at"), result.getTimestamp("locked_at"), result.getLong("locked_customer_id"), result.getTimestamp("accepted_offer_at"), result.getLong("accepted_customer_id")));
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

        return items;
    }

    public static List<Item> getMostPopularItems(int count) {
        List<Item> items = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at, locked_at, locked_customer_id, accepted_offer_at, accepted_customer_id FROM Item WHERE accepted_offer_at IS NULL ORDER BY created_at DESC LIMIT " + count + ";";

        try {
            statement = connection.prepareStatement(sql);

            result = statement.executeQuery();

            while (result.next()) {
                items.add(new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at"), result.getTimestamp("locked_at"), result.getLong("locked_customer_id"), result.getTimestamp("accepted_offer_at"), result.getLong("accepted_customer_id")));
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

        return items;
    }

    public static List<Item> getItemsByCustomerId(Long id) {
        List<Item> items = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at, locked_at, locked_customer_id, accepted_offer_at, accepted_customer_id FROM Item WHERE customer_id = ? ORDER BY created_at DESC;";

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, id);

            result = statement.executeQuery();

            while (result.next()) {
                items.add(new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at"), result.getTimestamp("locked_at"), result.getLong("locked_customer_id"), result.getTimestamp("accepted_offer_at"), result.getLong("accepted_customer_id")));
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

        return items;
    }

    public static List<Item> getOpenItemsByCustomerId(Long id) {
        List<Item> items = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at, locked_at, locked_customer_id, accepted_offer_at, accepted_customer_id FROM Item WHERE customer_id = ? AND accepted_offer_at IS NULL ORDER BY created_at DESC;";

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, id);

            result = statement.executeQuery();

            while (result.next()) {
                items.add(new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at"), result.getTimestamp("locked_at"), result.getLong("locked_customer_id"), result.getTimestamp("accepted_offer_at"), result.getLong("accepted_customer_id")));
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

        return items;
    }

    public static List<Item> getItemsOpenByCustomerId(Long id) {
        List<Item> items = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at, locked_at, locked_customer_id, accepted_offer_at, accepted_customer_id FROM Item WHERE customer_id = ? AND accepted_offer_at IS NULL ORDER BY created_at DESC;";

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, id);

            result = statement.executeQuery();

            while (result.next()) {
                items.add(new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at"), result.getTimestamp("locked_at"), result.getLong("locked_customer_id"), result.getTimestamp("accepted_offer_at"), result.getLong("accepted_customer_id")));
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

        return items;
    }

    public static List<Item> getItemsAcceptedByCustomerId(Long id) {
        List<Item> items = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at, locked_at, locked_customer_id, accepted_offer_at, accepted_customer_id FROM Item WHERE customer_id = ? AND accepted_offer_at IS NOT NULL ORDER BY created_at DESC;";

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, id);

            result = statement.executeQuery();

            while (result.next()) {
                items.add(new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at"), result.getTimestamp("locked_at"), result.getLong("locked_customer_id"), result.getTimestamp("accepted_offer_at"), result.getLong("accepted_customer_id")));
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

        return items;
    }


    public static List<Item> getItemsByCategoryId(Long id) {
        List<Item> items = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at, locked_at, locked_customer_id, accepted_offer_at, accepted_customer_id FROM Item WHERE category_id = ? ORDER BY created_at DESC;";

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, id);

            result = statement.executeQuery();

            while (result.next()) {
                items.add(new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at"), result.getTimestamp("locked_at"), result.getLong("locked_customer_id"), result.getTimestamp("accepted_offer_at"), result.getLong("accepted_customer_id")));
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

        return items;
    }

    public static List<Item> getOpenItemsByCategoryId(Long id) {
        List<Item> items = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at, locked_at, locked_customer_id, accepted_offer_at, accepted_customer_id FROM Item WHERE category_id = ? AND accepted_offer_at IS NULL ORDER BY created_at DESC;";

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, id);

            result = statement.executeQuery();

            while (result.next()) {
                items.add(new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at"), result.getTimestamp("locked_at"), result.getLong("locked_customer_id"), result.getTimestamp("accepted_offer_at"), result.getLong("accepted_customer_id")));
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

        return items;
    }

    public static Item getItemById(Long id) {
        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at, locked_at, locked_customer_id, accepted_offer_at, accepted_customer_id FROM Item WHERE id = ?;";

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, id);

            result = statement.executeQuery();

            if (result.next()) {
                return new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at"), result.getTimestamp("locked_at"), result.getLong("locked_customer_id"), result.getTimestamp("accepted_offer_at"), result.getLong("accepted_customer_id"));
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

    /*
        TODO: Olio parametrina erillisten kenttien sijaan
     */
    public boolean save() {
        String sql = "INSERT INTO Item (customer_id, category_id, title, description, vaihdossa, created_at) VALUES(?, ?, ?, ?, ?, ?);";

        PreparedStatement statement = null;

        int result = 0;

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, this.customer_id);
            statement.setLong(2, this.category_id);
            statement.setString(3, this.title);
            statement.setString(4, this.description);
            statement.setString(5, this.vaihdossa);

            java.util.Date date = new java.util.Date(System.currentTimeMillis());
            java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());

            statement.setTimestamp(6, timestamp);

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

    /*
        TODO: Olio parametrina erillisten kenttien sijaan
     */
    public static boolean updateItem(Long id, Long category_id, String title, String description, String vaihdossa) {
        /*
            TODO: edited_at
         */
        String sql = "UPDATE Item SET category_id = ?, title = ?, description = ?, vaihdossa = ? WHERE id = ?;";

        PreparedStatement statement = null;

        int result = 0;

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, category_id);
            statement.setString(2, title);
            statement.setString(3, description);
            statement.setString(4, vaihdossa);
            statement.setLong(5, id);

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

    public static boolean deleteItem(Long id) {
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

    public static List<Item> getItemsForCustomerCounterOffersByCustomerId(Long customer_id) {
        List<Item> items = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at, locked_at, locked_customer_id, accepted_offer_at, accepted_customer_id FROM Item WHERE id IN (SELECT item_id FROM CounterOffer WHERE customer_id = ?) ORDER BY created_at DESC;";

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, customer_id);

            result = statement.executeQuery();

            while (result.next()) {
                items.add(new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at"), result.getTimestamp("locked_at"), result.getLong("locked_customer_id"), result.getTimestamp("accepted_offer_at"), result.getLong("accepted_customer_id")));
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

        return items;
    }

    public static List<Item> getItemsOpenForCustomerCounterOffersByCustomerId(Long customer_id) {
        List<Item> items = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at, locked_at, locked_customer_id, accepted_offer_at, accepted_customer_id FROM Item WHERE id IN (SELECT item_id FROM CounterOffer WHERE customer_id = ?) ORDER BY created_at DESC;";

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, customer_id);

            result = statement.executeQuery();

            while (result.next()) {
                items.add(new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at"), result.getTimestamp("locked_at"), result.getLong("locked_customer_id"), result.getTimestamp("accepted_offer_at"), result.getLong("accepted_customer_id")));
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

        return items;
    }

    public static List<Item> getItemsAcceptedForCustomerCounterOffersByCustomerId(Long customer_id) {
        List<Item> items = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at, locked_at, locked_customer_id, accepted_offer_at, accepted_customer_id FROM Item WHERE id IN (SELECT item_id FROM CounterOffer WHERE customer_id = ?) AND accepted_customer_id = ? ORDER BY created_at DESC;";

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, customer_id);
            statement.setLong(2, customer_id);

            result = statement.executeQuery();

            while (result.next()) {
                items.add(new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at"), result.getTimestamp("locked_at"), result.getLong("locked_customer_id"), result.getTimestamp("accepted_offer_at"), result.getLong("accepted_customer_id")));
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

        return items;
    }

    public static List<Item> getItemsClosedForCustomerCounterOffersByCustomerId(Long customer_id) {
        List<Item> items = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at, locked_at, locked_customer_id, accepted_offer_at, accepted_customer_id FROM Item WHERE id IN (SELECT item_id FROM CounterOffer WHERE customer_id = ?) AND accepted_customer_id IS NOT NULL AND accepted_customer_id != ? AND locked_customer_id != ? ORDER BY created_at DESC;";

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, customer_id);
            statement.setLong(2, customer_id);
            statement.setLong(3, customer_id);

            result = statement.executeQuery();

            while (result.next()) {
                items.add(new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at"), result.getTimestamp("locked_at"), result.getLong("locked_customer_id"), result.getTimestamp("accepted_offer_at"), result.getLong("accepted_customer_id")));
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

        return items;
    }

    public static List<Item> getItemsForCustomerOffersByCustomerId(Long customer_id) {
        List<Item> items = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at, locked_at, locked_customer_id, accepted_offer_at, accepted_customer_id FROM Item WHERE locked_customer_id = ? ORDER BY created_at DESC;";

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, customer_id);

            result = statement.executeQuery();

            while (result.next()) {
                items.add(new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at"), result.getTimestamp("locked_at"), result.getLong("locked_customer_id"), result.getTimestamp("accepted_offer_at"), result.getLong("accepted_customer_id")));
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

        return items;
    }

    public static List<Item> getItemsOpenForCustomerOffersByCustomerId(Long customer_id) {
        List<Item> items = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at, locked_at, locked_customer_id, accepted_offer_at, accepted_customer_id FROM Item WHERE locked_customer_id = ? AND accepted_offer_at IS NULL ORDER BY created_at DESC;";

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, customer_id);

            result = statement.executeQuery();

            while (result.next()) {
                items.add(new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at"), result.getTimestamp("locked_at"), result.getLong("locked_customer_id"), result.getTimestamp("accepted_offer_at"), result.getLong("accepted_customer_id")));
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

        return items;
    }

    public static List<Item> getItemsAcceptedForCustomerOffersByCustomerId(Long customer_id) {
        List<Item> items = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, customer_id, category_id, title, description, vaihdossa, created_at, locked_at, locked_customer_id, accepted_offer_at, accepted_customer_id FROM Item WHERE locked_customer_id = ? AND accepted_customer_id = ? AND accepted_offer_at IS NOT NULL ORDER BY created_at DESC;";

        try {
            statement = connection.prepareStatement(sql);

            statement.setLong(1, customer_id);
            statement.setLong(2, customer_id);

            result = statement.executeQuery();

            while (result.next()) {
                items.add(new Item(result.getLong("id"), result.getString("title"), result.getString("description"), result.getString("vaihdossa"), result.getLong("category_id"), result.getLong("customer_id"), result.getTimestamp("created_at"), result.getTimestamp("locked_at"), result.getLong("locked_customer_id"), result.getTimestamp("accepted_offer_at"), result.getLong("accepted_customer_id")));
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

        return items;
    }

    public static boolean lockItem(Long id, Long customer_id) {
        String sql = "UPDATE Item SET locked_at = ?, locked_customer_id = ? WHERE id = ?;";

        PreparedStatement statement = null;

        int result = 0;

        try {
            statement = connection.prepareStatement(sql);

            java.util.Date date = new java.util.Date(System.currentTimeMillis());
            java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());

            statement.setTimestamp(1, timestamp);
            statement.setLong(2, customer_id);
            statement.setLong(3, id);

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

    public static boolean unlockItem(Long id) {
        String sql = "UPDATE Item SET locked_at = NULL, locked_customer_id = NULL WHERE id = ?;";

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

    public static int deleteExpiredOffers() {
        /*
        String sql = "UPDATE Item SET locked_at = NULL, locked_customer_id = null where accepted_offer_at IS NULL and current_timestamp > created_at+interval 1 day;";

        PreparedStatement statement = null;

        int result = 0;

        try {
            statement = connection.prepareStatement(sql);

            result = statement.executeUpdate();
        } catch (Exception e) {
            Logger.error(e.toString());
        }

        try {
            statement.close();
        } catch (SQLException e) {
            Logger.error(e.toString());
        }

        return result;
        */
        return 0;
    }

    public static boolean acceptOffer(Long id) {
        String sql = "UPDATE Item SET accepted_offer_at = ?, accepted_customer_id = locked_customer_id WHERE id = ?;";

        PreparedStatement statement = null;

        int result = 0;

        try {
            statement = connection.prepareStatement(sql);

            java.util.Date date = new java.util.Date(System.currentTimeMillis());
            java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());

            statement.setTimestamp(1, timestamp);

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
