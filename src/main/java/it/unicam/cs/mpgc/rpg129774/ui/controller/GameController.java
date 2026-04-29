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
        locationNameLabel.setText(loc.getName());
        locationDescLabel.setText(loc.getDescription());

        // Explore button is visible only if location has enemies
        exploreButton.setVisible(loc.hasEnemies());

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
    private void onInventory() {
        SceneManager.getInstance().switchTo("/it/unicam/cs/mpgc/rpg129774/fxml/inventory.fxml");
    }

    @FXML
    private void onQuests() {
        SceneManager.getInstance().switchTo("/it/unicam/cs/mpgc/rpg129774/fxml/quest.fxml");
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
