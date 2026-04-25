package test;

import domain.booking.Booking;
import domain.booking.BookingStatus;
import domain.resource.Desk;
import domain.resource.DeskType;
import domain.resource.Resource;
import domain.resource.Room;
import domain.user.IndividualUser;
import domain.user.User;
import money.Money;
import org.junit.jupiter.api.Test;
import pricing.HappyHoursPricing;
import pricing.PricingPolicy;
import pricing.StandardPricing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PricingPolicyTest {
    private final HappyHoursPricing pricing = new HappyHoursPricing();
    private Booking createBooking(int startHour, int endHour) {
        User user = new IndividualUser("test@mail.com", "Test", null);
        Resource resource = new Desk("D1", Money.of("60"), DeskType.HOT);

        return new Booking(
                "1",
                user,
                resource,
                LocalDateTime.of(2026, 4, 4, startHour, 0),
                LocalDateTime.of(2026, 4, 4, endHour, 0),
                BookingStatus.PENDING,
                null
        );
    }

    @Test
    void shouldCalculateNormalPrice() {
        Booking booking = createBooking(10, 12);
        Money price = pricing.price(booking);
        assertEquals(Money.of("120.00"), price);
    }

    @Test
    void shouldApplyDiscountWhenFullyInHH() {
        Booking booking = createBooking(14, 16);
        Money price = pricing.price(booking);
        assertEquals(Money.of("84.00"), price);
    }

    @Test
    void shouldApplyPartialDiscount() {
        Booking booking = createBooking(13, 17);
        Money price = pricing.price(booking);
        assertEquals(Money.of("204.00"), price);
    }

    @Test
    void shouldApplyDiscountOnlyForEndPart() {
        Booking booking = createBooking(13, 15);
        Money price = pricing.price(booking);
        assertEquals(Money.of("102.00"), price);
    }

    @Test
    void shouldApplyDiscountOnlyForStartingPart() {
        Booking booking = createBooking(15, 17);
        Money price = pricing.price(booking);
        assertEquals(Money.of("102.00"), price);
    }
}
