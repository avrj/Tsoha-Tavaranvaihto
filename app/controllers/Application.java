package controllers;

import play.*;
import play.db.DB;
import play.mvc.*;

import views.html.*;

import models.*;

import java.sql.*;

public class Application extends Controller {
    private static Connection connection = DB.getConnection();

    public static Result index() {
        System.out.println("Opened database successfully");

        PreparedStatement kysely = null;
        ResultSet tulokset = null;
        int tulos = 0;

        try {
            //Alustetaan muuttuja jossa on Select-kysely, joka palauttaa lukuarvon:
            String sqlkysely = "SELECT COUNT(*) FROM Users as two";

            kysely = connection.prepareStatement(sqlkysely);
            tulokset = kysely.executeQuery();
            if(tulokset.next()) {
                //Tuloksen arvoksi pitäisi tulla numero kaksi.
                tulos = tulokset.getInt("two");
            } else {
                //out.println("Virhe!");
            }
        } catch (Exception e) {
            //out.println("Virhe: " + e.getMessage()");
        }

        try {
            tulokset.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            kysely.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Testi testi = new Testi();
        testi.setTesti(5);

        return ok(index.render(testi.getTesti() + "Your new application is ready." + tulos));
    }

}
