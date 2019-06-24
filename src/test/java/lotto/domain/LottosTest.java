package lotto.domain;

import lotto.domain.generator.StubLottoGenerator;
import lotto.domain.generator.StubLottosGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class LottosTest {

    @Test
    @DisplayName("로또 자동/수동 객체 생성 확인")
    void lotto() {

        List<Lotto> stubLottos = Arrays.asList(new Lotto(new StubLottoGenerator(Arrays.asList(40, 41, 42, 43, 44, 45))));
        Lottos lottos = new Lottos(new StubLottosGenerator(stubLottos),
                                   new PurchaseAmount(3 * PurchaseAmount.AMOUNT_PER_LOTTO),
                                   Arrays.asList("1,2,3,4,5,6",
                                                 "7,8,9,10,11,12"));

        assertThat(lottos.getLottos())
                .containsExactlyInAnyOrder(new Lotto(Arrays.asList(40, 41, 42, 43, 44, 45)),
                                           new Lotto(Arrays.asList(1, 2, 3, 4, 5, 6)),
                                           new Lotto(Arrays.asList(7, 8, 9, 10, 11, 12)));
    }

    @ParameterizedTest(name = "당첨 여부 확인. 당첨 Rule={0}")
    @MethodSource
    void countCorrectsByCompareWonNumbers(PrizeRule prizeRule) {

        List<Lotto> stubLottos = Arrays.asList(
                new Lotto(new StubLottoGenerator(Arrays.asList(1, 2, 3, 11, 12, 13))), // 3
                new Lotto(new StubLottoGenerator(Arrays.asList(1, 2, 3, 4, 12, 13))), // 4
                new Lotto(new StubLottoGenerator(Arrays.asList(1, 2, 3, 4, 5, 13))), // 5
                new Lotto(new StubLottoGenerator(Arrays.asList(1, 2, 3, 4, 5, 7))), // 5 + 1
                new Lotto(new StubLottoGenerator(Arrays.asList(1, 2, 3, 4, 5, 6)))); // 6

        Lottos lottos = new Lottos(new StubLottosGenerator(stubLottos), new PurchaseAmount(stubLottos.size() * PurchaseAmount.AMOUNT_PER_LOTTO), Collections.emptyList());

        WonNumbers wonNumbers = new WonNumbers("1, 2, 3, 4, 5, 6", "7");
        assertThat(lottos.isMatchPrizeRule(prizeRule, wonNumbers)).isEqualTo(1);
    }

    private static Stream<Arguments> countCorrectsByCompareWonNumbers() {

        return Stream.of(
                Arrays.stream(PrizeRule.values()).map(Arguments::of).toArray(Arguments[]::new)
        );
    }
}