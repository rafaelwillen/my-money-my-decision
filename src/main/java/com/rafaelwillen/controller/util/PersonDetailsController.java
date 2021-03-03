package com.rafaelwillen.controller.util;

import com.rafaelwillen.model.family.Parent;
import com.rafaelwillen.model.family.Person;
import com.rafaelwillen.util.CustomWindow;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class PersonDetailsController extends CustomWindow implements Initializable {
    @FXML
    private AnchorPane topBar;

    @FXML
    private Label name_textField;

    @FXML
    private Label birthDate_label;

    @FXML
    private Label username_label;

    @FXML
    private Label sex_label;

    @FXML
    private Label password_label;

    @FXML
    private ListView<String> phoneNumbers_listView;

    @FXML
    private ListView<String> emails_listView;

    @Override
    protected boolean validateForm() {
        try {
            throw new IllegalAccessException("This method must not be called");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeWindowDraggable(topBar);
    }

    public void initData(Person person, Person userLoggedIn) {
        name_textField.setText(person.getName());
        sex_label.setText(person.getSex().name());
        username_label.setText(person.getUsername());
        birthDate_label.setText(person.getBirthDate().toString());
        emails_listView.getItems().addAll(person.getEmails());
        phoneNumbers_listView.getItems().addAll(person.getPhoneNumbers());

        if (person.equals(userLoggedIn) || userLoggedIn instanceof Parent) {
            password_label.setText(person.getPassword());
        } else {
            password_label.setText(person.getPassword().replaceAll("\\w", "x"));
        }
    }

    @FXML
    void closeWindow(MouseEvent event) {
        closeWindow(topBar, true);
    }
}
