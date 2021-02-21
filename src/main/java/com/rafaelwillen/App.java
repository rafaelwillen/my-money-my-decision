package com.rafaelwillen;

import com.rafaelwillen.database.DatabaseManager;
import com.rafaelwillen.util.AlertManager;
import com.rafaelwillen.util.RoutesConstants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        try {
            DatabaseManager.initDatabase();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            AlertManager.showErrorAlert("Erro Fatal", "Ocorreu um erro ao carregar a base de dados");
        }
        try {
            Parent root = FXMLLoader.load(getClass().getResource(RoutesConstants.CREATE_FAMILY_FXML));
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            stage.setTitle("Criar Familia");
            stage.show();
        } catch (IOException e) {
            AlertManager.showErrorAlert("Erro Fatal", "Ocorreu um erro ao carregar a janela");
            e.printStackTrace();
        }

    }

}