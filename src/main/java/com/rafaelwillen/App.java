package com.rafaelwillen;

import com.rafaelwillen.database.SQLiteConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * JavaFX App
 */
public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/rafaelwillen/fxml/HelloWorld.fxml"));
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Hello World");
        stage.show();

        var con = SQLiteConnection.connect();
        System.out.println("DB Connected");
        SQLiteConnection.closeConnection(con);
    }

    public static void main(String[] args) {
        launch();
    }

}