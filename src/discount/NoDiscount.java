package discount;

import domain.booking.Booking;
import money.Money;

public class NoDiscount implements Discount {
    @Override
    public Money applyDiscount(Money basePrice, Booking booking) {
        return basePrice;
    }
}
