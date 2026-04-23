package test;

import money.Money;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class MoneyTest {
    @Test
    void shouldCreateMoneyWithGivenAmount() {
        Money m = new Money(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), m.amount());
    }

    @Test
    void shouldCreateFromString() {
        Money m = Money.of("99.99");
        assertEquals(new BigDecimal("99.99"), m.amount());
    }

    @Test
    void shouldAddTwoAmounts() {
        Money a = Money.of("10.00");
        Money b = Money.of("20.00");
        Money sum = a.add(b);
        assertEquals(Money.of("30.00"), sum);
    }

    @Test
    void shouldSubtract() {
        Money a = Money.of("30.00");
        Money b = Money.of("5.00");
        Money diff = a.subtract(b);
        assertEquals(Money.of("25.00"), diff);
    }

    @Test
    void shouldMultiplyByBigDecimal() {
        Money m = Money.of("10.00");
        Money result = m.multiply(new BigDecimal("3"));
        assertEquals(Money.of("30.00"), result);
    }

    @Test
    void shouldRoundHalfUp() {
        Money m = Money.of("10.00").multiply(new BigDecimal("1.005"));
        assertEquals(Money.of("10.05"), m);
    }

    @Test
    void shouldCompareCorrectly() {
        Money small = Money.of("1.00");
        Money big = Money.of("100.00");
        assertTrue(small.compareTo(big) < 0);
        assertTrue(big.compareTo(small) > 0);
        assertEquals(0, small.compareTo(Money.of("1.00")));
    }

    @Test
    void shouldHaveProperToString() {
        assertEquals("100.00 PLN", Money.of("100.00").toString());
    }
}
