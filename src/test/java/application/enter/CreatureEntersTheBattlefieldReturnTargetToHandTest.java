package application.enter;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import com.matag.cards.Cards;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.matag.game.turn.phases.main1.Main1Phase.M1;
import static com.matag.game.turn.phases.main2.Main2Phase.M2;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

public class CreatureEntersTheBattlefieldReturnTargetToHandTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CreatureEntersTheBattlefieldReturnTargetToHandTest.InitTestServiceForTest());
  }

  @Test
  public void creatureEntersTheBattlefieldReturnTargetToHand() {
    // When Exclusion Mage
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Island"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Island"), 1).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Island"), 2).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Exclusion Mage")).click();
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Then Exclusion Mage is on the battlefield and its trigger on the stack
    int exclusionMageId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Exclusion Mage")).getCardIdNumeric();
    browser.player1().getStackHelper().containsAbility("Player1's Exclusion Mage (" + exclusionMageId + "): That targets get returned to its owner's hand.");

    // When player 1 selects opponent creature to return
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Banehound")).target();
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Then it's returned to its owner hand
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).isEmpty();
    browser.player1().getHandHelper(OPPONENT).toHaveSize(1);

    // When playing another Exclusion Mage
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Island"), 3).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Island"), 4).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Island"), 5).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Exclusion Mage")).click();
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Player 1 continue without targets as nothing can be targeted
    browser.player1().getActionHelper().clickContinueAndExpectPhase(M1, OPPONENT);
    browser.player1().getStackHelper().isEmpty();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(cards.get("Exclusion Mage"), cards.get("Exclusion Mage"));

    // Moving to player 1 main phase
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);
    browser.player1().getActionHelper().clickContinueAndExpectPhase(M2, PLAYER);
    browser.player1().getActionHelper().clickContinueAndExpectPhase(M1, OPPONENT);

    // Replaying Banehound
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getFirstCard(cards.get("Swamp")).tap();
    browser.player2().getHandHelper(PLAYER).getFirstCard(cards.get("Banehound")).click();
    browser.player1().getActionHelper().clickContinueAndExpectPhase(M1, OPPONENT);
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(cards.get("Banehound"));
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerHand(gameStatus, cards.get("Exclusion Mage"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Exclusion Mage"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"));

      addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Swamp"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Banehound"));
    }
  }
}
