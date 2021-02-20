module com.rafaelwillen {
    requires javafx.controls;
    requires javafx.fxml;
    requires sqlite.jdbc;
    requires java.sql;

    opens com.rafaelwillen to javafx.fxml;
    exports com.rafaelwillen;
    exports com.rafaelwillen.controller;
}