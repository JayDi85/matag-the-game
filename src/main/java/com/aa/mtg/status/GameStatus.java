package com.aa.mtg.status;

import com.aa.mtg.player.Player;
import lombok.Data;

@Data
public class GameStatus {

    private Player player1;
    private Player player2;

}