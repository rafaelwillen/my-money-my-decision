package com.rafaelwillen.controller.util;

import com.rafaelwillen.database.DatabaseManager;
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
import java.util.ResourceBundle;

public class WelcomeController extends CustomWindow implements Initializable {

    @FXML
    private AnchorPane topBar;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeWindowDraggable(topBar);
    }

    @FXML
    void closeWindow(MouseEvent event) {
       if (closeWindow(topBar, false))
        DatabaseManager.deleteDatabase();
    }

    @FXML
    void nextPage(ActionEvent event) {
        Stage stage = new Stage(StageStyle.UNDECORATED);
        try {
            Parent root = FXMLLoader.load(getClass().getResource(RoutesConstants.CREATE_FAMILY_FXML));
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            stage.setTitle("Criar Família");
            closeWindow(topBar, true);
            stage.show();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    protected boolean validateForm() {
        try {
            throw new IllegalAccessException("Must not be called");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
