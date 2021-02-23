package com.rafaelwillen;

import com.rafaelwillen.database.DatabaseManager;
import com.rafaelwillen.database.SQLiteConnection;
import com.rafaelwillen.util.AlertManager;
import com.rafaelwillen.util.RoutesConstants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        if (!SQLiteConnection.testConnection()){
            Alert databaseError = new Alert(Alert.AlertType.ERROR);
            databaseError.setTitle("Erro na base de dados");
            databaseError.setHeaderText("Erro na base de dados");
            databaseError.setContentText("Não foi possível conectar na base de dados");
            databaseError.showAndWait();
            return;
        }
        Parent root;
        String windowTitle;
        try {
            DatabaseManager.initDatabase();
            if (DatabaseManager.firstTimeUsage()){
                root = FXMLLoader.load(getClass().getResource(RoutesConstants.WELCOME_SCREEN_FXML));
                windowTitle = "Tela de configuração";
            } else {
                root = FXMLLoader.load(getClass().getResource(RoutesConstants.LOGIN_FXML));
                windowTitle = "Login";
            }
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle(windowTitle);
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            AlertManager.showErrorAlert("Erro Fatal", "Ocorreu um erro ao carregar a base de dados");
        }
    }

}