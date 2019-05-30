package application;

import application.browser.MtgBrowser;
import org.junit.After;
import org.junit.Before;
import org.springframework.boot.web.server.LocalServerPort;

public class AbstractApplicatonTest {

    @LocalServerPort
    private int port;

    protected MtgBrowser player1;
    protected MtgBrowser player2;

    @Before
    public void setup() {
        // When player1 joins the game is waiting for opponent
        player1 = new MtgBrowser(port);
        player1.getMessageHelper().hasMessage("Waiting for opponent...");

        // When player2 joins the game both players see the table with the cards
        player2 = new MtgBrowser(port);

        // Message disappears
        player1.getMessageHelper().hasNoMessage();
        player2.getMessageHelper().hasNoMessage();
    }

    @After
    public void cleanup() {
        player1.close();
        player2.close();
    }
}
