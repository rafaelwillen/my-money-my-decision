package com.rafaelwillen;

import com.rafaelwillen.database.DatabaseManager;
import com.rafaelwillen.database.SQLiteConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * JavaFX App
 */
public class App extends Application {
    @Override
    public void start(Stage stage) {

    }

    public static void main(String[] args) {
        launch();
    }

}