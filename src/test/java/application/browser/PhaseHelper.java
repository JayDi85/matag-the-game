package application.browser;

import application.AbstractApplicationTest;
import com.matag.player.PlayerType;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class PhaseHelper {
  public static final By PHASE_CSS_SELECTOR = By.cssSelector("#turn-phases .active");
  public static final By PRIORITY_CSS_SELECTOR = By.cssSelector("#turn-phases .active");

  private static final Logger LOGGER = LoggerFactory.getLogger(PhaseHelper.class);

  private MatagBrowser matagBrowser;

  PhaseHelper(MatagBrowser matagBrowser) {
    this.matagBrowser = matagBrowser;
  }

  public void is(String phase, PlayerType priority) {
    matagBrowser.wait(textToBe(PHASE_CSS_SELECTOR, phase));
    isPriority(priority);
  }

  public void isPriority(PlayerType priority) {
    try {
      matagBrowser.wait(attributeContains(PRIORITY_CSS_SELECTOR, "class", getClassNameLinkedToPriority(priority)));
    } catch (Exception e) {
      LOGGER.warn("PhaseAndPriority:" + getPhase() + " " + getPriority());
      throw e;
    }
  }

  public String getPhase() {
    matagBrowser.wait(visibilityOfElementLocated(PHASE_CSS_SELECTOR));
    return matagBrowser.findElement(PHASE_CSS_SELECTOR).getText();
  }

  public PlayerType getPriority() {
    matagBrowser.wait(visibilityOfElementLocated(PRIORITY_CSS_SELECTOR));
    var classes = matagBrowser.findElement(PRIORITY_CSS_SELECTOR).getAttribute("class");
    if (classes.contains("active-for-player")) {
      return PLAYER;
    } else {
      return OPPONENT;
    }
  }

  private String getClassNameLinkedToPriority(PlayerType priority) {
    if (priority == PLAYER) {
      return "active-for-player";
    } else {
      return "active-for-opponent";
    }
  }
}
