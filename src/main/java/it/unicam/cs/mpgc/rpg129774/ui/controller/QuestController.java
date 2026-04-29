package it.unicam.cs.mpgc.rpg129774.ui.controller;

import it.unicam.cs.mpgc.rpg129774.model.quest.Quest;
import it.unicam.cs.mpgc.rpg129774.service.GameService;
import it.unicam.cs.mpgc.rpg129774.service.QuestService;
import it.unicam.cs.mpgc.rpg129774.ui.util.SceneManager;
import it.unicam.cs.mpgc.rpg129774.ui.util.ServiceAware;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 * Controls the Quest Board screen.
 * Displays all available quests (can be accepted) and active quests (currently in progress),
 * with real-time kill-progress shown for the selected active quest.
 */
public class QuestController implements ServiceAware {

    private GameService gameService;
    private QuestService questService;

    @FXML private ListView<String> availableList;
    @FXML private ListView<String> activeList;
    @FXML private Label progressLabel;
    @FXML private Label detailLabel;

    /** Parallel lists so we can look up the Quest object by index. */
    private java.util.List<Quest> availableQuests;
    private java.util.List<Quest> activeQuests;

    @Override
    public void setGameService(Object gameService) {
        this.gameService = (GameService) gameService;
        this.questService = this.gameService.getQuestService();
    }

    @FXML
    public void initialize() {
        if (questService != null) {
            refreshLists();
            // Show quest detail when user clicks on available list
            availableList.getSelectionModel().selectedIndexProperty().addListener(
                    (obs, old, idx) -> showAvailableDetail(idx.intValue()));
            // Show progress when user clicks on active list
            activeList.getSelectionModel().selectedIndexProperty().addListener(
                    (obs, old, idx) -> showActiveProgress(idx.intValue()));
        }
    }

    // ---- Refresh ----

    private void refreshLists() {
        availableQuests = questService.getAvailableQuests();
        activeQuests   = questService.getActiveQuests();

        availableList.getItems().clear();
        availableQuests.forEach(q -> availableList.getItems().add(
                q.getTitle() + "  [+" + q.getXpReward() + " XP, +" + q.getGoldReward() + " G]"));

        activeList.getItems().clear();
        activeQuests.forEach(q -> activeList.getItems().add(
                q.getTitle() + "  — " + q.getProgressText()));

        progressLabel.setText("");
        detailLabel.setText("");
    }

    // ---- Selection listeners ----

    private void showAvailableDetail(int idx) {
        if (idx < 0 || idx >= availableQuests.size()) return;
        Quest q = availableQuests.get(idx);
        detailLabel.setText(q.getDescription());
        progressLabel.setText("");
        activeList.getSelectionModel().clearSelection();
    }

    private void showActiveProgress(int idx) {
        if (idx < 0 || idx >= activeQuests.size()) return;
        Quest q = activeQuests.get(idx);
        detailLabel.setText(q.getDescription());
        progressLabel.setText("Progress: " + q.getProgressText());
        availableList.getSelectionModel().clearSelection();
    }

    // ---- Actions ----

    @FXML
    private void onAccept() {
        int idx = availableList.getSelectionModel().getSelectedIndex();
        if (idx < 0) {
            showAlert("No Quest Selected", "Please select an available quest to accept.");
            return;
        }
        Quest quest = availableQuests.get(idx);
        try {
            questService.acceptQuest(quest);
            refreshLists();
        } catch (IllegalStateException e) {
            showAlert("Quest Limit Reached", e.getMessage());
        }
    }

    @FXML
    private void onBack() {
        SceneManager.getInstance().switchTo("/it/unicam/cs/mpgc/rpg129774/fxml/game.fxml");
    }

    // ---- Helpers ----

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, content, javafx.scene.control.ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
