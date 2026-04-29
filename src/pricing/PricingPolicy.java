package pricing;

import domain.booking.Booking;
import money.Money;

public interface PricingPolicy {
    Money price (Booking booking);
}
