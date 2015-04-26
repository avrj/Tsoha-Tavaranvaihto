package models;

import play.Logger;
import play.data.validation.Constraints;
import play.db.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by avrj on 27.3.2015.
 */
public class Category {
    private static Connection connection = DB.getConnection();

    @Constraints.Required
    @Constraints.MinLength(value = 3)
    public String title;
    private Long id;

    public Category(Long id, String title) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public static List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();

        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, title FROM Category;";
        try {
            statement = connection.prepareStatement(sql);

            result = statement.executeQuery();

            while (result.next()) {
                categories.add(new Category(result.getLong("id"), result.getString("title")));
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

        return categories;
    }

    public static Long getItemsCount(Long id) {
        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT COUNT(id) FROM Item WHERE category_id = ?;";
        try {
            statement = connection.prepareStatement(sql);
            statement.setLong(1, id);

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

    public static Category getCategoryById(Long id) {
        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT id, title FROM Category WHERE id = ?;";
        try {
            statement = connection.prepareStatement(sql);
            statement.setLong(1, id);

            result = statement.executeQuery();

            if (result.next()) {
                return new Category(result.getLong("id"), result.getString("title"));
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

    public static List getCategoriesAsScalaTupleList() {
        List category_items = new ArrayList<>();

        int i = 1;
        /*
            TODO: i -> id kannasta
         */
        for (Category category : getCategories()) {
            category_items.add(play.libs.Scala.Tuple(Integer.toString(i), category.getTitle()));
            i++;
        }

        return category_items;
    }

    /*
        TODO: Olio parametrina erillisten kenttien sijaan
     */
    public boolean save() {
        String sql = "INSERT INTO Category (title) VALUES(?);";

        PreparedStatement statement = null;

        int result = 0;

        try {
            statement = connection.prepareStatement(sql);

            statement.setString(1, this.title);

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
