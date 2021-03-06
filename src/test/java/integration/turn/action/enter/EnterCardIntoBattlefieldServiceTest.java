package integration.turn.action.enter;

import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.turn.action.enter.EnterCardIntoBattlefieldService;
import com.matag.game.turn.action.player.DrawXCardsService;
import integration.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = EnterTestConfiguration.class)
public class EnterCardIntoBattlefieldServiceTest {

  @Autowired
  private EnterCardIntoBattlefieldService enterCardIntoBattlefieldService;

  @Autowired
  private CardInstanceFactory cardInstanceFactory;

  @Autowired
  private TestUtils testUtils;

  @Autowired
  private DrawXCardsService drawXCardsService;

  @Autowired
  private Cards cards;

  @Test
  public void enterTheBattlefield() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var card = cardInstanceFactory.create(gameStatus, 100, cards.get("Swamp"), "player-name");
    card.setController("player-name");

    // When
    enterCardIntoBattlefieldService.enter(gameStatus, card);

    // Then
    assertThat(card.getModifiers().getPermanentId()).isGreaterThan(0);
    assertThat(gameStatus.getPlayer1().getBattlefield().getCards()).contains(card);
  }

  @Test
  public void enterTheBattlefieldOpponent() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var card = cardInstanceFactory.create(gameStatus, 100, cards.get("Swamp"), "opponent-name");
    card.setController("opponent-name");

    // When
    enterCardIntoBattlefieldService.enter(gameStatus, card);

    // Then
    assertThat(card.getModifiers().getPermanentId()).isGreaterThan(0);
    assertThat(gameStatus.getPlayer2().getBattlefield().getCards()).contains(card);
  }

  @Test
  public void enterTheBattlefieldTapped() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var card = cardInstanceFactory.create(gameStatus, 100, cards.get("Diregraf Ghoul"), "player-name");
    card.setController("player-name");

    // When
    enterCardIntoBattlefieldService.enter(gameStatus, card);

    // Then
    card.getModifiers().isTapped();
  }

  @Test
  public void enterTheBattlefieldTrigger() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var card = cardInstanceFactory.create(gameStatus, 100, cards.get("Jadecraft Artisan"), "player-name");
    card.setController("player-name");

    // When
    enterCardIntoBattlefieldService.enter(gameStatus, card);

    // Then
    assertThat(gameStatus.getStack().getItems()).contains(card);
    assertThat(card.getTriggeredAbilities()).hasSize(1);
    assertThat(card.getTriggeredAbilities().get(0).getAbilityTypeText()).isEqualTo("That targets get +2/+2.");
  }

  @Test
  public void enterTheBattlefieldAdamantTriggered() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var card = cardInstanceFactory.create(gameStatus, 100, cards.get("Ardenvale Paladin"), "player-name");
    card.setController("player-name");

    gameStatus.getTurn().setLastManaPaid(Map.of(
      1, singletonList("WHITE"),
      2, singletonList("WHITE"),
      3, singletonList("WHITE"),
      4, singletonList("BLUE")
    ));

    // When
    enterCardIntoBattlefieldService.enter(gameStatus, card);

    // Then
    assertThat(card.getModifiers().getCounters().getPlus1Counters()).isEqualTo(1);
  }

  @Test
  public void enterTheBattlefieldAdamantNotTriggered() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var card = cardInstanceFactory.create(gameStatus, 100, cards.get("Ardenvale Paladin"), "player-name");
    card.setController("player-name");

    gameStatus.getTurn().setLastManaPaid(Map.of(
      1, singletonList("WHITE"),
      2, singletonList("WHITE"),
      3, singletonList("BLUE"),
      4, singletonList("BLUE")
    ));

    // When
    enterCardIntoBattlefieldService.enter(gameStatus, card);

    // Then
    assertThat(card.getModifiers().getCounters().getPlus1Counters()).isEqualTo(0);
  }

  @Test
  public void enterTheBattlefieldAdamantSameTriggered() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var card = cardInstanceFactory.create(gameStatus, 100, cards.get("Clockwork Servant"), "player-name");
    card.setController("player-name");

    gameStatus.getTurn().setLastManaPaid(Map.of(
      1, singletonList("WHITE"),
      2, singletonList("WHITE"),
      3, singletonList("BLUE"),
      4, singletonList("BLUE")
    ));

    // When
    enterCardIntoBattlefieldService.enter(gameStatus, card);

    // Then
    verifyNoMoreInteractions(drawXCardsService);
  }

  @Test
  public void enterTheBattlefieldAdamantSameNotTriggered() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var card = cardInstanceFactory.create(gameStatus, 100, cards.get("Clockwork Servant"), "player-name");
    card.setController("player-name");

    gameStatus.getTurn().setLastManaPaid(Map.of(
      1, singletonList("BLACK"),
      2, singletonList("BLACK"),
      3, singletonList("BLUE"),
      4, singletonList("BLACK")
    ));

    // When
    enterCardIntoBattlefieldService.enter(gameStatus, card);

    // Then
    verify(drawXCardsService).drawXCards(gameStatus.getPlayer1(), 1, gameStatus);
  }
}
