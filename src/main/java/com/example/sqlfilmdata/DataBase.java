package com.example.sqlfilmdata;
import javafx.scene.control.Alert;

import java.sql.*;


public class DataBase {
    Connection connection = null;
    DBConnection dbConnection = null;
    FileMakeReadWrite file;
/*
    public Connection connectDB() {

        try {
            connection = dbConnection.getConnection(file.getSettingsData().get(0),
                    file.getSettingsData().get(1),
                    file.getSettingsData().get(2));
            Statement statement = connection.createStatement();
            return connection;
        } catch (Exception e) {
            System.out.println();
            return null;

        }

    }*/

}
