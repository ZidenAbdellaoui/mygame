package it.unicam.cs.mpgc.rpg129774.ui.util;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

/**
 * Singleton scene manager. Centralises scene loading and transition logic so that
 * controllers do not depend on each other — they simply call SceneManager.
 */
public class SceneManager {

    private static SceneManager instance;

    private Stage primaryStage;
    private Object injectedService;   // GameService reference passed to controllers

    private SceneManager() {}

    public static SceneManager getInstance() {
        if (instance == null) instance = new SceneManager();
        return instance;
    }

    public void init(Stage stage, Object gameService) {
        this.primaryStage = stage;
        this.injectedService = gameService;
    }

    /**
     * Loads and switches to the given FXML scene with a fade transition.
     *
     * @param fxmlPath resource-relative FXML path (e.g. "/it/.../fxml/main-menu.fxml")
     */
    public void switchTo(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(getClass().getResource(fxmlPath)));

            // Inject service into controller if it implements Initializable with our hook
            loader.setControllerFactory(c -> {
                try {
                    Object controller = c.getDeclaredConstructor().newInstance();
                    if (controller instanceof ServiceAware sa) {
                        sa.setGameService(injectedService);
                    }
                    return controller;
                } catch (Exception e) {
                    throw new RuntimeException("Cannot create controller: " + c, e);
                }
            });

            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource(
                            "/it/unicam/cs/mpgc/rpg129774/css/style.css")).toExternalForm());

            fadeIn(root, () -> primaryStage.setScene(scene));
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load scene: " + fxmlPath, e);
        }
    }

    private void fadeIn(Parent root, Runnable onStart) {
        root.setOpacity(0);
        onStart.run();
        FadeTransition ft = new FadeTransition(Duration.millis(300), root);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    public Stage getPrimaryStage() { return primaryStage; }
    public Object getInjectedService() { return injectedService; }
}
