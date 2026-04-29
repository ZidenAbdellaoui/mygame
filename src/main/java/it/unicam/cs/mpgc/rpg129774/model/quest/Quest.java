package it.unicam.cs.mpgc.rpg129774.model.quest;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a quest with objectives, rewards, and lifecycle state.
 * Kill requirements are stored as a map of enemyId → requiredCount,
 * making it trivial to add new objective types in the future.
 */
public class Quest {

    private final String id;
    private final String title;
    private final String description;
    private final int xpReward;
    private final int goldReward;
    private final Map<String, Integer> requiredKills; // enemyId → count
    private final Map<String, Integer> currentKills;  // enemyId → count
    private QuestStatus status;

    public Quest(String id, String title, String description, int xpReward, int goldReward,
                 Map<String, Integer> requiredKills) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.xpReward = xpReward;
        this.goldReward = goldReward;
        this.requiredKills = new HashMap<>(requiredKills);
        this.currentKills = new HashMap<>();
        requiredKills.keySet().forEach(k -> this.currentKills.put(k, 0));
        this.status = QuestStatus.AVAILABLE;
    }

    /**
     * Registers a kill and checks if objectives are now met.
     *
     * @param enemyId the defeated enemy's id
     * @return true if this quest is now complete
     */
    public boolean notifyKill(String enemyId) {
        if (status != QuestStatus.IN_PROGRESS) return false;
        if (!requiredKills.containsKey(enemyId)) return false;

        currentKills.merge(enemyId, 1, Integer::sum);
        if (isObjectiveMet()) {
            status = QuestStatus.COMPLETED;
            return true;
        }
        return false;
    }

    private boolean isObjectiveMet() {
        return requiredKills.entrySet().stream()
                .allMatch(e -> currentKills.getOrDefault(e.getKey(), 0) >= e.getValue());
    }

    public void accept() {
        if (status == QuestStatus.AVAILABLE) status = QuestStatus.IN_PROGRESS;
    }

    public void abandon() {
        if (status == QuestStatus.IN_PROGRESS) {
            status = QuestStatus.AVAILABLE;
            currentKills.replaceAll((k, v) -> 0);
        }
    }

    public String getProgressText() {
        if (requiredKills.isEmpty()) return "No kill objectives.";
        StringBuilder sb = new StringBuilder();
        requiredKills.forEach((id, req) -> {
            int cur = currentKills.getOrDefault(id, 0);
            sb.append(id).append(": ").append(cur).append("/").append(req).append("  ");
        });
        return sb.toString().trim();
    }

    // --- Getters ---

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getXpReward() { return xpReward; }
    public int getGoldReward() { return goldReward; }
    public QuestStatus getStatus() { return status; }
    public Map<String, Integer> getRequiredKills() { return Map.copyOf(requiredKills); }
    public Map<String, Integer> getCurrentKills() { return Map.copyOf(currentKills); }
}
