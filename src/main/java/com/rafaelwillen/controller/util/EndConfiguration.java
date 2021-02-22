package com.rafaelwillen.controller.util;

import com.rafaelwillen.database.DatabaseManager;
import com.rafaelwillen.util.AlertManager;
import com.rafaelwillen.util.CustomWindow;
import com.rafaelwillen.util.RoutesConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EndConfiguration extends CustomWindow implements Initializable {

    @FXML
    private AnchorPane topBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            DatabaseManager.setSetupComplete();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            AlertManager.showErrorAlert("Erro na base de dados", "Erro na base de dados. Tente novamente");
        }
        makeWindowDraggable(topBar);
    }

    @FXML
    void closeWindow(MouseEvent event) {
        nextPage(null);
    }

    @FXML
    void nextPage(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(RoutesConstants.LOGIN_FXML));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.setResizable(false);
            stage.show();
            closeWindow(topBar, true);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            AlertManager.showErrorAlert("Erro Fatal", "Ocorreu um erro fatal. Por favor reinstala a aplicacao");
        }

    }

    @Override
    protected boolean validateForm() {
        return false;
    }
}
