package it.unicam.cs.mpgc.rpg129774.ui.controller;

import it.unicam.cs.mpgc.rpg129774.model.character.Enemy;
import it.unicam.cs.mpgc.rpg129774.model.character.Hero;
import it.unicam.cs.mpgc.rpg129774.model.combat.CombatAction;
import it.unicam.cs.mpgc.rpg129774.model.combat.CombatResult;
import it.unicam.cs.mpgc.rpg129774.service.CombatService;
import it.unicam.cs.mpgc.rpg129774.service.GameService;
import it.unicam.cs.mpgc.rpg129774.ui.util.SceneManager;
import it.unicam.cs.mpgc.rpg129774.ui.util.ServiceAware;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;

/**
 * Orchestrates the combat view.
 */
public class CombatController implements ServiceAware {

    private GameService gameService;
    private CombatService combatService;

    @FXML private Label heroName;
    @FXML private ProgressBar heroHpBar;
    @FXML private Label heroHpLabel;
    
    @FXML private Label enemyName;
    @FXML private ProgressBar enemyHpBar;
    @FXML private Label enemyHpLabel;
    
    @FXML private TextArea combatLog;
    @FXML private Button attackBtn;
    @FXML private Button healBtn;
    @FXML private Button fleeBtn;

    @Override
    public void setGameService(Object gameService) {
        this.gameService = (GameService) gameService;
        this.combatService = this.gameService.getCombatService();
    }

    @FXML
    public void initialize() {
        if (combatService != null) refreshView();
    }

    private void refreshView() {
        Hero hero = combatService.getCurrentHero();
        Enemy enemy = combatService.getCurrentEnemy();

        heroName.setText(hero.getDisplayName());
        heroHpLabel.setText(hero.getStats().getHp() + " / " + hero.getStats().getMaxHp());
        heroHpBar.setProgress((double) hero.getStats().getHp() / hero.getStats().getMaxHp());

        enemyName.setText(enemy.getDisplayName());
        enemyHpLabel.setText(enemy.getStats().getHp() + " / " + enemy.getStats().getMaxHp());
        enemyHpBar.setProgress((double) enemy.getStats().getHp() / enemy.getStats().getMaxHp());
    }

    private void appendLog(String msg) {
        combatLog.appendText(msg + "\n");
    }

    @FXML
    private void onAttack() {
        processTurn(CombatAction.ATTACK, null);
    }

    @FXML
    private void onHeal() {
        // Find first available potion in the hero's inventory
        it.unicam.cs.mpgc.rpg129774.model.item.Item potion =
                gameService.getInventoryService().getItems().stream()
                        .filter(i -> i instanceof it.unicam.cs.mpgc.rpg129774.model.item.Potion)
                        .findFirst()
                        .orElse(null);
        if (potion == null) {
            appendLog("You have no potions!");
            return;
        }
        processTurn(CombatAction.USE_ITEM, potion);
    }

    @FXML
    private void onFlee() {
        processTurn(CombatAction.FLEE, null);
    }

    private void processTurn(CombatAction action, it.unicam.cs.mpgc.rpg129774.model.item.Item item) {
        CombatResult result = combatService.executeAction(action, item);
        result.getMessages().forEach(this::appendLog);
        refreshView();

        if (combatService.isCombatOver()) {
            attackBtn.setDisable(true);
            healBtn.setDisable(true);
            fleeBtn.setDisable(true);
            
            if (result.isHeroWon()) {
                // Notify quest service of the kill to update progress
                gameService.getQuestService().notifyKill(combatService.getCurrentEnemy().getId());
            }

            // small delay before returning to town
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
                Platform.runLater(() -> 
                        SceneManager.getInstance().switchTo("/it/unicam/cs/mpgc/rpg129774/fxml/game.fxml"));
            }).start();
        }
    }
}
