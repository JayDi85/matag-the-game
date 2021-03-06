package com.matag.game.deck;

import com.matag.adminentities.DeckInfo;
import com.matag.game.adminclient.AdminClient;
import com.matag.game.security.SecurityToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeckRetrieverService {
  private final AdminClient adminClient;

  public DeckInfo retrieveDeckForUser(SecurityToken token) {
    return adminClient.getDeckInfo(token);
  }
}
