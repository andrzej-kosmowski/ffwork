package discount;

import domain.booking.Booking;
import money.Money;

import java.math.BigDecimal;

public class StudentDiscount implements Discount {
    private static final BigDecimal MULTIPLIER = BigDecimal.valueOf(0.8);
    @Override
    public Money applyDiscount(Money basePrice, Booking booking) {
        return basePrice.multiply(MULTIPLIER);
    }
}
