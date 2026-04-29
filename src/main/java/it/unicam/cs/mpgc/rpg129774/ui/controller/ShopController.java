package it.unicam.cs.mpgc.rpg129774.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unicam.cs.mpgc.rpg129774.model.character.Hero;
import it.unicam.cs.mpgc.rpg129774.model.item.Armor;
import it.unicam.cs.mpgc.rpg129774.model.item.Item;
import it.unicam.cs.mpgc.rpg129774.model.item.Potion;
import it.unicam.cs.mpgc.rpg129774.model.item.Weapon;
import it.unicam.cs.mpgc.rpg129774.service.GameService;
import it.unicam.cs.mpgc.rpg129774.ui.util.SceneManager;
import it.unicam.cs.mpgc.rpg129774.ui.util.ServiceAware;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Handles the Town Shop where the player can buy items using gold.
 */
public class ShopController implements ServiceAware {

    private GameService gameService;
    private final List<Item> catalog = new ArrayList<>();

    @FXML private Label goldLabel;
    @FXML private ListView<String> shopList;
    @FXML private Label detailLabel;

    @Override
    public void setGameService(Object gameService) {
        this.gameService = (GameService) gameService;
    }

    @FXML
    public void initialize() {
        loadCatalog();
        if (gameService != null) refreshView();
        
        shopList.getSelectionModel().selectedIndexProperty().addListener((obs, old, idx) -> {
            if (idx.intValue() >= 0 && idx.intValue() < catalog.size()) {
                Item item = catalog.get(idx.intValue());
                StringBuilder details = new StringBuilder(item.getDescription() + "\n");
                if (item instanceof Weapon w) {
                    details.append(String.format("\nAttack: +%d\nAccuracy: %d%%", w.getAttackBonus(), (int)(w.getAccuracy() * 100)));
                } else if (item instanceof Armor a) {
                    details.append(String.format("\nDefense: +%d", a.getDefenseBonus()));
                } else if (item instanceof Potion p) {
                    details.append(String.format("\nHeal: %d HP\nMana Restore: %d MP", p.getHealAmount(), p.getManaAmount()));
                }
                detailLabel.setText(details.toString());
            }
        });
    }

    private void loadCatalog() {
        try (InputStream is = getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg129774/data/items.json");
             InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(is))) {
             
            JsonArray arr = JsonParser.parseReader(reader).getAsJsonArray();
            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();
                String id = obj.get("id").getAsString();
                String name = obj.get("name").getAsString();
                String desc = obj.get("description").getAsString();
                int gold = obj.get("goldValue").getAsInt();
                String type = obj.get("type").getAsString();
                
                if (type.equals("WEAPON")) {
                    int extra = obj.has("attackBonus") ? obj.get("attackBonus").getAsInt() : 0;
                    double acc = obj.has("accuracy") ? obj.get("accuracy").getAsDouble() : 1.0;
                    catalog.add(new Weapon(id, name, desc, gold, extra, acc));
                } else if (type.equals("ARMOR")) {
                    int extra = obj.has("defenseBonus") ? obj.get("defenseBonus").getAsInt() : 0;
                    catalog.add(new Armor(id, name, desc, gold, extra));
                } else if (type.equals("POTION")) {
                    int hp = obj.has("healAmount") ? obj.get("healAmount").getAsInt() : 0;
                    int mp = obj.has("manaAmount") ? obj.get("manaAmount").getAsInt() : 0;
                    catalog.add(new Potion(id, name, desc, gold, hp, mp));
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load shop catalog.");
        }
    }

    private void refreshView() {
        Hero hero = gameService.getCurrentState().getHero();
        goldLabel.setText("Your Gold: " + hero.getGold());
        
        int selected = shopList.getSelectionModel().getSelectedIndex();
        shopList.getItems().clear();
        for (Item item : catalog) {
            shopList.getItems().add(item.getName() + " - " + item.getGoldValue() + " Gold");
        }
        if (selected >= 0) shopList.getSelectionModel().select(selected);
    }

    @FXML
    private void onBuy() {
        int idx = shopList.getSelectionModel().getSelectedIndex();
        if (idx < 0) return;
        
        Item itemToBuy = catalog.get(idx);
        Hero hero = gameService.getCurrentState().getHero();
        
        if (hero.spendGold(itemToBuy.getGoldValue())) {
            gameService.getInventoryService().addItem(itemToBuy); // They get an independent instance of the item
            showAlert("Item Purchased", "You bought " + itemToBuy.getName() + ".");
            refreshView();
        } else {
            showAlert("Not Enough Gold", "You cannot afford this item.");
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
