import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Stream;

public class FluxTest {
  @Test
  void firstFlux() {
    Flux.just("A", "B", "C")
        .log()
        .subscribe();
  }

  @Test
  void iterableFlux() {
    Flux.fromIterable(Arrays.asList("a", "b", "c"))
        .log()
        .subscribe(System.out::println);
  }

  @Test
  void streamFlux() {
    Flux.fromStream(Stream.of("a", "b", "c"))
        .log()
        .subscribe(System.out::println);
  }

  @Test
  void range() {
    Flux.range(10, 20)
        .log()
        .subscribe(System.out::println);
  }

  @Test
  void interval() throws Exception{
    Flux.interval(Duration.ofSeconds(1))
        .log()
        .take(3)
        .subscribe();
    Thread.sleep(5000);
  }
}
