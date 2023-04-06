package com.example.sqlfilmdata;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.List;
import java.util.Optional;

public class FileAlertC {
    private Alert alert;

    public void alertWarning(String text1, String text2, String text3) {
        alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(text1);
        alert.setHeaderText(text2);
        alert.setContentText(text3);
        alert.showAndWait();
    }
    public void alertError(String title, String dialog, String message){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(dialog);
        alert.setContentText(message);;

        alert.showAndWait();

    }
    public boolean alertConfirmation(String title, String dialog, String message){
        boolean ok=false;
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(dialog);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            ok=true;
        }
        return ok;
    }
    public void alertInformation(String title, String dialog, String message){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(dialog);
        alert.setContentText(message);

        alert.showAndWait();

    }
    public void alertInformation2(String title, List<String> list,String dialog){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(dialog);
        String message= makeListString(list);
        alert.setContentText(message);

        alert.showAndWait();

    }
    public String makeListString(List<String> list){
        String text="";
        for(int i=0;i< list.size();i++){
            text+=list.get(i)+"...\n";
        }
        return text;
    }



}
