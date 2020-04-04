package application.stats;

import static org.assertj.core.api.Assertions.assertThat;

import application.AbstractApplicationTest;
import com.matag.admin.stats.StatsResponse;
import java.time.LocalDateTime;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class StatsControllerTest extends AbstractApplicationTest {
  @Test
  public void shouldGetStatsAsUnauthenticatedUser() {
    // When
    ResponseEntity<StatsResponse> response = restTemplate.getForEntity("/stats", StatsResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void shouldGetTotalUsers() {
    // When
    StatsResponse response = restTemplate.getForObject("/stats", StatsResponse.class);

    // Then
    assertThat(response.getTotalUsers()).isEqualTo(2);
  }

  @Test
  public void shouldGetOnlineUsers() {
    // Given
    setCurrentTime(LocalDateTime.parse("2000-01-01T00:00:00"));
    loginUser(USER_2_SESSION_TOKEN);
    setCurrentTime(TEST_START_TIME);

    // When
    StatsResponse response = restTemplate.getForObject("/stats", StatsResponse.class);

    // Then
    assertThat(response.getOnlineUsers()).isEqualTo(1);
  }

  @Test
  public void shouldGetNumOfCards() {
    // When
    StatsResponse response = restTemplate.getForObject("/stats", StatsResponse.class);

    // Then
    assertThat(response.getTotalCards()).isGreaterThan(100);
  }
}