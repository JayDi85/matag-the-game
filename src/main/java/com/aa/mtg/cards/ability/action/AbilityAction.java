package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.game.status.GameStatus;

public interface AbilityAction {
    void perform(Ability ability, CardInstance cardInstance, GameStatus gameStatus);
}