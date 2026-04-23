package test;

import domain.booking.Booking;
import domain.booking.BookingStatus;
import domain.resource.Resource;
import domain.resource.Room;
import domain.user.IndividualUser;
import money.Money;
import org.junit.jupiter.api.Test;
import pricing.HappyHoursPricing;
import pricing.PricingPolicy;
import pricing.StandardPricing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class PricingPolicyTest {
    @Test
    void standardPricingCalculateCorrectly() {
        PricingPolicy policy = new StandardPricing();
        Room room = new Room("a", 10, Set.of());
        Booking b = createBooking(room, 120);
        Money price = policy.price(b);
        assertEquals(new BigDecimal("30.00"), price.amount());
    }

    @Test
    void happyHoursPricingAppliesDiscountIfStartInWindow() {
        PricingPolicy policy = new HappyHoursPricing();
        Resource room = new Room("A", 10, Set.of());
        Booking booking = createBooking(room, 60,
                LocalDateTime.of(2026, 4, 23, 14, 30));
        Money price = policy.price(booking);
        assertEquals(new BigDecimal("10.50"), price.amount());
    }

    @Test
    void happyHoursNoDiscountOutsideWindow() {
        PricingPolicy policy = new HappyHoursPricing();
        Resource room = new Room("A", 10, Set.of());
        Booking booking = createBooking(room, 60,
                LocalDateTime.of(2026, 4, 23, 10, 30));
        Money price = policy.price(booking);
        assertEquals(new BigDecimal("15.00"), price.amount());
    }

    private Booking createBooking(Resource r, int minutes) {
        return createBooking(r, minutes, LocalDateTime.now());
    }

    private Booking createBooking(Resource r, int minutes, LocalDateTime start) {
        return new Booking("test", new IndividualUser("a@a.pl", "A"), r,
                start, start.plusMinutes(minutes), BookingStatus.PENDING, null);
    }
}
