package test;

import domain.booking.Booking;
import domain.booking.BookingStatus;
import domain.resource.Resource;
import domain.resource.Room;
import domain.user.IndividualUser;
import domain.user.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Set;

public class BookingTest {
    @Test
    void shouldCreateBookingWithPendingStatus() {
        User u = new IndividualUser("a@b.com", "U");
        Resource res = new Room("Room1", 5, Set.of());
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(2);
        Booking b = new Booking("BK-1", u, res, start, end, BookingStatus.PENDING, null);
        assertEquals(BookingStatus.PENDING, b.getStatus());
        assertEquals(120, b.durationMinutes());
    }

    @Test
    void shouldThrowWhenEndIsBeforeStart() {
        User u = new IndividualUser("a@b.com", "U");
        Resource res = new Room("Room1", 5, Set.of());
        LocalDateTime start = LocalDateTime.now();
        assertThrows(IllegalArgumentException.class, () ->
                new Booking("bad", u, res, start, start.minusHours(2), BookingStatus.PENDING, null));
    }

    @Test
    void shouldTransitionStatues() {
        User u = new IndividualUser("a@b.com", "U");
        Resource res = new Room("Room1", 5, Set.of());
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(2);
        Booking b = new Booking("BK-1", u, res, start, end, BookingStatus.PENDING, null);

        b.confirm();
        b.complete();
        assertEquals(BookingStatus.COMPLETED, b.getStatus());
    }
}
