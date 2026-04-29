package discount;

import domain.booking.Booking;
import money.Money;

public interface Discount {
    Money applyDiscount(Money basePrice, Booking booking);
}
