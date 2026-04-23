package money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal amount) implements Comparable<Money> {

    public Money {
        Objects.requireNonNull(amount);
        if (amount.signum() < 0)
            throw new IllegalArgumentException("negative amount");
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static Money of(String value) {
        return new Money(new BigDecimal(value));
    }

    public static Money of(double value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other) {
        return new Money(this.amount.subtract(other.amount));
    }

    public Money multiply(BigDecimal m) {
        return new Money(this.amount.multiply(m));
    }

    public Money multiply(double m) {
        return multiply(BigDecimal.valueOf(m));
    }

    @Override
    public String toString() {
        return amount + " PLN";
    }

    @Override
    public int compareTo(Money o) {
        return this.amount.compareTo(o.amount);
    }
}
