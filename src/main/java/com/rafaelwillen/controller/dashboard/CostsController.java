package com.rafaelwillen.controller.dashboard;

import com.rafaelwillen.controller.form.create.CostController;
import com.rafaelwillen.controller.util.FamilyMemberTypeController;
import com.rafaelwillen.controller.util.SelectPrevisionController;
import com.rafaelwillen.model.family.*;
import com.rafaelwillen.model.finance.Cost;
import com.rafaelwillen.model.finance.MonthlyPrevision;
import com.rafaelwillen.util.AlertManager;
import com.rafaelwillen.util.RoutesConstants;
import com.rafaelwillen.util.tableViewModel.CostModel;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class CostsController implements Initializable {

    @FXML
    private ComboBox<Object> member_comboBox;

    @FXML
    private Label totalIndividualCost_label;

    @FXML
    private TableView<CostModel> individualCost_tableView;

    @FXML
    private TableColumn<CostModel, String> iCostName_column;

    @FXML
    private TableColumn<CostModel, LocalDate> iCostCreationDate_column;

    @FXML
    private TableColumn<CostModel, Double> iCostValue_column;

    @FXML
    private TableColumn<CostModel, String> iCostLocal_column;

    @FXML
    private Label totalGeneralCost_label;

    @FXML
    private TableView<CostModel> generalCost_tableView;

    @FXML
    private TableColumn<CostModel, String> gCostName_column;

    @FXML
    private TableColumn<CostModel, LocalDate> gCostDate_column;

    @FXML
    private TableColumn<CostModel, Double> gCostValue_column;

    @FXML
    private TableColumn<CostModel, String> gCostLocal_column;
    private Family family;
    private Person userLoggedIn;
    private boolean isParent;
    private LinkedList<MonthlyPrevision> previsions;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initData(Family family, Person userLoggedIn, LinkedList<MonthlyPrevision> previsions){
        this.family = family;
        this.userLoggedIn = userLoggedIn;
        this.previsions = previsions;
        isParent = userLoggedIn instanceof Parent;
    }

    @FXML
    void addGeneralCost(MouseEvent event) {
        if (previsions.size() == 0){
            AlertManager.showWarningAlert("Operação Negada", "Não é possível adicionar gasto sem um previsão mensal do mês actual");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RoutesConstants.CREATE_COST_FXML));
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Criar Gasto Geral");
        MonthlyPrevision prevision = showSelectPrevision();
        if (prevision == null) return;
        try {
            stage.setScene(new Scene(loader.load()));
            CostController controller = loader.getController();
            controller.initData(prevision, family);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.showAndWait();
    }

    @FXML
    void addIndividualCost(MouseEvent event) {
        if (previsions.size() == 0){
            AlertManager.showWarningAlert("Operação Negada", "Não é possível adicionar gasto sem um previsão mensal do mês actual");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RoutesConstants.CREATE_COST_FXML));
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Criar Gasto Individual");
        CostController controller;
        try {
            stage.setScene(new Scene(loader.load()));
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        MonthlyPrevision prevision = showSelectPrevision();
        if (prevision == null) return;
        switch (showFamilyMemberTypeForm()){
            case "PARENTE":
                controller.initData(prevision, new Parent[]{family.getMother(), family.getFather()});
                break;
            case "FILHO":
                controller.initData(prevision, family.getSons().toArray(new Son[0]));
                break;
            case "ANIMAL":
                controller.initData(prevision, family.getPets().toArray(new Pet[0]));
                break;
            default:
                return;
        }
        stage.showAndWait();
    }

    private MonthlyPrevision showSelectPrevision() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RoutesConstants.SELECT_PREVISION_FXML));
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("My Money My Decision");
        SelectPrevisionController controller;
        try {
            stage.setScene(new Scene(loader.load()));
            controller = loader.getController();
            controller.initData(previsions);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        stage.showAndWait();
        if (!controller.canContinue()) return null;
        return controller.getSelectedPrevision();
    }

    private String showFamilyMemberTypeForm(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RoutesConstants.FAMILY_MEMBER_TYPE_FXML));
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("My Money My Decision");
        FamilyMemberTypeController controller;
        try {
            stage.setScene(new Scene(loader.load()));
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        stage.showAndWait();
        return controller.getSelectedType();
    }


}
