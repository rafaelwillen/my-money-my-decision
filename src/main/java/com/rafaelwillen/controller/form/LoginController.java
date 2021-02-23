package com.rafaelwillen.controller.form;

import com.rafaelwillen.controller.dashboard.DashboardController;
import com.rafaelwillen.database.dao.family.FamilyDAO;
import com.rafaelwillen.database.dao.family.PersonDAO;
import com.rafaelwillen.model.family.Family;
import com.rafaelwillen.model.family.Person;
import com.rafaelwillen.util.AlertManager;
import com.rafaelwillen.util.CustomWindow;
import com.rafaelwillen.util.RoutesConstants;
import com.rafaelwillen.util.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController extends CustomWindow implements Initializable {

    @FXML
    private AnchorPane topBar;

    @FXML
    private TextField username_textField;

    @FXML
    private PasswordField password_passwordField;

    private Person userLoggedIn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeWindowDraggable(topBar);
    }

    @FXML
    void closeWindow(MouseEvent event) {
        closeWindow(topBar, false);
    }

    @FXML
    void login(ActionEvent event) {
        if (!validateForm()) return;

        String username = username_textField.getText();
        String password = password_passwordField.getText();

        try {
            if (!PersonDAO.getInstance().authenticate(username, password)) {
                AlertManager.showErrorAlert("Utilizador Não Encontrado", "Os dados inseridos não pertencem a um utilizador");
                return;
            }
            userLoggedIn = PersonDAO.getInstance().get(username);
            AlertManager.showSuccessAlert("Login Efectuado", "Login bem sucedido");
            showDashboard();
            closeWindow(topBar, true);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            AlertManager.showErrorAlert("Erro na Base de Dados", "Ocorreu um erro na base de dados. Por favor tente novamente");
        }
    }

    @FXML
    void showPassword(ActionEvent event) {
        String password = password_passwordField.getText();
        Alert passwordDialog = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
        passwordDialog.setTitle("Mostrar Password");
        passwordDialog.setHeaderText("Password: " + password);
        passwordDialog.show();
    }

    @Override
    protected boolean validateForm() {
        String username = username_textField.getText();
        String password = password_passwordField.getText();
        if (Validator.isEmpty(username) || Validator.isEmpty(password)) {
            AlertManager.showWarningAlert("Campos Vazios", "Preencha todos os campos obrigatorios");
            return false;
        }
        if (!Validator.isUsernameOrPassword(username) || !Validator.isUsernameOrPassword(password)) {
            AlertManager.showWarningAlert("Username ou password inválidos", "O username e a password devem ter 8 caractéres");
            return false;
        }
        return true;
    }

    private void showDashboard() throws SQLException {
        Family family = FamilyDAO.getInstance().get(1);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(RoutesConstants.DASHBOARD_SCREEN_FXML));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setResizable(false);
            stage.setTitle("My Money My Decision - " + family.getName());
            stage.setScene(new Scene(loader.load()));
            DashboardController controller = loader.getController();
            controller.initData(userLoggedIn, family);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }


}
