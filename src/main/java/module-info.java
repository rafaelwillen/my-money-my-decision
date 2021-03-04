module com.rafaelwillen {
    requires javafx.controls;
    requires javafx.fxml;
    requires sqlite.jdbc;
    requires java.sql;

    opens com.rafaelwillen to javafx.fxml, javafx.graphics;
    opens com.rafaelwillen.controller.form.create to javafx.fxml;
    opens com.rafaelwillen.controller.form to javafx.fxml;
    opens com.rafaelwillen.controller.util to javafx.fxml;
    opens com.rafaelwillen.controller.dashboard to javafx.fxml;
    opens com.rafaelwillen.util.tableViewModel to javafx.base;
    exports com.rafaelwillen.controller.form.create;
    exports com.rafaelwillen.controller.dashboard;
}