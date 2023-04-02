package com.example.sqlfilmdata;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MakeFolderFileWriteAndRead {


    private String path;
    @FXML
    private File file = null;
    private File file2 = new File("C:\\Users\\Public\\Documents\\mySQLFilmData\\settings.txt");
    @FXML
    private File files = null;
    @FXML
    private FileWriter fileWriter;
    @FXML
    private List<String> settingsData = new ArrayList<>();
    private String getPath;

    public List<String> getSettingsData() {
        return settingsData;
    }

    public void setSettingsData(List<String> settingsData) {
        this.settingsData = settingsData;
    }

    @FXML
    protected void makeFileAndaSave(List<String> datalist) throws IOException {

        makeFile();
        writeSometihngs(datalist);
        fileReader();
    }


    public File getFile() {
        return file;
    }

    public File makeFile() throws IOException {

        try {
            file = new File(revCreateDirectory() + "\\" + "settings.txt");
            file.createNewFile();
            getPath = revCreateDirectory() + "\\" + "settings.txt";

            System.out.println("File created: " + file2.getName());

            System.out.println("File already exists.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();

        }
        return file;

    }


    public String revCreateDirectory() throws IOException {
        String path = "";
        files = new File("C:\\Users\\Public\\Documents\\mySQLFilmData");

        if (files.mkdirs()) {
            System.out.println("Multiple directories are created!");
            path = "C:\\Users\\Public\\Documents\\mySQLFilmData";
        } else {
            System.out.println("Failed to create multiple directories!");
        }

        return path;
    }

    public void fileReader() throws IOException {
        settingsData.clear();
        BufferedReader br = new BufferedReader(new FileReader(this.file2));
        String text = "";
        try {
            String line;
            while ((line = br.readLine()) != null) {
                text += line + "\n";
                System.out.println(text);
                settingsData.add(line);
            }
        } finally {
            br.close();
        }

    }

    @FXML
    public void writeSometihngs(List<String> list) throws IOException {
        fileWriter = new FileWriter(file2, false);
        for (int i = 0; i < list.size(); i++) {
            fileWriter.write(list.get(i) + "\n");
        }
        fileWriter.close();

    }

    public File getFile2() {
        return file2;
    }

    public void readExistFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file2));
        String text = "";
        try {
            String line;
            while ((line = br.readLine()) != null) {
                text += line + "\n";
                System.out.println(text);
                settingsData.add(line);
            }
        } finally {
            br.close();
        }

    }

}
