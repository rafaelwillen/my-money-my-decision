package com.rafaelwillen.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

import java.util.Optional;

public class AlertManager {
    private AlertManager() {
    }

    /**
     * Shows a success alert
     *
     * @param title   The alert title
     * @param content The alert content
     */
    public static void showSuccessAlert(String title, String content) {
        Alert successAlert = buildAlert(title, content);
        successAlert.setAlertType(Alert.AlertType.INFORMATION);
        successAlert.showAndWait();
    }

    /**
     * Shows a error alert
     *
     * @param title   The alert title
     * @param content The alert content
     */
    public static void showErrorAlert(String title, String content) {
        Alert errorAlert = buildAlert(title, content);
        errorAlert.setAlertType(Alert.AlertType.ERROR);
        errorAlert.showAndWait();
    }

    /**
     * Shows a warning alert
     *
     * @param title   The alert title
     * @param content The alert content message
     */
    public static void showWarningAlert(String title, String content) {
        Alert warningAlert = buildAlert(title, content);
        warningAlert.setAlertType(Alert.AlertType.WARNING);
        warningAlert.showAndWait();
    }

    public static boolean showConfirmationDialog(String title, String content) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, title);
        confirmation.setHeaderText(title);
        confirmation.setContentText(content);
        confirmation.initModality(Modality.APPLICATION_MODAL);
        ButtonType sim = new ButtonType("Sim", ButtonBar.ButtonData.YES);
        ButtonType nao = new ButtonType("Não", ButtonBar.ButtonData.NO);
        confirmation.getButtonTypes().clear();
        confirmation.getButtonTypes().addAll(sim, nao);
        Optional<ButtonType> result = confirmation.showAndWait();
        return result.isPresent() && result.get().getButtonData().equals(ButtonBar.ButtonData.YES);
    }

    private static Alert buildAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        return alert;
    }

}
