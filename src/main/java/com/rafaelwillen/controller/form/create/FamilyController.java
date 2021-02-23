package com.rafaelwillen.controller.form.create;

import com.rafaelwillen.database.DatabaseManager;
import com.rafaelwillen.database.dao.family.FamilyDAO;
import com.rafaelwillen.model.family.Family;
import com.rafaelwillen.util.AlertManager;
import com.rafaelwillen.util.CustomWindow;
import com.rafaelwillen.util.RoutesConstants;
import com.rafaelwillen.util.Validator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

public class FamilyController extends CustomWindow implements Initializable {
    @FXML
    private AnchorPane topBar;

    @FXML
    private TextField familyName_textField;

    @FXML
    private TextField phoneNumber_textField;

    @FXML
    private ComboBox<String> province_comboBox;

    @FXML
    private TextField district_textField;

    @FXML
    private TextField street_textField;

    @FXML
    private ToggleGroup familyType;

    private Family family;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeWindowDraggable(topBar);
        populateProvinces();
        province_comboBox.setValue("Luanda");
    }

    @Override
    public boolean validateForm() {
        String name = familyName_textField.getText();
        String phone = phoneNumber_textField.getText();
        String district = district_textField.getText();
        String street = street_textField.getText();

        if (Validator.isEmpty(name) || Validator.isEmpty(phone) || Validator.isEmpty(district) || Validator.isEmpty(street)) {
            AlertManager.showWarningAlert("Campos Vazios", "Preencha todos os campos obrigatorios");
            return false;
        }
        if (!Validator.isPhoneNumber(phone)) {
            AlertManager.showWarningAlert("Número de telefone inválido", "O número de telefone deve ter 9 dígitos");
            return false;
        }
        return true;
    }

    @FXML
    void closeWindow() {
        closeWindow(topBar, false);
        DatabaseManager.deleteDatabase();
    }

    @FXML
    void nextWindow() {
        if (!validateForm() || !addFamily()) return;
        AlertManager.showSuccessAlert("Família criada", "A família " + familyName_textField.getText() + " foi criada com sucesso");
        closeWindow(topBar, true);
        configCreateParentWindow();
    }

    private void configCreateParentWindow() {
        String type = ((RadioButton) familyType.getSelectedToggle()).getText();

        switch (type.toUpperCase()) {
            case "PAI":
                showCreateParentWindow("PAI");
                break;
            case "MÃE":
                showCreateParentWindow("MAE");
                break;
            default:
                showCreateParentWindow("PAI");
                showCreateParentWindow("MAE");
        }
        // TODO: Show end configuration here
        showEndConfigurationWindow();
    }

    private void showEndConfigurationWindow() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(RoutesConstants.END_CONFIGURATION_FXML));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setTitle("Fim de Configuração");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    private void showCreateParentWindow(String parent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RoutesConstants.CREATE_PARENT_FXML));
        Stage stage = new Stage(StageStyle.UNDECORATED);
        try {
            stage.setScene(new Scene(loader.load()));
            ParentController controller = loader.getController();
            controller.initData(parent, family);
            stage.setTitle("Criar Parente");
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private boolean addFamily() {
        String name = familyName_textField.getText();
        String phone = phoneNumber_textField.getText();
        String province = province_comboBox.getValue();
        String district = district_textField.getText();
        String street = street_textField.getText();
        HashMap<String, String> address = new HashMap<>();
        address.put(Family.PROVINCE_KEY, province);
        address.put(Family.STREET_KEY, street);
        address.put(Family.DISTRICT_KEY, district);
        family = new Family(1, name, phone, address);
        try {
            FamilyDAO.getInstance().add(family);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            AlertManager.showErrorAlert("Erro na criação", "Ocorreu um erro na criação da família, tente novamente");
            return false;
        }
        return true;
    }

    private void populateProvinces() {
        String[] provinceList = {"Cabinda", "Zaire", "Uíge", "Luanda", "Malanje", "Lunda Norte", "Lunda Sul", "Bengo",
                "Cuanza Norte", "Cuanza Sul", "Benguela", "Namibe", "Huíla", "Bíe", "Huambo", "Moxíco",
                "Cuando Cubango", "Cunene"};
        Arrays.sort(provinceList);
        province_comboBox.getItems().addAll(provinceList);
    }
}
