package it.unicam.cs.mpgc.rpg129774.ui.controller;

import it.unicam.cs.mpgc.rpg129774.service.GameService;
import it.unicam.cs.mpgc.rpg129774.ui.util.SceneManager;
import it.unicam.cs.mpgc.rpg129774.ui.util.ServiceAware;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;

import java.util.Optional;

/**
 * Controls the main menu screen: new game, load game, and exit.
 */
public class MainMenuController implements ServiceAware {

    private GameService gameService;

    @FXML
    private ListView<String> savesList;

    @Override
    public void setGameService(Object gameService) {
        this.gameService = (GameService) gameService;
    }

    @FXML
    public void initialize() {
        // populate save slots when the screen loads
        if (gameService != null) refreshSavesList();
    }

    private void refreshSavesList() {
        savesList.getItems().clear();
        gameService.listSaves().forEach(gs ->
                savesList.getItems().add(gs.getId() + "  |  " + gs.getSaveDate()
                        + "  |  " + gs.getHero().getName()));
    }

    @FXML
    private void onNewGame() {
        SceneManager.getInstance().switchTo("/it/unicam/cs/mpgc/rpg129774/fxml/character-creation.fxml");
    }

    @FXML
    private void onLoadGame() {
        String selected = savesList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Save Selected", "Please select a save file to load.");
            return;
        }
        // extract id (first token before |)
        String saveId = selected.split("\\s*\\|\\s*")[0].trim();
        Optional<it.unicam.cs.mpgc.rpg129774.model.GameState> loaded = gameService.loadGame(saveId);
        if (loaded.isPresent()) {
            SceneManager.getInstance().switchTo("/it/unicam/cs/mpgc/rpg129774/fxml/game.fxml");
        } else {
            showAlert("Load Failed", "Could not load the selected save.");
        }
    }

    @FXML
    private void onExit() {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to quit?",
                ButtonType.YES, ButtonType.NO);
        alert.setTitle("Exit");
        alert.showAndWait().ifPresent(b -> {
            if (b == ButtonType.YES) System.exit(0);
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION, content, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
