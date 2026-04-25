package pricing;

import domain.booking.Booking;
import money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

public class HappyHoursPricing implements PricingPolicy {
    private static final int HAPPY_HOURS_START = 14;
    private static final int HAPPY_HOURS_END = 16;
    private static final double DISCOUNT = 0.30;

    @Override
    public Money price(Booking booking) {
        BigDecimal pricePerMinute = pricePerMinute(booking);
        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();

        long totalMinutes = booking.durationMinutes();

        LocalDateTime hhStart = start.withHour(HAPPY_HOURS_START).withMinute(0);
        LocalDateTime hhEnd = start.withHour(HAPPY_HOURS_END).withMinute(0);

        long happyMinutes = overlapMinutes(start, end, hhStart, hhEnd);
        long normalMinutes = totalMinutes - happyMinutes;

        BigDecimal normalPrice = pricePerMinute.multiply(BigDecimal.valueOf(normalMinutes));
        BigDecimal happyPrice = pricePerMinute
                    .multiply(BigDecimal.valueOf(happyMinutes))
                    .multiply(BigDecimal.valueOf(1 - DISCOUNT));

        return new Money(normalPrice.add(happyPrice));
    }

    private BigDecimal pricePerMinute(Booking booking) {
        return booking.getResource()
                .hourlyRate()
                .amount()
                .divide(BigDecimal.valueOf(60), 10, RoundingMode.HALF_UP);
    }

    private long overlapMinutes(LocalDateTime startA, LocalDateTime endA, LocalDateTime hhStart, LocalDateTime hhEnd) {
        LocalDateTime start = startA.isAfter(hhStart) ? startA : hhStart;
        LocalDateTime end = endA.isBefore(hhEnd) ? endA : hhEnd;

        return start.isBefore(end)
                ? Duration.between(start, end).toMinutes()
                : 0;
    }
}