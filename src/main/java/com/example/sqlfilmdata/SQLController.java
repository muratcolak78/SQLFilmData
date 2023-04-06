package com.example.sqlfilmdata;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SQLController {
    @FXML
    private PasswordField TFPassword = new PasswordField();
    @FXML
    private TextField TFUserName = new TextField();
    @FXML
    public Label labelConnection = new Label(), lbWarning;
    @FXML
    private TableColumn<Film, String> clmTitle, clmDescrip, clmLanguage;
    @FXML
    private TableColumn<Film, Integer> col_id, clmRlesYear, clmRentDurat;
    @FXML
    private TableColumn<Film, Double> clmRentrate;
    @FXML
    private TableView table_Film;
    @FXML
    private TextField TFUrl = new TextField();


    @FXML
    private TextField tFID = new TextField(), tFTitle = new TextField(), tFDescrip = new TextField(), tFReleaseyear = new TextField(), tFLanguage = new TextField(), tFRentalRage = new TextField(), tFRentalDuration = new TextField();

    @FXML
    private TextField tFSearch = new TextField();
    @FXML
    private Button BtSave = new Button(), BtDelete = new Button(), btAd = new Button(), btUpDate = new Button(), btDelete = new Button();
    @FXML
    private Button BtGetConnction = new Button();

    private List<String> makeNullList = new ArrayList<>(Arrays.asList("", ""));
    private List<String> Listdata = new ArrayList<>();
    @FXML
    private Hyperlink linkDelete = new Hyperlink();

    int index = -1;
    private FileAlertC alert = new FileAlertC();
    @FXML
    private ObservableList<Film> dataList;
    private DBConnection dbConnection = new DBConnection();
    private Connection connection = null;
    @FXML
    private File file = new File("C:\\Users\\Public\\Documents\\mySQLFilmData2\\settings.txt");
    private FileWriter fileWriter;
    private String getPath;
    private List<String> readedData;
    private String userName;
    private String passWord;
    @FXML
    private Button buttonDataSettinsDelete;
    private List<String> information = new ArrayList<>();

    @FXML
    public void linkDelete() throws SQLException {
        file.delete();
        TFUserName.setText("");
        TFPassword.setText("");
        dataList.clear();
        buttonDataSettinsDelete.setDisable(true);
        table_Film.setVisible(false);
        setTextFieldEditable(false);
        setButtonsDisable(true);
        TFUserName.setEditable(true);
        TFPassword.setEditable(true);
        updateTable(dataList);
        connection.close();
        alert.alertInformation("Settings", "Username ve Password verileri silindi", "");


    }

    @FXML
    public void allActions(ActionEvent e) throws IOException, SQLException {

        String misson = ((Button) e.getSource()).getText();


        switch (misson) {
            case "Get Connection":

                userName = TFUserName.getText();
                passWord = TFPassword.getText();
                System.out.println(userName + "   " + passWord);

                if (file.exists()) buttonDataSettinsDelete.setDisable(false);

                if (userName.isEmpty() && passWord.isEmpty()) {
                    System.out.println(" herikisi boşsa komutu çalıştı");

                    if (file.exists()) {
                        System.out.println(file.exists());
                        System.out.println("dosya varsa koşulu çalıştı");

                        List<String> readData = new ArrayList<>();
                        String path = "C:\\Users\\Public\\Documents\\mySQLFilmData2\\settings.txt";
                        fileReader(readData, path);
                        userName = readData.get(0);
                        passWord = readData.get(1);

                        readData.clear();

                        information.add("Kullanıcı adı ve şifre bilgileri settings datadan çekildi");


                    } else if (!file.exists()) {
                        System.out.println("eğer iki fieldde boşsa ve dosya yoksa koşulu çalıştı");
                        alert.alertError("Uyarı", "Lütfen kullanıcı adı ve şifre bilgilerini giriniz", "");
                        break;
                    }
                } else {
                    userName = TFUserName.getText();
                    passWord = TFPassword.getText();

                    System.out.println(" her iki field dolu koşulu çalıştı");

                    makeFile();

                    List<String> writeData = new ArrayList<>();
                    String path = "C:\\Users\\Public\\Documents\\mySQLFilmData2\\settings.txt";

                    writeData.add(userName);
                    writeData.add(passWord);

                    fileWriter(writeData, path);

                    writeData.clear();

                    TFUserName.setText("");
                    TFPassword.setText("");
                    information.add("Kullanıcı adı ve şifre settings datadan çekildi");

                }

                System.out.println("connection öncesi son durum");
                System.out.println(userName + "--" + passWord);

                getDataFilm();

                updateTable(dataList);

                if (dataList.size() > 0) {
                    table_Film.setVisible(true);
                    setTextFieldEditable(true);
                    setButtonsDisable(false);
                    TFUserName.setEditable(false);
                    TFPassword.setEditable(false);

                    alert.alertInformation2("Connection", information, "Database Bağlantısı Başarılı");
                } else {

                    information.add("Geçersiz kullanıcı adı veya parola");
                    alert.alertInformation2("Connection", information, "Database Bağlantısı Kurulamadı");

                }

                information.clear();
                break;

            case "Add":
                add();
                connection.close();
                break;
            case "Delete Data":
                delete();
                connection.close();
                break;
            case "Update":
                upDate();
                connection.close();
                break;

        }

    }

    public void makeTFactive() {
        TFUserName.setEditable(true);
        TFUrl.setEditable(true);
        TFPassword.setEditable(true);

    }

    public void makeTFPassive() {
        TFUserName.setEditable(false);
        TFUrl.setEditable(false);
        TFPassword.setEditable(false);
    }

    @FXML
    public void about() {
        alert.alertInformation("About", " bu program databasede sabit bir  dosyaya bağanır ve görüntüleme, ekleme güncelleme ve silme işlemleri yapabilir. ", "Programmed by colak");

    }


    @FXML
    void getSelected(MouseEvent event) {
        index = table_Film.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        tFID.setText(col_id.getCellData(index).toString());
        tFTitle.setText(clmTitle.getCellData(index).toString());
        tFDescrip.setText(clmDescrip.getCellData(index).toString());
        tFReleaseyear.setText(clmRlesYear.getCellData(index).toString());
        tFLanguage.setText(clmLanguage.getCellData(index));
        tFRentalRage.setText(clmRentrate.getCellData(index).toString());
        tFRentalDuration.setText(clmRentDurat.getCellData(index).toString());

    }

    public void updateTable(ObservableList<Film> liste) {
        System.out.println("update data çalıştı");
        this.table_Film.setItems(liste);
        col_id.setCellValueFactory(new PropertyValueFactory<Film, Integer>("film_id"));
        clmTitle.setCellValueFactory(new PropertyValueFactory<Film, String>("title"));
        clmDescrip.setCellValueFactory(new PropertyValueFactory<Film, String>("description"));
        clmRlesYear.setCellValueFactory(new PropertyValueFactory<Film, Integer>("release_year"));
        clmLanguage.setCellValueFactory(new PropertyValueFactory<Film, String>("language"));
        clmRentrate.setCellValueFactory(new PropertyValueFactory<Film, Double>("rental_rate"));
        clmRentDurat.setCellValueFactory(new PropertyValueFactory<Film, Integer>("rental_duration"));

    }


    public void delete() {
        connection = dbConnection.getConnection(userName, passWord);
        boolean sonuc = alert.alertConfirmation("Uyarı film birden fazla tablo ile ilişkili", tFID.getText() + " " + tFTitle.getText() + " isimli  film  silindiğinde  ilişkili tablolarda da silinecek  ", " Silmek için Eminmisiniz?");

        String sql3 = " SET FOREIGN_KEY_CHECKS = 0; delete from sakila.film where film_id= " + tFID.getText() + "; delete from sakila.film_actor where film_id= " + tFID.getText() + "; SET FOREIGN_KEY_CHECKS = 1;";
        String[] listr = new String[4];
        listr[0] = " SET FOREIGN_KEY_CHECKS = 0";
        listr[1] = "delete from sakila.film where film_id= " + tFID.getText() + ";";
        listr[2] = "delete from sakila.film_actor where film_id= " + tFID.getText() + ";";
        listr[3] = "SET FOREIGN_KEY_CHECKS = 1;";
        System.out.println(sonuc);

        try {
            for (int i = 0; i < listr.length; i++) {


                if (sonuc) {
                    PreparedStatement preparedStatement = connection.prepareStatement(listr[i]);
                    preparedStatement.executeUpdate(listr[i]);

                }
            }
            alert.alertInformation("Bilgilendirme", tFID.getText() + " " + tFTitle.getText() + " isimli film silindi", "..");

            getDataFilm();
            updateTable(dataList);
            tFID.setText("");
            tFTitle.setText("");
            tFDescrip.setText("");
            tFReleaseyear.setText("");
            tFLanguage.setText("");
            tFRentalRage.setText("");
            tFRentalDuration.setText("");
            tFID.setEditable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void upDate() {

        int language = 0;
        if (tFLanguage.getText().equals("English")) {
            language = 1;
        } else if (tFLanguage.getText().equalsIgnoreCase("Italian")) {
            language = 2;
        } else if (tFLanguage.getText().equalsIgnoreCase("Japanese")) {
            language = 3;
        } else if (tFLanguage.getText().equalsIgnoreCase("Mandarin")) {
            language = 4;
        } else if (tFLanguage.getText().equalsIgnoreCase("French")) {
            language = 5;
        } else if (tFLanguage.getText().equalsIgnoreCase("German")) {
            language = 6;
        } else {
            language = 7;
        }

        connection = dbConnection.getConnection(userName, passWord);
        //String sql = "delete from sakila.film where film_id ="+tFID.getText()+";";

        String sql = "UPDATE sakila.film SET title='" + tFTitle.getText() + "',description='" + tFDescrip.getText() + "', language_id='" + language + "', release_year= '" + tFReleaseyear.getText() + "' , rental_duration='" + tFRentalDuration.getText() + "', rental_rate='" + tFRentalRage.getText() + "', length= 0, replacement_cost= 0.0, special_features=0, last_update = CURRENT_TIMESTAMP WHERE film_id=" + tFID.getText() + ";";
        boolean sonuc = alert.alertConfirmation("Uyarı ", tFID.getText() + " " + tFTitle.getText() + " isimli  film güncellenecek", "Eminmisiniz?");
        System.out.println(sonuc);
        System.out.println(sql);
        //'language.name='" + tFLanguage.getText() + "
        try {
            if (sonuc) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.executeUpdate(sql);

                alert.alertInformation("Bilgilendirme", tFID.getText() + " " + tFTitle.getText() + " isimli film güncellendi", "..");
            }
            getDataFilm();
            updateTable(dataList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        tFID.setText("");
        tFTitle.setText("");
        tFDescrip.setText("");
        tFReleaseyear.setText("");
        tFLanguage.setText("");
        tFRentalRage.setText("");
        tFRentalDuration.setText("");

    }


    public ObservableList<Film> getDataFilm() throws IOException {

        connection = dbConnection.getConnection(userName, passWord);
        dataList = FXCollections.observableArrayList();

        try {

            String sql = "select film_id,title, description, release_year,language.name, rental_rate, rental_duration   from sakila.film inner join  sakila.language on sakila.film.language_id=language.language_id;";
            Statement ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery(sql);


            while (resultSet.next()) {
                dataList.add(new Film(
                        resultSet.getInt("film_id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getInt("release_year"),
                        resultSet.getString("language.name"),
                        resultSet.getDouble("rental_rate"),
                        resultSet.getInt("rental_duration")));

            }


        } catch (Exception e) {
            System.out.println(e.getMessage());

            e.printStackTrace();
        }
        System.out.println("data list eklemesi çalıştı");
        return dataList;
    }

    @FXML
    public ObservableList<Film> search() throws SQLException, IOException {


        connection = dbConnection.getConnection(userName, passWord);
        ObservableList<Film> list = FXCollections.observableArrayList();

        try {

            String sql = "select film_id,title, description, release_year,language.name, rental_rate, rental_duration   from sakila.film inner join  sakila.language on sakila.film.language_id=language.language_id;";
            String sql2 = "select  film_id,title, description, release_year, name, rental_rate, rental_duration   from sakila.film inner join  sakila.language on sakila.film.language_id=language.language_id where title like '" + tFSearch.getText() + "%';";
            PreparedStatement ps = connection.prepareStatement(sql2);
            ResultSet resultSet = ps.executeQuery(sql2);


            while (resultSet.next()) {

                list.add(new Film(
                        resultSet.getInt("film_id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getInt("release_year"),
                        resultSet.getString("language.name"),
                        resultSet.getDouble("rental_rate"),
                        resultSet.getInt("rental_duration")));

            }

            if (list.size() == 0) {
                alert.alertError("Bilgilendirme", "Search Film", "Bu adda film bulunamadı");
            }
            updateTable(list);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @FXML
    public void test() {
        System.out.println(" çalıştı");
    }


    public void add() {
        int language = 0;
        if (tFLanguage.getText().equals("English")) {
            language = 1;
        } else if (tFLanguage.getText().equalsIgnoreCase("Italian")) {
            language = 2;
        } else if (tFLanguage.getText().equalsIgnoreCase("Japanese")) {
            language = 3;
        } else if (tFLanguage.getText().equalsIgnoreCase("Mandarin")) {
            language = 4;
        } else if (tFLanguage.getText().equalsIgnoreCase("French")) {
            language = 5;
        } else if (tFLanguage.getText().equalsIgnoreCase("German")) {
            language = 6;
        } else {
            language = 7;
        }

        connection = dbConnection.getConnection(userName, passWord);

        boolean sonuc = alert.alertConfirmation("Uyarı ", tFID.getText() + " " + tFTitle.getText() + " isimli  film listeye eklenecek", "Eminmisiniz?");
        String sql2 = "INSERT INTO sakila.film (title, description, language_id, release_year, rental_duration, rental_rate, length, replacement_cost, special_features, last_update) value ('" + tFTitle.getText() + "', '" + tFDescrip.getText() + "', '" + language + "', '" + tFReleaseyear.getText() + "', '" + tFRentalDuration.getText() + "', '" + tFRentalRage.getText() + "', 0, 0.0, 0, CURRENT_TIMESTAMP );";


        try {
            if (sonuc) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql2);
                preparedStatement.executeUpdate(sql2);

                alert.alertInformation("Bilgilendirme", tFID.getText() + " " + tFTitle.getText() + " isimli film listeye eklendi", "..");
            }
            getDataFilm();
            updateTable(dataList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        tFID.setText("");
        tFTitle.setText("");
        tFDescrip.setText("");
        tFReleaseyear.setText("");
        tFLanguage.setText("");
        tFRentalRage.setText("");
        tFRentalDuration.setText("");

    }

    public void setTextFieldEditable(boolean trueOrFalse) {
        tFTitle.setEditable(trueOrFalse);
        tFDescrip.setEditable(trueOrFalse);
        tFReleaseyear.setEditable(trueOrFalse);
        tFLanguage.setEditable(trueOrFalse);
        tFRentalRage.setEditable(trueOrFalse);
        tFRentalDuration.setEditable(trueOrFalse);
        tFSearch.setEditable(trueOrFalse);
    }

    public void setButtonsDisable(boolean trueOrFalse) {
        btAd.setDisable(trueOrFalse);
        btDelete.setDisable(trueOrFalse);
        btUpDate.setDisable(trueOrFalse);
    }

    public File folderAndFileMaker() {


        return file;
    }

    public File makeFile() throws IOException {
        folderMaker();
        try {

            file = new File("C:\\Users\\Public\\Documents\\mySQLFilmData2\\settings.txt");
            file.createNewFile();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return file;

    }

    public void folderMaker() throws IOException {
        File folder = new File("C:\\Users\\Public\\Documents\\mySQLFilmData2");

        if (folder.mkdirs()) {
            System.out.println("Multiple directories are created!");
        } else {
            System.out.println("Failed to create multiple directories!");
        }

    }

    public List<String> fileReader(List<String> readedData, String path) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(path));
        String text = "";
        try {
            String line;
            while ((line = fileReader.readLine()) != null) {
                text += line + "\n";
                System.out.println("filereader " + text);
                readedData.add(line);

            }
            buttonDataSettinsDelete.setDisable(false);
        } finally {
            fileReader.close();
        }
        //alert.alertInformation("bilgilendirme", "dosyadan veriler okundu", path);
        information.add("dosyadan veriler okundu");

        return readedData;
    }

    @FXML
    public void fileWriter(List<String> listWriteData, String path) throws IOException {
        fileWriter = new FileWriter(path, false);
        for (int i = 0; i < listWriteData.size(); i++) {
            fileWriter.write(listWriteData.get(i) + "\n");
        }
        buttonDataSettinsDelete.setDisable(false);
        fileWriter.close();
        //alert.alertInformation("bilgilendirme", "Veriler dosyaya yazdırıldı", path);
        information.add("Veriler dosyaya yazdırıldı");
    }

    @FXML
    public void showSingleFileChooser() {
        if (file.exists()) {
            FileChooser fileChooser = new FileChooser();

            fileChooser.setInitialDirectory(new File("C:\\Users\\Public\\Documents\\mySQLFilmData2"));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );
            File selectedFile = fileChooser.showOpenDialog(null);
        } else {
            alert.alertInformation("Dikkat", "Settings file bulunamadı", "Ana sayfada username ve password kaydediniz.");
        }


    }

}



    /*
     önce observation list oluşturuyoruz:
     daha sonra observation list return eden bir metot yazıyoruz.
        bu metot sql kodu ile çağırılan listeyi alıyor ve
        sürekli film objesi oluşturarak listeye atıyor
        result set de ki columnname ler sql data daki isimler ile aynı olmalı
      daha sonra updateTable () ismli bir tablo yapıyoruz.
      bu tabloda yeni bir observation listesi içine atıyoruz
      bu listeyi tablo içine setItems(list);codu ile atıyoruz.
      altında ise setCellValueFactory(new PropertyValueFactory ile değerine göre ekliyoruz.

     */

