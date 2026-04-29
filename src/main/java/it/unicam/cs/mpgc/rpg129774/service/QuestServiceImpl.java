package it.unicam.cs.mpgc.rpg129774.service;

import it.unicam.cs.mpgc.rpg129774.model.character.Hero;
import it.unicam.cs.mpgc.rpg129774.model.quest.Quest;
import it.unicam.cs.mpgc.rpg129774.model.quest.QuestStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages quest state in memory; the GameService persists completion via GameState.
 */
public class QuestServiceImpl implements QuestService {

    private final List<Quest> allQuests;
    private final Hero hero;

    public QuestServiceImpl(List<Quest> quests, Hero hero) {
        this.allQuests = new ArrayList<>(quests);
        this.hero = hero;
    }

    @Override
    public List<Quest> getAvailableQuests() {
        return allQuests.stream()
                .filter(q -> q.getStatus() == QuestStatus.AVAILABLE)
                .toList();
    }

    @Override
    public List<Quest> getActiveQuests() {
        return allQuests.stream()
                .filter(q -> q.getStatus() == QuestStatus.IN_PROGRESS)
                .toList();
    }

    @Override
    public void acceptQuest(Quest quest) {
        if (!getActiveQuests().isEmpty()) {
            throw new IllegalStateException("You can only have 1 active quest at a time.");
        }
        quest.accept();
    }

    @Override
    public List<Quest> notifyKill(String enemyId) {
        List<Quest> completed = new ArrayList<>();
        for (Quest q : allQuests) {
            if (q.notifyKill(enemyId)) {
                completed.add(q);
                hero.gainXp(q.getXpReward());
                hero.addGold(q.getGoldReward());
            }
        }
        return completed;
    }
}
