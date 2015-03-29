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
 * Created by avrj on 27.3.2015.
 */
public class Categories {
    private static Connection connection = DB.getConnection();

    public List<Category> getCategories() {
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

        } catch (Exception e) {  }

        try { statement.close(); } catch (SQLException e) {  }

        try { result.close(); } catch (SQLException e) {  }

        return categories;
    }

    public Long getItemsCount(Long id) {
        PreparedStatement statement = null;

        ResultSet result = null;

        String sql = "SELECT COUNT(id) FROM Item WHERE category_id = ?;";
        try {
            statement = connection.prepareStatement(sql);
            statement.setLong(1, id);

            result = statement.executeQuery();

            if (result.next()) {
                return result.getLong("COUNT(id)");
            }

        } catch (Exception e) {  }

        try { statement.close(); } catch (SQLException e) {  }

        try { result.close(); } catch (SQLException e) {  }

        return null;
    }

    public Category getCategoryById(Long id) {
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

        } catch (Exception e) {  }

        try { statement.close(); } catch (SQLException e) {  }

        try { result.close(); } catch (SQLException e) {  }

        return null;
    }

    public List getCategoriesAsScalaTupleList() {
        List category_items = new ArrayList<>();

        int i = 1;
        /*
            TODO: i -> id kannasta
         */
        for(Category category : getCategories()) {
            category_items.add(play.libs.Scala.Tuple(Integer.toString(i), category.getTitle()));
            i++;
        }

        return category_items;
    }

    /*
        TODO: Olio parametrina erillisten kenttien sijaan
     */
    public int createCategory(String title) {
        String sql = "INSERT INTO Category (title) VALUES(?);";

        PreparedStatement statement = null;

        int result = 0;

        try {
            statement = connection.prepareStatement(sql);

            statement.setString(1, title);

            result = statement.executeUpdate();
        } catch (Exception e) { Logger.error(e.toString()); }

        try { statement.close(); } catch (SQLException e) { Logger.error(e.toString()); }

        return result;

    }
}
