module com.example.sqlfilmdata {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.sqlfilmdata to javafx.fxml;
    exports com.example.sqlfilmdata;
}