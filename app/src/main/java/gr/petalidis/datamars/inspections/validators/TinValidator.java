package gr.petalidis.datamars.inspections.validators;

import java.util.stream.IntStream;

public class TinValidator implements Validator<String> {
    @Override
    public boolean isValid(String tin) {

        if (tin.length() < 9) {
            return false;
        }
        Double iSum = IntStream.range(0, tin.length() - 1).mapToDouble(i -> Integer.parseInt(tin.substring(i, i + 1))
                * (int) Math.pow(2, (tin.length() - i - 1))).sum();
        if (iSum == 0) {
            return false;
        }

        Double btRem = iSum % 11;

        int lastDigit = Integer.parseInt(tin.substring(tin.length() - 1, tin.length()));
        if (lastDigit == btRem || (btRem == 10 && lastDigit == 0)) {
            return true;
        }
        return false;
    }
}
