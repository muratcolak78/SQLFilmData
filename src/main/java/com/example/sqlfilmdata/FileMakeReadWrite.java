package com.example.sqlfilmdata;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileMakeReadWrite {
    public List<String> userAndpass = new ArrayList<>();
    private FileAlertC fileAlert = new FileAlertC();
    private File file, folder;
    public FileWriter fileWriter;


    private List<String> settingsData = new ArrayList<>();
    public  FileMakeReadWrite() throws IOException {
        this.file=makeFile();
    }



    @FXML
    public void saveSettings(String userName, String password) throws IOException {
        fileWriter = new FileWriter(this.file, false);
        if (userName.isEmpty() || password.isEmpty()) {
            fileAlert.alertError("Settings.txt", "Başarısız", "Kullanıcı bilgileri kaydedilemedi!");

        } else {
            fileWriter.write(userName + "\n");
            fileWriter.write(password + "\n");

            settingsData.add(userName);
            settingsData.add(password);

            fileAlert.alertInformation("Settings.txt", "Başarılı", "Kullanıcı bilgileri başartıyla KAYDEDİLDİ!");
        }
        fileWriter.close();
        //fileReader(this.file);
    }

    public File getFile() {
        return this.file;
    }

    @FXML
    public void deleteSettings() throws IOException {
        fileWriter = new FileWriter(this.file,false);


        fileWriter.write("");
        fileWriter.write("");


        fileAlert.alertInformation("Settings.txt", "Başarılı", "Kullanıcı bilgileri başartıyla SİLİNDİ!");

        fileWriter.close();

        settingsData.clear();


    }


    public void fileReader(File file) throws IOException {

        settingsData = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));

        try {
            String line;
            while ((line = br.readLine()) != null) {
                settingsData.add(line);
            }
        } finally {
            br.close();
        }


    }


    public List<String> getSettingsData() {
        return settingsData;
    }

    public void setSettingsData(List<String> settingsData) {
        this.settingsData = settingsData;
    }


    public List<String> getUserAndpass() {
        return userAndpass;
    }


    public File makeFile() {
        File newfile=null;
        try {
            newfile = new File(revCreateDirectory() + "\\" + "settings.txt");
            if (!newfile.exists()){
                newfile.createNewFile();
                System.out.println("File created: " );

            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return newfile;
    }

    public String revCreateDirectory() {
        String path = "";
        folder = new File("C:\\Users\\Public\\Documents\\mySQL");
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                System.out.println("Multiple directories are created!");
                path = "C:\\Users\\Public\\Documents\\mySQL";
            } else {
                System.out.println("Failed to create multiple directories!");
            }
        }
        return path;
    }

}
