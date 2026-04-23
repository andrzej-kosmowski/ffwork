package domain.pricing;

import domain.booking.Booking;
import money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StandardPricing implements PricingPolicy {

    @Override
    public Money price(Booking booking) {
        BigDecimal hourlyRate = booking.getResource().hourlyRate().amount();
        BigDecimal pricePerMinute = hourlyRate.divide(BigDecimal.valueOf(60), 10, RoundingMode.HALF_UP);
        BigDecimal total = pricePerMinute.multiply(
                BigDecimal.valueOf(booking.durationMinutes())
        );

        return new Money(total);
    }
}
