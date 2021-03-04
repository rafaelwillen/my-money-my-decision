package com.rafaelwillen.controller.dashboard;

import com.rafaelwillen.model.family.Family;
import com.rafaelwillen.model.family.Person;
import com.rafaelwillen.util.CustomWindow;
import com.rafaelwillen.util.RoutesConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardController extends CustomWindow implements Initializable {

    private final static String VIEW_KEY = "view";
    private final static String CONTROLLER_KEY = "controller";
    @FXML
    private BorderPane mainPane;
    @FXML
    private AnchorPane topBar;
    @FXML
    private Label windowTitle_label;
    @FXML
    private VBox menuButton_vBox;
    private Person userLoggedIn;
    private Family family;
    private HashMap<String, Object> page;

    public void initData(Person userLoggedIn, Family family) {
        this.family = family;
        this.userLoggedIn = userLoggedIn;
        showHomeScreen(null);
        windowTitle_label.setText(String.join(" - ", "My Money My Decision", family.getName()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeWindowDraggable(topBar);
    }

    @FXML
    void closeWindow(MouseEvent event) {
        closeWindow(topBar, false);
    }

    @FXML
    void logout(ActionEvent event) {
        if (confirmLogout()) {
            closeWindow(topBar, true);
            showLoginWindow();
        }
    }

    @FXML
    void minimizeWindow(MouseEvent event) {
        minimizeWindow(topBar);
    }

    @FXML
    void showConsultScreen(ActionEvent event) {
        setDefaultStyle();
        setSelectedButton(4);
        page = getPage(RoutesConstants.CONSULTS_SCREEN_FXML);
        mainPane.setCenter((Pane) page.get(VIEW_KEY));
    }

    @FXML
    void showCostsScreen(ActionEvent event) {
        setDefaultStyle();
        setSelectedButton(3);
        page = getPage(RoutesConstants.COSTS_SCREEN_FXML);
        mainPane.setCenter((Pane) page.get(VIEW_KEY));
    }

    @FXML
    void showFamilyScreen(ActionEvent event) {
        setDefaultStyle();
        setSelectedButton(1);
        page = getPage(RoutesConstants.FAMILY_SCREEN_FXML);
        FamilyController controller = (FamilyController) page.get(CONTROLLER_KEY);
        controller.initData(family, userLoggedIn);
        mainPane.setCenter((Pane) page.get(VIEW_KEY));
    }

    @FXML
    void showHomeScreen(ActionEvent event) {
        setDefaultStyle();
        setSelectedButton(0);
         page = getPage(RoutesConstants.HOME_SCREEN_FXML);
        HomeController controller = (HomeController)  page.get(CONTROLLER_KEY);
        controller.initData(userLoggedIn, family);
        mainPane.setCenter((Pane) page.get(VIEW_KEY));
    }

    @FXML
    void showPrevisionScreen(ActionEvent event) {
        setDefaultStyle();
        setSelectedButton(2);
        page = getPage(RoutesConstants.PREVISION_SCREEN_FXML);
        PrevisionController controller = (PrevisionController) page.get(CONTROLLER_KEY);
        controller.initData(family, userLoggedIn);
        mainPane.setCenter((Pane) page.get(VIEW_KEY));
    }

    @Override
    protected boolean validateForm() {
        try {
            throw new IllegalAccessException("Must not be called here");
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    private void setSelectedButton(int i) {
        menuButton_vBox.getChildren().get(i).getStyleClass().clear();
        menuButton_vBox.getChildren().get(i).getStyleClass().add("selected-side");
    }

    private void setDefaultStyle() {
        for (Node node : menuButton_vBox.getChildren()) {
            Button button = (Button) node;
            button.getStyleClass().clear();
            button.getStyleClass().add("menu-button");
        }
    }

    private void showLoginWindow() {
        try {
            Parent root;
            root = FXMLLoader.load(getClass().getResource(RoutesConstants.LOGIN_FXML));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setTitle("Login");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private boolean confirmLogout() {
        Alert alertLogout = new Alert(Alert.AlertType.CONFIRMATION);
        alertLogout.setTitle("Finalizar Sessão");
        alertLogout.setHeaderText("Finalizar Sessão?");
        alertLogout.setContentText("Tem a certeza que deseja finalizar sessão?");
        ButtonType no = new ButtonType("Não", ButtonBar.ButtonData.NO);
        ButtonType yes = new ButtonType("Sim", ButtonBar.ButtonData.YES);
        alertLogout.getButtonTypes().clear();
        alertLogout.getButtonTypes().addAll(yes, no);
        Optional<ButtonType> resposta = alertLogout.showAndWait();
        return resposta.isPresent() && resposta.get().getText().equals("Sim");
    }

    private HashMap<String, Object> getPage(String windowPath) {
        HashMap<String, Object> returnValue = new HashMap<>();
        Pane selectedView;
        Initializable controller;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(windowPath));
        try {
            selectedView = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
        returnValue.put(VIEW_KEY, selectedView);
        returnValue.put(CONTROLLER_KEY, controller);
        return returnValue;
    }
}
