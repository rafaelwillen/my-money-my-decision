package com.rafaelwillen.util;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

public abstract class CustomWindow {
    protected double xOffset = 0;
    protected double yOffset = 0;

    /**
     * Closes the window which the node exists
     *
     * @param node The node
     */
    protected boolean closeWindow(Node node, boolean toGoNextScreen) {
        if (toGoNextScreen) {
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        } else {
            if (confirmClose()) {
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();
                return true;
            }
            return false;
        }
        return true;
    }

    /**
     * Makes the undecorated window draggable which the node exists
     *
     * @param node The node
     */
    protected void makeWindowDraggable(Node node) {
        node.setOnMousePressed(event -> {
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
        node.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        node.setOnMouseDragged(event -> {
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    /**
     * Minimizes the window which the node exists
     *
     * @param node The node
     */
    protected void minimizeWindow(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setIconified(true);
    }

    private boolean confirmClose() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Fechar?");
        confirmationAlert.getButtonTypes().clear();
        confirmationAlert.getButtonTypes().addAll(new ButtonType("Sim", ButtonBar.ButtonData.YES), new ButtonType("Não", ButtonBar.ButtonData.NO));
        confirmationAlert.setContentText("Tem a certeza que deseja fechar?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        return result.isPresent() && result.get().getButtonData().equals(ButtonBar.ButtonData.YES);
    }

    protected abstract boolean validateForm();

}
