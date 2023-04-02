package com.example.sqlfilmdata;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class SQLController {
    @FXML
    private PasswordField TFPassword = new PasswordField();
    @FXML
    private TextField TFUserName = new TextField();
    @FXML
    public Label labelConnection = new Label();
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
    private TextField tfLogUsername;
    @FXML
    private PasswordField tFLogPassword;
    @FXML
    private CheckBox keepMeLogged;


    @FXML
    private TextField tFID = new TextField(), tFTitle = new TextField(), tFDescrip = new TextField(), tFReleaseyear = new TextField(), tFLanguage = new TextField(), tFRentalRage = new TextField(), tFRentalDuration = new TextField();

    @FXML
    private TextField tFSearch = new TextField();
    @FXML
    private Button BtSave = new Button(), BtDelete = new Button(), btAd = new Button(), btUpDate = new Button(), btDelete = new Button();
    @FXML
    private Button BtGetConnction = new Button();
    private MakeFolderFileWriteAndRead fileX;
    private List<String> makeNullList = new ArrayList<>(Arrays.asList("", ""));
    private List<String> Listdata = new ArrayList<>();
    private File file;


    int index = -1;
    FileAlertC alert = new FileAlertC();

    ObservableList<Film> dataList;


    DBConnection dbConnection = new DBConnection();
    Connection connection = null;

    public SQLController() throws IOException {
        fileX = new MakeFolderFileWriteAndRead();
        file = fileX.getFile2();
        System.out.println("***" + fileX.getFile2().exists());

    }

    @FXML
    public void allActions(ActionEvent e) throws IOException, SQLException {

        String misson = ((Button) e.getSource()).getText();

        switch (misson) {
            case "Get Connection":
                if (fileX.getFile2().exists() && TFUserName.getText().isEmpty() && TFPassword.getText().isEmpty()) {
                    fileX.readExistFile();
                    fileX.makeFileAndaSave(fileX.getSettingsData());
                    System.out.println(fileX.getSettingsData());

                } else if (fileX.getFile2().exists() && !TFUserName.getText().isEmpty() || !TFPassword.getText().isEmpty()) {
                    fileX.makeFileAndaSave(makeNullList);
                    Listdata.clear();
                    //fileX.getFile2().delete();
                    Listdata.add(TFUserName.getText());
                    Listdata.add(TFPassword.getText());
                    fileX.makeFileAndaSave(Listdata);


                } else if (!fileX.getFile2().exists()) {
                    if (TFUserName.getText().isEmpty() && TFPassword.getText().isEmpty()) {
                        fileX.makeFileAndaSave(makeNullList);
                        //fileX.getSettingsData().clear();
                        updateTable(dataList);
                    } else {
                        Listdata.add(TFUserName.getText());
                        Listdata.add(TFPassword.getText());

                        fileX.makeFileAndaSave(Listdata);
                        fileX.getSettingsData().clear();
                        updateTable(dataList);
                    }

                } else {

                }
                System.out.println("--------" + TFUserName.getText());
                System.out.println("********" + TFPassword.getText());

                TFUserName.setText("");
                TFPassword.setText("");
                //makeTFPassive();
                table_Film.setVisible(false);

                System.out.println(fileX.getSettingsData());

                //file.fileReader(this.file.getFile());
                System.out.println(fileX.getSettingsData() + " " + fileX.getSettingsData().size());
                if (fileX.getSettingsData().size() < 2) {
                    alert.alertInformation("Connection", "  ", "Lütfen geçerli kullanıcı adı ve şifreyi giriniz");
                    break;

                } else {

                    if (fileX.getSettingsData().size() > 0) {
                        getDataFilm();
                        updateTable(dataList);
                        table_Film.setVisible(true);
                        setTextFieldEditable(true);
                        setButtonsDisable(false);
                        alert.alertInformation("Connection", " Database bağlantısı", "Başarılı");
                        break;
                    } else {
                        alert.alertInformation("Connection", " Database bağlantısı kurulamadı ", "Lütfen geçerli kullanıcı adı ve şifreyi giriniz");
                        break;
                    }

                }

            case "Delete Data Setting":

                fileX.getFile2().delete();
                TFUrl.setText("");
                TFUserName.setText("");
                TFPassword.setText("");
                makeTFactive();
                //dataList.removeAll();
                table_Film.setVisible(false);
                setTextFieldEditable(false);
                setButtonsDisable(true);
                fileX.getSettingsData().clear();
                updateTable(dataList);
                fileX.setSettingsData(makeNullList);
                Listdata.clear();
                connection.close();


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
        table_Film.setItems(liste);
        col_id.setCellValueFactory(new PropertyValueFactory<Film, Integer>("film_id"));
        clmTitle.setCellValueFactory(new PropertyValueFactory<Film, String>("title"));
        clmDescrip.setCellValueFactory(new PropertyValueFactory<Film, String>("description"));
        clmRlesYear.setCellValueFactory(new PropertyValueFactory<Film, Integer>("release_year"));
        clmLanguage.setCellValueFactory(new PropertyValueFactory<Film, String>("language"));
        clmRentrate.setCellValueFactory(new PropertyValueFactory<Film, Double>("rental_rate"));
        clmRentDurat.setCellValueFactory(new PropertyValueFactory<Film, Integer>("rental_duration"));

    }


    public void delete() {
        connection = dbConnection.getConnection(fileX.getSettingsData().get(0), fileX.getSettingsData().get(1));
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

        connection = dbConnection.getConnection(fileX.getSettingsData().get(0), fileX.getSettingsData().get(1));
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

        //fileX.fileReader(this.file.getFile());
        connection = dbConnection.getConnection(fileX.getSettingsData().get(0), fileX.getSettingsData().get(1));
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
            System.out.println(" burası çalıştı");
            e.printStackTrace();
        }

        return dataList;
    }

    @FXML
    public ObservableList<Film> search() throws SQLException, IOException {

        //ObservableList<Film> liste = getDataFilm();
        connection = dbConnection.getConnection(fileX.getSettingsData().get(0), fileX.getSettingsData().get(1));
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

        connection = dbConnection.getConnection(fileX.getSettingsData().get(0), fileX.getSettingsData().get(1));

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

