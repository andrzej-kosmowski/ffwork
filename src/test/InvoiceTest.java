package test;

import billing.Invoice;
import domain.booking.Booking;
import domain.booking.BookingStatus;
import domain.resource.Resource;
import domain.resource.Room;
import domain.user.IndividualUser;
import domain.user.User;
import money.Money;
import org.junit.jupiter.api.Test;
import service.BillingService;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.Assert.*;

public class InvoiceTest {
    @Test
    void shouldGenerateInvoice() {
        BillingService service = new BillingService();
        User user = new IndividualUser("a@b.com", "Jan");
        Resource room = new Room("Room A", 10, Set.of());
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(2);
        Booking booking = new Booking("BK-1", user, room, start, end, BookingStatus.CONFIRMED, null);

        booking.setCalculatedPrice(Money.of("100.00"));
        Invoice invoice = service.toInvoice(booking);
        assertTrue(invoice.invoiceNumber().startsWith("INV-"));
        assertEquals(user, invoice.buyer());
        assertEquals(Money.of("100.00"), invoice.total());
        assertTrue(invoice.itemDescription().contains("Room A"));
    }
}
