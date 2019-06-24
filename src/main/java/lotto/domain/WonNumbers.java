package lotto.domain;

import lotto.domain.validator.LottoValidator;
import lotto.utils.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WonNumbers {

    private final List<WonNumber> wonNumbers;

    public WonNumbers(String wonNormalNumbersValue, String wonBonusNumberValue) {

        validateInputs(wonNormalNumbersValue, wonBonusNumberValue);

        List<Integer> wonNormalNumbers = LottoParser.parse(wonNormalNumbersValue);
        LottoValidator.validateNumbers(wonNormalNumbers);

        int wonBonusNumber = Integer.parseInt(wonBonusNumberValue);
        LottoValidator.validateNumber(wonBonusNumber);

        this.wonNumbers = createWonNumbers(wonNormalNumbers, wonBonusNumber);
    }

    private List<WonNumber> createWonNumbers(List<Integer> wonNormalNumbers, int wonBonusNumber) {

        return Stream.concat(wonNormalNumbers.stream().map(WonNumber::ofNormalNumber),
                             Stream.of(wonBonusNumber).map(WonNumber::ofBonusNumber))
                .collect(Collectors.toList());
    }

    private void validateInputs(String wonNormalNumbersValue, String wonBonusNumbers) {

        if (StringUtils.isBlank(wonNormalNumbersValue) || StringUtils.isBlank(wonBonusNumbers)) {
            throw new IllegalArgumentException("입력받은 우승번호가 유효하지 않습니다.");
        }
    }

    public List<WonNumber> getNormalNumbers() {

        return wonNumbers.stream().filter(wonNumber -> !wonNumber.isBonusNumber()).collect(Collectors.toList());
    }

    public List<WonNumber> getBonusNumbers() {

        return wonNumbers.stream().filter(WonNumber::isBonusNumber).collect(Collectors.toList());
    }
}
