package com.rafaelwillen.controller.dashboard;

import com.rafaelwillen.controller.form.create.ParentController;
import com.rafaelwillen.controller.form.create.PetController;
import com.rafaelwillen.controller.form.create.SonController;
import com.rafaelwillen.controller.util.PersonDetailsController;
import com.rafaelwillen.controller.util.PetDetailsController;
import com.rafaelwillen.database.DatabaseManager;
import com.rafaelwillen.database.dao.family.PersonDAO;
import com.rafaelwillen.database.dao.family.PetDao;
import com.rafaelwillen.model.family.*;
import com.rafaelwillen.util.AlertManager;
import com.rafaelwillen.util.CardBuilder;
import com.rafaelwillen.util.RoutesConstants;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class FamilyController implements Initializable {
    @FXML
    private VBox parentsCardContainer;

    @FXML
    private Button addPet_button;

    @FXML
    private Button addSon_button;

    @FXML
    private Label editFamily_button;

    @FXML
    private Label familyName_label;

    @FXML
    private Label province_label;

    @FXML
    private Label district_label;

    @FXML
    private Label address_label;

    @FXML
    private Label phoneNumber_label;

    @FXML
    private Label numberMembers_label;

    @FXML
    private Label numberPets_label;

    @FXML
    private Button deleteFamily_button;

    @FXML
    private VBox sonsContainer;

    @FXML
    private VBox petsContainer;

    private Person userLoggedIn;
    private Family family;
    private boolean isParent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initData(Family family, Person userLoggedIn) {
        this.family = family;
        this.userLoggedIn = userLoggedIn;
        isParent = userLoggedIn instanceof Parent;

        setParentData();
        setFamilyData();
        setSonsData();
        setPetsData();
        if (!isParent) {
            addPet_button.setDisable(true);
            addSon_button.setDisable(true);
            editFamily_button.setDisable(true);
            editFamily_button.setVisible(false);
            deleteFamily_button.setDisable(true);
        }
    }

    @FXML
    void addPet(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RoutesConstants.CREATE_PET_FXML));
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Criar Animal Estimação");
        PetController controller;
        try {
            stage.setScene(new Scene(loader.load()));
            controller = loader.getController();
            controller.initData(family);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        stage.showAndWait();
        numberPets_label.setText(String.valueOf(family.getPets().size()));
        setPetsData();
    }

    @FXML
    void addSon(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RoutesConstants.CREATE_SON_FXML));
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Criar Filho");
        SonController controller;
        try {
            stage.setScene(new Scene(loader.load()));
            controller = loader.getController();
            controller.initData(family);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        stage.showAndWait();
        numberMembers_label.setText(String.valueOf(family.getNumberOfMembers()));
        setSonsData();
    }

    @FXML
    void deleteFamily(ActionEvent event) {
        if (confirmDeleteFamily()) {
            TextInputDialog passwordConfirmation = new TextInputDialog();
            passwordConfirmation.setHeaderText("Verificação de credênciais");
            passwordConfirmation.setContentText("Digite a sua password para eliminar a família");
            passwordConfirmation.initModality(Modality.APPLICATION_MODAL);
            Optional<String> textFieldResult = passwordConfirmation.showAndWait();
            if (textFieldResult.isPresent() && textFieldResult.get().equals(userLoggedIn.getPassword())) {
                DatabaseManager.deleteDatabase();
                System.exit(0);
            } else {
                AlertManager.showWarningAlert("Password Invalida", "A password digitada não está correcta");
            }
        }
    }

    @FXML
    void editFamily(MouseEvent event) {
        // TODO: Make the edit family view
    }

    private boolean confirmDeleteFamily() {
        Alert confirmation = new Alert(Alert.AlertType.WARNING, "Eliminar Família");
        confirmation.setHeaderText("Eliminar Família");
        confirmation.setContentText("Tem a certeza que deseja eliminar esta família? Esta acção é irreversível e apagará todos os dados!");
        ButtonType sim = new ButtonType("Sim", ButtonBar.ButtonData.YES);
        ButtonType nao = new ButtonType("Não", ButtonBar.ButtonData.NO);
        confirmation.getButtonTypes().clear();
        confirmation.getButtonTypes().addAll(sim, nao);
        confirmation.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> result = confirmation.showAndWait();
        return result.isPresent() && result.get().getButtonData().equals(ButtonBar.ButtonData.YES);
    }

    private void setFamilyData() {
        familyName_label.setText(family.getName());
        HashMap<String, String> address = family.getAddress();
        province_label.setText(address.get(Family.PROVINCE_KEY));
        district_label.setText(address.get(Family.DISTRICT_KEY));
        address_label.setText(address.get(Family.STREET_KEY));
        phoneNumber_label.setText(family.getHousePhoneNumber());
        numberMembers_label.setText(String.valueOf(family.getNumberOfMembers()));
        numberPets_label.setText(String.valueOf(family.getPets().size()));
    }

    private void setPetsData() {
        petsContainer.getChildren().clear();
        for (Pet pet : family.getPets()) {
            EventHandler<ActionEvent> deleteEvent = actionEvent -> deletePet(pet);
            EventHandler<ActionEvent> editEvent = actionEvent -> editPet(pet);
            EventHandler<ActionEvent> seeEvent = actionEvent -> showPet(pet);
            HashMap<String, EventHandler<ActionEvent>> events = new HashMap<>();
            events.put("DELETE", deleteEvent);
            events.put("EDIT", editEvent);
            events.put("SEE", seeEvent);
            petsContainer.getChildren().add(CardBuilder.buildPetCard(pet, events));
        }
    }

    private void showPet(Pet pet) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RoutesConstants.PET_DETAILS_FXML));
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setTitle("Detalhes do Animal");
        stage.initModality(Modality.APPLICATION_MODAL);
        try {
            stage.setScene(new Scene(loader.load()));
            PetDetailsController controller = loader.getController();
            controller.initData(pet);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        stage.showAndWait();
    }

    private void editPet(Pet pet) {
        // TODO: Show the edit form for the pet
    }

    private void deletePet(Pet pet) {
        if (!isParent) {
            AlertManager.showWarningAlert("Operação Inválida", "Apenas um pai ou mãe pode realizar esta operação");
            return;
        }
        if (AlertManager.showConfirmationDialog("Eliminar Animal de Estimação", "Tem a certeza que deseja eliminar " + pet.getName() + " ?")) {
            try {
                PetDao.getInstance().delete(pet.getId());
                family.getPets().remove(pet);
                setPetsData();
                numberPets_label.setText(String.valueOf(family.getPets().size()));
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                AlertManager.showErrorAlert("Erro na eliminação", "Ocorreu um erro na eliminação do animal de estimação, tente novamente");
            }
        }
    }

    private void setSonsData() {
        sonsContainer.getChildren().clear();
        for (Son son : family.getSons()) {
            EventHandler<ActionEvent> deleteEvent = actionEvent -> deleteSon(son);
            EventHandler<ActionEvent> editEvent = actionEvent -> editSon(son);
            EventHandler<ActionEvent> seeEvent = actionEvent -> showPersonDetailWindow(son);
            HashMap<String, EventHandler<ActionEvent>> events = new HashMap<>();
            events.put("DELETE", deleteEvent);
            events.put("EDIT", editEvent);
            events.put("SEE", seeEvent);
            sonsContainer.getChildren().add(CardBuilder.buildSonCard(son, events));
        }
    }

    private void editSon(Son son) {
        // TODO: Show the edit form for the son
    }

    private void deleteSon(Son son) {
        if (!isParent) {
            AlertManager.showWarningAlert("Operação Inválida", "Apenas um pai ou mãe pode realizar esta operação");
            return;
        }
        if (AlertManager.showConfirmationDialog("Eliminar Filho", "Tem a certeza que deseja eliminar " + son.getName() + " ?")) {
            try {
                PersonDAO.getInstance().delete(son.getUsername());
                family.getSons().remove(son);
                setSonsData();
                numberMembers_label.setText(String.valueOf(family.getNumberOfMembers()));
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                AlertManager.showErrorAlert("Erro na eliminação", "Ocorreu um erro na eliminação do filho, tente novamente");
            }
        }
    }

    private void setParentData() {
        Parent father = family.getFather();
        Parent mother = family.getMother();
        HashMap<String, EventHandler<ActionEvent>> fatherHandlers = buildEventHandlers(father);
        HashMap<String, EventHandler<ActionEvent>> motherHandlers = buildEventHandlers(mother);

        Pane fatherContainer = buildParentContainer(father);
        Pane motherContainer = buildParentContainer(mother);

        if (fatherContainer instanceof GridPane) {
            setEventsToContainer(fatherContainer, fatherHandlers);
        }

        if (motherContainer instanceof GridPane) {
            setEventsToContainer(motherContainer, motherHandlers);
        }

        setParentsCardContainer(fatherContainer, motherContainer);
    }

    private void setParentsCardContainer(Pane fatherContainer, Pane motherContainer) {
        Node firstChild = parentsCardContainer.getChildren().get(0);
        if (firstChild.getId() != null && firstChild.getId().equals("buttonsRow")) {
            parentsCardContainer.getChildren().clear();
            parentsCardContainer.getChildren().addAll(fatherContainer, motherContainer, firstChild);
        } else {
            parentsCardContainer.getChildren().set(0, fatherContainer);
            parentsCardContainer.getChildren().set(1, motherContainer);
        }
    }

    private void setEventsToContainer(Pane container, HashMap<String, EventHandler<ActionEvent>> handlers) {
        HashMap<String, MenuItem> menuItemMap = CardBuilder.getMenuItems(container);
        menuItemMap.forEach((s, menuItem) -> menuItem.addEventHandler(ActionEvent.ACTION, handlers.get(s)));
    }

    private HashMap<String, EventHandler<ActionEvent>> buildEventHandlers(Parent parent) {
        return new HashMap<>(Map.of(
                "DELETE", mouseEvent -> deleteParent(parent),
                "EDIT", mouseEvent -> editParent(parent),
                "SEE", mouseEvent -> showPersonDetailWindow(parent)
        ));
    }

    private void showPersonDetailWindow(Person person) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RoutesConstants.PERSON_DETAILS_FXML));
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setTitle("Detalhes da Pessoa");
        stage.initModality(Modality.APPLICATION_MODAL);
        try {
            stage.setScene(new Scene(loader.load()));
            PersonDetailsController controller = loader.getController();
            controller.initData(person, userLoggedIn);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        stage.showAndWait();
    }

    private void editParent(Parent parent) {
        // TODO: Show the edit form for the parent
    }

    private void deleteParent(Parent parent) {
        if (!isParent){
            AlertManager.showWarningAlert("Operação Inválida", "Apenas um pai ou mãe pode realizar esta operação");
            return;
        }
        if (parent.equals(userLoggedIn)){
            AlertManager.showWarningAlert("Operação Negada", "Não é possível remover o mesmo utilizador");
            return;
        }
        if (AlertManager.showConfirmationDialog("Eliminar Parente", "Tem a certeza que deseja eliminar " + parent.getName() + " ?")){
            try {
                PersonDAO.getInstance().delete(parent.getUsername());
                if (parent.getSex().equals(Sex.MASCULINO)){
                    family.setFather(null);
                } else {
                    family.setMother(null);
                }
                setParentData();
                numberMembers_label.setText(String.valueOf(family.getNumberOfMembers()));
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private Pane buildParentContainer(Parent parent) {
        EventHandler<MouseEvent> addParentHandler = mouseEvent -> addParent();
        return parent == null ? CardBuilder.buildAddParentCard(addParentHandler) : CardBuilder.buildParentCard(parent);
    }

    private void addParent() {
        if (!isParent){
            AlertManager.showWarningAlert("Operação Inválida", "Apenas um pai ou mãe pode realizar esta operação");
            return;
        }
        boolean isFather = userLoggedIn.getSex().equals(Sex.MASCULINO);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RoutesConstants.CREATE_PARENT_FXML));
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setTitle("Criar Parente");
        stage.initModality(Modality.APPLICATION_MODAL);
        try {
            stage.setScene(new Scene(loader.load()));
            ParentController controller = loader.getController();
            controller.initData(isFather ? "MAE" : "PAI", family, false);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        stage.showAndWait();
        numberMembers_label.setText(String.valueOf(family.getNumberOfMembers()));
        setParentData();
    }

}
