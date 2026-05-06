package it.unicam.cs.mpgc.rpg129774.ui.controller;

import it.unicam.cs.mpgc.rpg129774.model.item.Armor;
import it.unicam.cs.mpgc.rpg129774.model.item.Item;
import it.unicam.cs.mpgc.rpg129774.model.item.Potion;
import it.unicam.cs.mpgc.rpg129774.model.item.Weapon;
import it.unicam.cs.mpgc.rpg129774.service.GameService;
import it.unicam.cs.mpgc.rpg129774.service.InventoryService;
import it.unicam.cs.mpgc.rpg129774.ui.util.SceneManager;
import it.unicam.cs.mpgc.rpg129774.ui.util.ServiceAware;
import it.unicam.cs.mpgc.rpg129774.ui.util.StackedItem;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Handles the display and use of items in the hero's inventory.
 */
public class InventoryController implements ServiceAware {

    private GameService gameService;
    private InventoryService inventoryService;

    @FXML private Label equippedWeaponLabel;
    @FXML private Label equippedArmorLabel;
    @FXML private javafx.scene.control.Button unequipWeaponBtn;
    @FXML private javafx.scene.control.Button unequipArmorBtn;
    @FXML private ListView<StackedItem> itemListView;
    @FXML private Label itemStatsLabel;

    @Override
    public void setGameService(Object gameService) {
        this.gameService = (GameService) gameService;
        this.inventoryService = this.gameService.getInventoryService();
    }

    @FXML
    public void initialize() {
        if (inventoryService != null) refreshView();
        
        itemListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Item item = newVal.item();
                itemStatsLabel.setText(item.getDescription() + "\nValue: " + item.getGoldValue() + "G");
            } else {
                itemStatsLabel.setText("");
            }
        });
    }

    private void refreshView() {
        Weapon w = gameService.getCurrentState().getHero().getEquippedWeapon();
        Armor a = gameService.getCurrentState().getHero().getEquippedArmor();

        equippedWeaponLabel.setText(w != null ? w.getName() : "None");
        unequipWeaponBtn.setVisible(w != null);

        equippedArmorLabel.setText(a != null ? a.getName() : "None");
        unequipArmorBtn.setVisible(a != null);

        itemListView.getItems().clear();
        Map<String, List<Item>> grouped = inventoryService.getItems().stream()
                .collect(Collectors.groupingBy(Item::getId));
        
        List<StackedItem> stacked = grouped.values().stream()
                .map(list -> new StackedItem(list.get(0), list.size()))
                .sorted(Comparator.comparing(s -> s.item().getName()))
                .toList();
                
        itemListView.getItems().addAll(stacked);
    }

    @FXML
    private void onUseEquip() {
        StackedItem selectedStack = itemListView.getSelectionModel().getSelectedItem();
        if (selectedStack == null) return;
        Item selected = selectedStack.item();

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
        StackedItem selectedStack = itemListView.getSelectionModel().getSelectedItem();
        if (selectedStack != null) {
            Item selected = selectedStack.item();
            inventoryService.removeItem(selected);
            refreshView();
        }
    }

    @FXML
    private void onUnequipWeapon() {
        gameService.getCurrentState().getHero().unequipWeapon();
        refreshView();
    }

    @FXML
    private void onUnequipArmor() {
        gameService.getCurrentState().getHero().unequipArmor();
        refreshView();
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
