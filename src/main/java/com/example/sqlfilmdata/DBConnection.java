package com.example.sqlfilmdata;
import java.sql.*;

public class DBConnection {

    public Connection databaseLink;
        public Connection getConnection(String username, String password) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection("jdbc:mysql://localhost:3306/",username,password);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return databaseLink;
    }

}
