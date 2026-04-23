package pricing;

import domain.booking.Booking;
import money.Money;

public class HappyHoursPricing implements PricingPolicy {
    private static final int HAPPY_HOURS_START = 14;
    private static final int HAPPY_HOURS_END = 16;
    private static final double DISCOUNT = 0.30;
    @Override
    public Money price(Booking booking) {
        Money base = StandardPricing.calcBasePrice(booking);
        int startHour = booking.getStart().getHour();
        boolean isHappyHour = startHour >= HAPPY_HOURS_START && startHour < HAPPY_HOURS_END;

        if (isHappyHour) {
            return base.multiply(1.0 - DISCOUNT);
        }

        return base;
    }
}
