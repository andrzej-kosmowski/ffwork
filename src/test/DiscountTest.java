package test;

import discount.CompanyTierDiscount;
import discount.Discount;
import discount.NoDiscount;
import discount.StudentDiscount;
import domain.booking.Booking;
import domain.booking.BookingStatus;
import domain.resource.Desk;
import domain.resource.DeskType;
import domain.resource.Resource;
import domain.user.CompanyUser;
import domain.user.IndividualUser;
import domain.user.User;
import money.Money;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscountTest {
    private Booking createBooking() {
        User user = new IndividualUser("test@gmail.com", "Test", null);
        Resource resource = new Desk("D1", Money.of("60"), DeskType.HOT);

        return new Booking(
                "1",
                user,
                resource,
                LocalDateTime.of(2026, 4, 4, 10, 0),
                LocalDateTime.of(2026, 4, 4, 12, 0),
                BookingStatus.PENDING,
                null
        );
    }

    @Test
    void shouldReturnSamePrice_NoDiscount() {
        Discount discount = new NoDiscount();
        Money result = discount.applyDiscount(Money.of("100.00"), createBooking());

        assertEquals(Money.of("100.00"), result);
    }

    @Test
    void shouldApplyTwentyPercent_StudentDiscount() {
        Discount discount = new StudentDiscount();
        Money result = discount.applyDiscount(Money.of("250.00"), createBooking());

        assertEquals(Money.of("200.00"), result);
    }

    @Test
    void shouldApplyFivePercent_CompanyTierOne() {
        Discount discount = new CompanyTierDiscount(1);
        Money result = discount.applyDiscount(Money.of("100.00"), createBooking());

        assertEquals(Money.of("95.00"), result);
    }

    @Test
    void shouldApplyTenPercent_CompanyTierTwo() {
        Discount discount = new CompanyTierDiscount(2);
        Money result = discount.applyDiscount(Money.of("100.00"), createBooking());

        assertEquals(Money.of("90.00"), result);
    }

    @Test
    void shouldApplyFifteenPercent_CompanyTierThree() {
        Discount discount = new CompanyTierDiscount(3);
        Money result = discount.applyDiscount(Money.of("100.00"), createBooking());

        assertEquals(Money.of("85.00"), result);
    }

    @Test
    void shouldApplyNoDiscount_CompanyUnknownTier() {
        Discount discount = new CompanyTierDiscount(999);
        Money result = discount.applyDiscount(Money.of("100.00"), createBooking());

        assertEquals(Money.of("100.00"), result);
    }

    @Test
    void shouldReturnStudentDiscountWithId() {
        User user = new IndividualUser("test@mail.com", "Test", "123");
        Discount discount = user.getDiscount();
        Money result = discount.applyDiscount(Money.of("100.00"), createBooking());

        assertEquals(Money.of("80.00"), result);
    }

    @Test
    void shouldReturnNoDiscountWithoutId() {
        User user = new IndividualUser("test@mail.com", "Test", null);
        Discount discount = user.getDiscount();
        Money result = discount.applyDiscount(Money.of("100.00"), createBooking());

        assertEquals(Money.of("100.00"), result);
    }

    @Test
    void shouldReturnNoDiscountWithBlankId() {
        User user = new IndividualUser("test@mail.com", "Test", "    ");
        Discount discount = user.getDiscount();
        Money result = discount.applyDiscount(Money.of("100.00"), createBooking());

        assertEquals(Money.of("100.00"), result);
    }

    @Test
    void shouldReturnCorrectTierDiscount() {
        User user = new CompanyUser("test@mail.com", "Test", "Test", "123", 2);
        Discount discount = user.getDiscount();
        Money result = discount.applyDiscount(Money.of("100.00"), createBooking());

        assertEquals(Money.of("90.00"), result);
    }
}
