package com.example.sqlfilmdata;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SQLApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SQLApplication.class.getResource("SQLFilmData.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1209, 750);
        stage.setTitle("Data Controller");
        stage.setScene(scene);
        stage.show();


    }

    public static void main() {
        launch();
    }
}