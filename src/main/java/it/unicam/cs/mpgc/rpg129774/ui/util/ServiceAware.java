package it.unicam.cs.mpgc.rpg129774.ui.util;

/**
 * Marker interface for controllers that need access to GameService.
 * SceneManager checks for this and injects the service automatically.
 */
public interface ServiceAware {
    void setGameService(Object gameService);
}
