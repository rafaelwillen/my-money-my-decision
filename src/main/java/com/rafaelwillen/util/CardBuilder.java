package com.rafaelwillen.util;

import com.rafaelwillen.model.family.Parent;
import com.rafaelwillen.model.family.Pet;
import com.rafaelwillen.model.family.Sex;
import com.rafaelwillen.model.family.Son;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.HashMap;

public class CardBuilder {

    private final static String IMAGE_PATH = "/com/rafaelwillen/icons/";

    public static GridPane buildParentCard(Parent parent) {
        GridPane parentCard = buildGridPane();
        ImageView imageView;
        Label familyRole;
        if (parent.getSex().equals(Sex.MASCULINO)) {
            imageView = buildImageView("father.png");
            familyRole = buildLabel("Pai");
        } else {
            imageView = buildImageView("mother.png");
            familyRole = buildLabel("Mãe");
        }
        MenuButton actions = buildMenuButton();
        Label parentName = buildLabel(parent.getName());
        Label parentAge = buildLabel(parent.getAge() + " anos");
        parentCard.add(imageView, 0, 0, 1, 2);
        parentCard.add(parentName, 1, 0);
        parentCard.add(familyRole, 1, 1);
        parentCard.add(actions, 2, 0);
        parentCard.add(parentAge, 2, 1);
        return parentCard;
    }

    public static HashMap<String, MenuItem> getMenuItems(Pane container) {
        HashMap<String, MenuItem> menuItems = new HashMap<>();
        MenuButton menuButton = null;

        for (Node node : container.getChildren()) {
            if (node instanceof MenuButton) {
                menuButton = (MenuButton) node;
                break;
            }
        }

        menuItems.put("EDIT", menuButton.getItems().get(0));
        menuItems.put("DELETE", menuButton.getItems().get(1));
        menuItems.put("SEE", menuButton.getItems().get(2));
        return menuItems;
    }

    public static GridPane buildSonCard(Son son, HashMap<String, EventHandler<ActionEvent>> events) {
        GridPane sonCard = buildGridPane();
        ImageView imageView = son.getSex().equals(Sex.MASCULINO) ? buildImageView("boy.png") : buildImageView("girl.png");
        sonCard.getStyleClass().clear();
        sonCard.getStyleClass().add("dark-card");
        MenuButton actions = buildMenuButton();

        actions.getItems().get(0).addEventHandler(ActionEvent.ACTION, events.get("EDIT"));
        actions.getItems().get(1).addEventHandler(ActionEvent.ACTION, events.get("DELETE"));
        actions.getItems().get(2).addEventHandler(ActionEvent.ACTION, events.get("SEE"));
        Label sonName = buildLabel(son.getName());
        Label sonAge = buildLabel(son.getAge() + " anos");
        sonCard.add(imageView, 0, 0, 1, 2);
        sonCard.add(sonName, 1, 0, 1, 2);
        sonCard.add(actions, 2, 0);
        sonCard.add(sonAge, 2, 1);
        return sonCard;
    }

    public static GridPane buildPetCard(Pet pet, HashMap<String, EventHandler<ActionEvent>> events) {
        GridPane petCard = buildGridPane();
        ImageView imageView = buildImageView("animal.png");
        petCard.getStyleClass().clear();
        petCard.getStyleClass().add("dark-card");
        MenuButton actions = buildMenuButton();
        actions.getItems().get(0).addEventHandler(ActionEvent.ACTION, events.get("EDIT"));
        actions.getItems().get(1).addEventHandler(ActionEvent.ACTION, events.get("DELETE"));
        actions.getItems().get(2).addEventHandler(ActionEvent.ACTION, events.get("SEE"));
        Label petName = buildLabel(pet.getName());
        Label petSex = buildLabel("Sexo: " + pet.getSex().name().charAt(0));
        Label petType = buildLabel(pet.getAnimalType());
        petCard.add(imageView, 0, 0, 1, 2);
        petCard.add(petName, 1, 0);
        petCard.add(petType, 1, 1);
        petCard.add(actions, 2, 0);
        petCard.add(petSex, 2, 1);
        return petCard;
    }

    public static HBox buildAddParentCard(EventHandler<MouseEvent> eventHandler) {
        HBox addParentCard = buildHBox();
        addParentCard.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        ImageView imageView = buildImageView("none.png");
        Label label = buildLabel("Clique para adicionar o parente");
        addParentCard.getChildren().addAll(imageView, label);
        return addParentCard;
    }

    private static HBox buildHBox() {
        HBox addParentCard = new HBox(20);
        addParentCard.setPrefHeight(100);
        addParentCard.getStyleClass().add("light-card");
        addParentCard.setPadding(new Insets(18, 15, 18, 15));
        addParentCard.setAlignment(Pos.CENTER_LEFT);
        addParentCard.setCursor(Cursor.HAND);
        return addParentCard;
    }

    private static MenuButton buildMenuButton() {
        MenuButton menuButton = new MenuButton("Acções");
        menuButton.setTooltip(new Tooltip("Acções"));
        menuButton.setStyle("-fx-background-color: #fff");
        menuButton.setPopupSide(Side.RIGHT);
        menuButton.setCursor(Cursor.HAND);
        menuButton.getItems().addAll(
                new MenuItem("Editar"),
                new MenuItem("Remover"),
                new MenuItem("Ver detalhes")
        );
        return menuButton;
    }

    private static Label buildLabel(String text) {
        Label label = new Label(text);
        label.maxHeight(Double.MAX_VALUE);
        return label;
    }

    private static ImageView buildImageView(String imageName) {
        ImageView imageView = new ImageView();
        Image image = new Image(CardBuilder.class.getResource(IMAGE_PATH + imageName).toString());
        imageView.setImage(image);
        imageView.setFitHeight(64);
        imageView.setFitWidth(64);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    private static GridPane buildGridPane() {
        GridPane parentCard = new GridPane();
        parentCard.setHgap(15);
        parentCard.setPrefHeight(100);
        parentCard.getStyleClass().add("light-card");
        parentCard.setPadding(new Insets(18, 15, 18, 15));
        parentCard.getColumnConstraints().addAll(
                new ColumnConstraints(),
                new ColumnConstraints(110),
                new ColumnConstraints()
        );
        parentCard.getRowConstraints().addAll(
                new RowConstraints(),
                new RowConstraints()
        );
        return parentCard;
    }
}
