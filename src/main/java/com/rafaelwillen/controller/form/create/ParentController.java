package com.rafaelwillen.controller.form.create;


import com.rafaelwillen.database.DatabaseManager;
import com.rafaelwillen.database.dao.family.EmailDAO;
import com.rafaelwillen.database.dao.family.PersonDAO;
import com.rafaelwillen.database.dao.family.PhoneNumberDAO;
import com.rafaelwillen.model.family.Family;
import com.rafaelwillen.model.family.Parent;
import com.rafaelwillen.model.family.Sex;
import com.rafaelwillen.util.AlertManager;
import com.rafaelwillen.util.CustomWindow;
import com.rafaelwillen.util.Validator;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;


public class ParentController extends CustomWindow implements Initializable {
    @FXML
    private AnchorPane topBar;

    @FXML
    private Label windowTitle;

    @FXML
    private Text header;

    @FXML
    private TextField name_textField;

    @FXML
    private ListView<String> phone_listView;

    @FXML
    private ListView<String> email_listView;

    @FXML
    private DatePicker birthDate_date;

    @FXML
    private TextField username_textField;

    @FXML
    private TextField password_textField;

    private Family family;

    private Sex parentSex;

    private boolean isFirstTime;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeWindowDraggable(topBar);
        email_listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        phone_listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void initData(String parent, Family family, boolean isFirstTime) {
        this.isFirstTime = isFirstTime;
        this.family = family;
        if (parent.equals("PAI")) {
            windowTitle.setText("Criar Pai");
            header.setText("Insira os dados do Pai");
            parentSex = Sex.MASCULINO;
        } else {
            windowTitle.setText("Criar Mãe");
            header.setText("Insira os dados da Mãe");
            parentSex = Sex.FEMININO;
        }
    }

    @FXML
    void addEmail(ActionEvent event) {
        String email;
        Optional<String> result = buildCustomTextInputDialog("Adicionar Email", "Email");
        if (result.isPresent()) {
            email = result.get();
            if (Validator.isEmail(email) && !existsEmailList(email)) {
                email_listView.getItems().add(email);
            } else {
                AlertManager.showErrorAlert("Email inválido", "Verifique se inseriu correctamente ou se já foi inserido");
            }
        }
    }

    @FXML
    void addPhone(ActionEvent event) {
        String phone;
        Optional<String> result = buildCustomTextInputDialog("Adicionar Nº de telefone", "Nº de telefone");
        if (result.isPresent()) {
            phone = result.get();
            if (Validator.isPhoneNumber(phone) && !existsPhoneList(phone)) {
                phone_listView.getItems().add(phone);
            } else {
                AlertManager.showErrorAlert("Telefone inválido", "Verifique se inseriu correctamente ou se já foi inserido");
            }
        }
    }

    @FXML
    void closeWindow(MouseEvent event) {
        if (closeWindow(topBar, false) && isFirstTime) {
            DatabaseManager.deleteDatabase();
        }
    }

    @FXML
    void nextPage(ActionEvent event) {
        if (!validateForm() || !addParent()) return;
        AlertManager.showSuccessAlert("Dados adicionados", "O parente " + name_textField.getText() + ", foi adicionado com sucesso!");
        closeWindow(topBar, true);
    }

    @FXML
    void removeEmail(ActionEvent event) {
        ObservableList<String> selectedEmails = email_listView.getSelectionModel().getSelectedItems();
        email_listView.getItems().removeAll(selectedEmails);
    }

    @FXML
    void removePhone(ActionEvent event) {
        ObservableList<String> selectedPhones = phone_listView.getSelectionModel().getSelectedItems();
        phone_listView.getItems().removeAll(selectedPhones);
    }

    @Override
    protected boolean validateForm() {
        String name = name_textField.getText();
        LocalDate birthDate = birthDate_date.getValue();
        String username = username_textField.getText();
        String password = password_textField.getText();

        if (Validator.isEmpty(name) || Validator.isEmpty(username) || Validator.isEmpty(password) || birthDate == null) {
            AlertManager.showWarningAlert("Campos Vazios", "Preencha todos os campos obrigatorios");
            return false;
        }

        if (!Validator.isUsernameOrPassword(username) || !Validator.isUsernameOrPassword(password)) {
            AlertManager.showWarningAlert("Username ou password inválidos", "O username e a password devem ter 8 caractéres");
            return false;
        }

        if (birthDate.isAfter(LocalDate.now())) {
            AlertManager.showWarningAlert("Data Inválida", "A data inserida acontece depois da data de hoje");
            return false;
        }

        if ((LocalDate.now().getYear() - birthDate.getYear()) < 18) {
            AlertManager.showWarningAlert("Data Inválida", "O parente deve ter mais de 18 anos");
            return false;
        }
        return true;
    }

    private boolean existsEmailList(String email) {
        return email_listView.getItems().stream().anyMatch(emailListItem -> emailListItem.equals(email));
    }

    private boolean existsPhoneList(String phone) {
        return phone_listView.getItems().stream().anyMatch(phoneListItem -> phoneListItem.equals(phone));
    }

    private boolean phonesExistsDatabase(LinkedList<String> phones) {
        for (String phone : phones) {
            try {
                if (PhoneNumberDAO.getInstance().exists(phone)) {
                    return true;
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                AlertManager.showErrorAlert("Erro na base de dados", "Ocorreu um erro na base de dados. Tente novamente");
                return true;
            }
        }
        return false;
    }

    private boolean emailsExistsDatabase(LinkedList<String> emails) {
        for (String email : emails) {
            try {
                if (EmailDAO.getInstance().exists(email)) {
                    return true;
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                AlertManager.showErrorAlert("Erro na base de dados", "Ocorreu um erro na base de dados. Tente novamente");
                return true;
            }
        }
        return false;
    }

    private boolean userIdExistsDatabase(String username, String password) {
        try {
            return PersonDAO.getInstance().exists(username, password);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            AlertManager.showErrorAlert("Erro na base de dados", "Não foi possível efectuar a operação, tente novamente");
        }
        return false;
    }

    private boolean addParent() {
        LinkedList<String> phones = new LinkedList<>(phone_listView.getItems());
        LinkedList<String> emails = new LinkedList<>(email_listView.getItems());
        if (phonesExistsDatabase(phones)) {
            AlertManager.showWarningAlert("Número de telefone já existente", "Já existe alguém com esse número");
            return false;
        }
        if (emailsExistsDatabase(emails)) {
            AlertManager.showWarningAlert("Email já existente", "Já existe alguém com esse email");
            return false;
        }

        String name = name_textField.getText();
        LocalDate birthDate = birthDate_date.getValue();
        String username = username_textField.getText();
        String password = password_textField.getText();

        if (userIdExistsDatabase(username, password)) {
            AlertManager.showWarningAlert("Dados já existentes", "Já existe uma password ou username igual");
            return false;
        }

        Parent parent = parentSex.equals(Sex.MASCULINO) ? Parent.buildFather(username, name, birthDate, password) : Parent.buildMother(username, name, birthDate, password);
        parent.getEmails().addAll(emails);
        parent.getPhoneNumbers().addAll(phones);
        try {
            PersonDAO.getInstance().add(parent);
            if (parent.getSex().equals(Sex.MASCULINO)) {
                family.setFather(parent);
            } else {
                family.setMother(parent);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            AlertManager.showErrorAlert("Erro na inserção", "Ocorreu um erro na inserção do parente, tente novamente");
            return false;
        }

        if (parentSex.equals(Sex.MASCULINO)) {
            family.setFather(parent);
        } else {
            family.setMother(parent);
        }
        return true;
    }

}
