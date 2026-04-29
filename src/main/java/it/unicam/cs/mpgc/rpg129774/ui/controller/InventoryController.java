package it.unicam.cs.mpgc.rpg129774.ui.controller;

import it.unicam.cs.mpgc.rpg129774.model.item.Armor;
import it.unicam.cs.mpgc.rpg129774.model.item.Item;
import it.unicam.cs.mpgc.rpg129774.model.item.Potion;
import it.unicam.cs.mpgc.rpg129774.model.item.Weapon;
import it.unicam.cs.mpgc.rpg129774.service.GameService;
import it.unicam.cs.mpgc.rpg129774.service.InventoryService;
import it.unicam.cs.mpgc.rpg129774.ui.util.SceneManager;
import it.unicam.cs.mpgc.rpg129774.ui.util.ServiceAware;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 * Handles the display and use of items in the hero's inventory.
 */
public class InventoryController implements ServiceAware {

    private GameService gameService;
    private InventoryService inventoryService;

    @FXML private Label equippedWeaponLabel;
    @FXML private Label equippedArmorLabel;
    @FXML private ListView<Item> itemListView;

    @Override
    public void setGameService(Object gameService) {
        this.gameService = (GameService) gameService;
        this.inventoryService = this.gameService.getInventoryService();
    }

    @FXML
    public void initialize() {
        if (inventoryService != null) refreshView();
    }

    private void refreshView() {
        Weapon w = gameService.getCurrentState().getHero().getEquippedWeapon();
        Armor a = gameService.getCurrentState().getHero().getEquippedArmor();

        equippedWeaponLabel.setText(w != null ? w.getName() : "None");
        equippedArmorLabel.setText(a != null ? a.getName() : "None");

        itemListView.getItems().clear();
        itemListView.getItems().addAll(inventoryService.getItems());
    }

    @FXML
    private void onUseEquip() {
        Item selected = itemListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        inventoryService.removeItem(selected);

        boolean used = false;
        if (selected instanceof Weapon w) {
            inventoryService.equipWeapon(w);
            used = true;
        } else if (selected instanceof Armor a) {
            inventoryService.equipArmor(a);
            used = true;
        } else if (selected instanceof Potion p) {
            used = inventoryService.usePotion(p);
        }

        if (used) {
            refreshView();
        } else {
            // bounce back to inventory if use failed
            inventoryService.addItem(selected);
            showAlert("Cannot Use", "You cannot use this item right now.");
        }
    }

    @FXML
    private void onDrop() {
        Item selected = itemListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            inventoryService.removeItem(selected);
            refreshView();
        }
    }

    @FXML
    private void onBack() {
        SceneManager.getInstance().switchTo("/it/unicam/cs/mpgc/rpg129774/fxml/game.fxml");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, content, javafx.scene.control.ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
