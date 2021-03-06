package com.matag.game.turn.action.finish;

import com.matag.game.adminclient.AdminClient;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FinishGameService {
  private final AdminClient adminClient;

  public void setWinner(GameStatus gameStatus, Player player) {
    gameStatus.getTurn().setWinner(player.getName());
    adminClient.finishGame(gameStatus.getGameId(), player);
  }
}
