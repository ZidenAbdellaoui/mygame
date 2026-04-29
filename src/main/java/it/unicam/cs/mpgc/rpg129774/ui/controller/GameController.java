package it.unicam.cs.mpgc.rpg129774.ui.controller;

import it.unicam.cs.mpgc.rpg129774.model.character.Hero;
import it.unicam.cs.mpgc.rpg129774.model.map.Location;
import it.unicam.cs.mpgc.rpg129774.service.GameService;
import it.unicam.cs.mpgc.rpg129774.ui.util.SceneManager;
import it.unicam.cs.mpgc.rpg129774.ui.util.ServiceAware;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Hub controller. Shows hero status and controls navigation between locations.
 */
public class GameController implements ServiceAware {

    private GameService gameService;

    @FXML private Label heroNameLabel;
    @FXML private Label heroStatsLabel;
    @FXML private Label locationNameLabel;
    @FXML private Label locationDescLabel;
    @FXML private VBox travelButtonsContainer;
    @FXML private Button exploreButton;
    @FXML private Button sleepButton;
    @FXML private Button questsButton;
    @FXML private Button shopButton;

    @Override
    public void setGameService(Object gameService) {
        this.gameService = (GameService) gameService;
    }

    @FXML
    public void initialize() {
        if (gameService != null) refreshView();
    }

    private void refreshView() {
        Hero hero = gameService.getCurrentState().getHero();
        heroNameLabel.setText(hero.getDisplayName() + " (Lvl " + hero.getStats().getLevel() + ")");
        heroStatsLabel.setText(String.format("HP: %d/%d  |  Mana: %d/%d  |  ATK: %d  |  DEF: %d  |  Gold: %d\nXP: %d/%d",
                hero.getStats().getHp(), hero.getStats().getMaxHp(),
                hero.getStats().getMana(), hero.getStats().getMaxMana(),
                hero.getEffectiveAttack(), hero.getStats().getDefense(),
                hero.getGold(), hero.getStats().getXp(), hero.getStats().getXpToNextLevel()));

        Location loc = gameService.getCurrentLocation();
        if (loc.getType() == it.unicam.cs.mpgc.rpg129774.model.map.LocationType.TOWN) {
            locationNameLabel.setText(loc.getName() + " — Day " + gameService.getCurrentState().getDayCounter());
            sleepButton.setVisible(true);
            sleepButton.setManaged(true);
            questsButton.setVisible(true);
            questsButton.setManaged(true);
            shopButton.setVisible(true);
            shopButton.setManaged(true);
        } else {
            locationNameLabel.setText(loc.getName());
            sleepButton.setVisible(false);
            sleepButton.setManaged(false);
            questsButton.setVisible(false);
            questsButton.setManaged(false);
            shopButton.setVisible(false);
            shopButton.setManaged(false);
        }
        locationDescLabel.setText(loc.getDescription());

        // Explore button is visible only if location has enemies
        exploreButton.setVisible(loc.hasEnemies());
        exploreButton.setManaged(loc.hasEnemies());

        travelButtonsContainer.getChildren().clear();
        for (Location dest : gameService.getAvailableLocations()) {
            Button btn = new Button("Travel to " + dest.getName());
            btn.getStyleClass().add("nav-btn");
            btn.setOnAction(e -> {
                gameService.travelTo(dest.getId());
                refreshView();
            });
            travelButtonsContainer.getChildren().add(btn);
        }
    }

    @FXML
    private void onExplore() {
        if (gameService.startEncounter().isPresent()) {
            SceneManager.getInstance().switchTo("/it/unicam/cs/mpgc/rpg129774/fxml/combat.fxml");
        } else {
            showAlert("Safe", "There are no monsters here.");
        }
    }

    @FXML
    private void onSleep() {
        Hero hero = gameService.getCurrentState().getHero();
        hero.getStats().heal(hero.getStats().getMaxHp());
        hero.getStats().restoreMana(hero.getStats().getMaxMana());
        gameService.getCurrentState().incrementDay();
        gameService.saveGame();
        showAlert("Rested", "You slept well in the tavern. HP and Mana are fully restored, and the game has been saved. Good morning!");
        refreshView();
    }

    @FXML
    private void onInventory() {
        SceneManager.getInstance().switchTo("/it/unicam/cs/mpgc/rpg129774/fxml/inventory.fxml");
    }

    @FXML
    private void onQuests() {
        SceneManager.getInstance().switchTo("/it/unicam/cs/mpgc/rpg129774/fxml/quest.fxml");
    }

    @FXML
    private void onShop() {
        SceneManager.getInstance().switchTo("/it/unicam/cs/mpgc/rpg129774/fxml/shop.fxml");
    }

    @FXML
    private void onSave() {
        gameService.saveGame();
        showAlert("Game Saved", "Your progress has been saved.");
    }

    @FXML
    private void onMainMenu() {
        SceneManager.getInstance().switchTo("/it/unicam/cs/mpgc/rpg129774/fxml/main-menu.fxml");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, content, javafx.scene.control.ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
