package it.unicam.cs.mpgc.rpg129774;

import it.unicam.cs.mpgc.rpg129774.service.GameService;
import it.unicam.cs.mpgc.rpg129774.service.GameServiceImpl;
import it.unicam.cs.mpgc.rpg129774.ui.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Application entry point.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Classic Fantasy RPG");
        primaryStage.setWidth(1366);
        primaryStage.setHeight(768);
        primaryStage.setResizable(false);

        // Core dependency graph
        GameService gameService = new GameServiceImpl();

        // Init scene manager and go to main menu
        SceneManager.getInstance().init(primaryStage, gameService);
        SceneManager.getInstance().switchTo("/it/unicam/cs/mpgc/rpg129774/fxml/main-menu.fxml");
    }
}
