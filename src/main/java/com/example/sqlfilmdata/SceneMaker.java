package com.example.sqlfilmdata;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneMaker {

        //public Stage stage= new Stage();
       /* public void  sceneMaker(String fxmlName, int v1, int v2, String seTitle, Stage stage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(SQLApplication.class.getResource(fxmlName));
            Scene scene = new Scene(fxmlLoader.load(), v1, v2);
            stage.setTitle(seTitle);
            stage.setScene(scene);
            stage.show();

        }

        public void sceneInformation(String seTitle,Stage stage) throws IOException {

            Pane pane=new Pane();
            Label label= new Label();
            Button button=new Button("Deneme");
            pane.getChildren().add(button);
            pane.getChildren().add(label);
            StackPane root = new StackPane();
            root.getChildren().add(pane);
            root.getChildren().add(button);
            stage.setTitle(seTitle);
            stage.setScene(new Scene(root, 300, 250));
            button.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    System.out.println("Hello World!");
                    label.setText(" merhaba dünya ");
                }
            });


            stage.show();

        }*/
    }


