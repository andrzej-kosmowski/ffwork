package test;

import domain.booking.Booking;
import domain.booking.BookingStatus;
import domain.resource.Device;
import domain.resource.Resource;
import domain.resource.Room;
import domain.user.IndividualUser;
import domain.user.User;
import money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pricing.StandardPricing;
import repo.booking.InMemoryBookingRepository;
import service.BookingService;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BookingServiceTest {
    private InMemoryBookingRepository bookingRepo;
    private BookingService service;
    private User user;
    private Resource room;
    @BeforeEach
    void setUp() {
        bookingRepo = new InMemoryBookingRepository();
        service = new BookingService(
                bookingRepo,
                new StandardPricing()
        );

        user = new IndividualUser("test@mail.com", "Jan");
        room = new Room("R1", 10, Set.of("projector"));
    }

    @Test
    void shouldCreateBookingAndSaveToRepo() {
        Booking booking = service.book(
                user,
                room,
                LocalDateTime.of(2026, 4, 4, 10, 0),
                LocalDateTime.of(2026, 4, 4, 12, 0)
        );

        assertNotNull(booking);
        assertEquals(1, bookingRepo.findAll().size());
        assertEquals(BookingStatus.PENDING, booking.getStatus());
    }

    @Test
    void shouldConfirmBooking() {
        Booking booking = service.book(
                user,
                room,
                LocalDateTime.of(2026, 4, 4, 10, 0),
                LocalDateTime.of(2026, 4, 4, 12, 0)
        );

        service.confirm(booking.getId());

        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    }

    @Test
    void shouldPreventDoubleBookingForRoom() {
        service.book(
                user,
                room,
                LocalDateTime.of(2026, 4, 4, 10, 0),
                LocalDateTime.of(2026, 4, 4, 12, 0)
        );

        assertThrows(IllegalStateException.class, () ->
                service.book(
                        user,
                        room,
                        LocalDateTime.of(2026, 4, 4, 10, 0),
                        LocalDateTime.of(2026, 4, 4, 12, 0))
        );
    }

    @Test
    void shouldAllowDeviceOverbookingUpToQuantity() {
        Device device = new Device("D1", Money.of("10.00"), 2);

        service.book(user, device,
                LocalDateTime.of(2026, 4, 4, 10, 0),
                LocalDateTime.of(2026, 4, 4, 12, 0));

        service.book(user, device,
                LocalDateTime.of(2026, 4, 4, 10, 30),
                LocalDateTime.of(2026, 4, 4, 12, 30));

        assertEquals(2, bookingRepo.findAll().size());

        assertThrows(IllegalStateException.class, () ->
                service.book(user, device,
                        LocalDateTime.of(2026, 4, 4, 11, 0),
                        LocalDateTime.of(2026, 4, 4, 12, 0))
        );
    }

    @Test
    void shouldCalculatePriceOnBooking() {
        Booking booking = service.book(
                user,
                room,
                LocalDateTime.of(2026, 4, 4, 10, 0),
                LocalDateTime.of(2026, 4, 4, 12, 0)
        );

        assertEquals(Money.of("30.00"), booking.getCalculatedPrice());
    }
}
