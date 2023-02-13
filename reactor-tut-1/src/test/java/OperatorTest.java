import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Flux.range;

public class OperatorTest {
  @Test
  void map() {
    range(1, 5)
        .map(c -> c * 10)
        .subscribe(System.out::println);
  }

  @Test
  void flatMap() {
    range(1, 5)
        .flatMap(c -> range(c * 10, 2))
        .subscribe(System.out::println);
  }

  @Test
  void flatMapMany() {
    just(3)
        .flatMapMany(c -> range(c*10, 2))
        .subscribe(System.out::println);
  }

  @Test
  void concat() throws Exception {
    var oneToFive = range(1, 5)
        .delayElements(Duration.ofSeconds(1));
    var sixToTen = range(6, 7)
        .delayElements(Duration.ofSeconds(2));
//    Flux.concat(oneToFive, sixToTen)
//        .subscribe(System.out::println);

    oneToFive.concatWith(sixToTen)
            .subscribe(System.out::println);

    Thread.sleep(10000);
  }

  @Test
  void merge() throws Exception {
    var oneToFive = range(1, 5)
        .delayElements(Duration.ofSeconds(1));
    var sixToTen = range(6, 7)
        .delayElements(Duration.ofSeconds(2));
    oneToFive.mergeWith(sixToTen)
        .subscribe(System.out::println);
    Thread.sleep(10000);

  }

  @Test
  void zip() throws Exception{
    var oneToFive = range(1, 5)
        .delayElements(Duration.ofSeconds(1));
    var sixToTen = range(6, 7)
        .delayElements(Duration.ofSeconds(2));
    Flux.zip(oneToFive, sixToTen, (item1, item2) -> item1 + " : " + item2)
        .subscribe(System.out::println);

    Thread.sleep(10000);
  }
}
