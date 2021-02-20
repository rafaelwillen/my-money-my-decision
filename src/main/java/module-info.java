module com.rafaelwillen {
    requires javafx.controls;
    requires javafx.fxml;
    requires sqlite.jdbc;

    opens com.rafaelwillen to javafx.fxml;
    exports com.rafaelwillen;
}