package it.unicam.cs.mpgc.rpg129774.ui.controller;

import it.unicam.cs.mpgc.rpg129774.model.character.CharacterClass;
import it.unicam.cs.mpgc.rpg129774.service.GameService;
import it.unicam.cs.mpgc.rpg129774.ui.util.SceneManager;
import it.unicam.cs.mpgc.rpg129774.ui.util.ServiceAware;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Handles hero name entry and class selection before starting a new game.
 */
public class CharacterCreationController implements ServiceAware {

    private GameService gameService;

    @FXML private TextField nameField;
    @FXML private ToggleGroup classGroup;
    @FXML private Label classDescription;

    @Override
    public void setGameService(Object gameService) {
        this.gameService = (GameService) gameService;
    }

    @FXML
    public void initialize() {
        classGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal instanceof ToggleButton btn) {
                String className = btn.getUserData().toString();
                CharacterClass cc = CharacterClass.valueOf(className);
                classDescription.setText(cc.getDescription());
            }
        });
    }

    @FXML
    private void onCreate() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showAlert("Missing Name", "Please enter a hero name.");
            return;
        }
        Toggle selected = classGroup.getSelectedToggle();
        if (selected == null) {
            showAlert("No Class Selected", "Please select a character class.");
            return;
        }
        CharacterClass cc = CharacterClass.valueOf(selected.getUserData().toString());
        gameService.newGame(name, cc);
        SceneManager.getInstance().switchTo("/it/unicam/cs/mpgc/rpg129774/fxml/game.fxml");
    }

    @FXML
    private void onBack() {
        SceneManager.getInstance().switchTo("/it/unicam/cs/mpgc/rpg129774/fxml/main-menu.fxml");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING, content, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
