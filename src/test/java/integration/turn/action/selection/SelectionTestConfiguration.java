package integration.turn.action.selection;

import com.matag.game.turn.action.permanent.PermanentGetService;
import com.matag.game.turn.action.player.PlayerGetService;
import integration.TestUtilsConfiguration;
import org.mockito.Mockito;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan("com.matag.game.turn.action.selection")
@Import(TestUtilsConfiguration.class)
public class SelectionTestConfiguration {

  @Bean
  @Primary
  public PermanentGetService permanentService() {
    return Mockito.mock(PermanentGetService.class);
  }

  @Bean
  @Primary
  public PlayerGetService playerGetService() {
    return Mockito.mock(PlayerGetService.class);
  }
}