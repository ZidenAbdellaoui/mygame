package it.unicam.cs.mpgc.rpg129774.service;

import it.unicam.cs.mpgc.rpg129774.model.quest.Quest;

import java.util.List;

/**
 * Manages the quest lifecycle: accepting, tracking kills, and rewarding completion.
 */
public interface QuestService {

    List<Quest> getAvailableQuests();

    List<Quest> getActiveQuests();

    void acceptQuest(Quest quest);

    /**
     * Notifies the service that an enemy was killed, updating all active quests.
     *
     * @param enemyId the defeated enemy's id
     * @return list of quests that were completed as a result of this kill
     */
    List<Quest> notifyKill(String enemyId);
}
