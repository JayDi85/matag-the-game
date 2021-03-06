package application.ability;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import com.matag.cards.Cards;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static application.browser.BattlefieldHelper.*;
import static com.matag.game.turn.phases.combat.BeginCombatPhase.BC;
import static com.matag.game.turn.phases.combat.DeclareAttackersPhase.DA;
import static com.matag.game.turn.phases.combat.DeclareBlockersPhase.DB;
import static com.matag.game.turn.phases.main1.Main1Phase.M1;
import static com.matag.player.PlayerType.PLAYER;

public class ActivatedAbilityOnCreatureTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new ActivatedAbilityOnCreatureTest.InitTestServiceForTest());
  }

  @Test
  public void activatedAbilityOnCreature() {
    // Playing jousting dummy
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 1).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Jousting Dummy")).click();
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // When increasing jousting dummy (as well on summoning sickness creature)
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 2).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 3).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 4).tap();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Jousting Dummy"), 1).click();

    var secondJoustingDummyId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Jousting Dummy"), 1).getCardIdNumeric();

    // then ability goes on the stack
    browser.player1().getStackHelper().containsAbility("Player1's Jousting Dummy (" + secondJoustingDummyId + "): Gets +1/+0 until end of turn.");

    // opponent accepts the ability
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // power of jousting dummy is increased
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Jousting Dummy"), 1).hasSummoningSickness();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Jousting Dummy"), 1).hasPowerAndToughness("3/1");

    // move at AfterBlocking phase
    browser.player1().getActionHelper().clickContinueAndExpectPhase(BC, PLAYER);
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DA, PLAYER);
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Jousting Dummy")).declareAsAttacker();
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DA, PLAYER);
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DB, PLAYER);

    // check can increase jousting dummy at instant speed
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 5).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 6).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 7).tap();
    browser.player1().getBattlefieldHelper(PLAYER, COMBAT_LINE).getCard(cards.get("Jousting Dummy"), 0).click();

    var firstJoustingDummyId = browser.player1().getBattlefieldHelper(PLAYER, COMBAT_LINE).getCard(cards.get("Jousting Dummy"), 0).getCardIdNumeric();

    // then ability goes on the stack
    browser.player1().getStackHelper().containsAbility("Player1's Jousting Dummy (" + firstJoustingDummyId + "): Gets +1/+0 until end of turn.");

    // opponent accepts the ability
    browser.player2().getActionHelper().clickContinueAndExpectPhase(DB, PLAYER);

    // power of jousting dummy is increased
    browser.player1().getBattlefieldHelper(PLAYER, COMBAT_LINE).getCard(cards.get("Jousting Dummy"), 0).doesNotHaveSummoningSickness();
    browser.player1().getBattlefieldHelper(PLAYER, COMBAT_LINE).getCard(cards.get("Jousting Dummy"), 0).hasPowerAndToughness("3/1");
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerHand(gameStatus, cards.get("Jousting Dummy"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Jousting Dummy"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
    }
  }
}
