import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

public class MonoTest {
  @Test
  void firstMono() {
    Mono.just("A");
  }
  @Test
  void monoWithConsumer() {
    var mono = Mono.just("A")
        .log()
        .doOnSubscribe(subs -> System.out.println("Subscribed: " + subs))
        .doOnRequest(req -> System.out.println("req: " + req))
        .doOnSuccess(suc -> System.out.println("succ: " + suc));

    mono.subscribe(System.out::println);
    mono.subscribe(s -> System.out.println("2nd sub : " + s));
  }
}
