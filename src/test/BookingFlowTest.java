package test;

import billing.Invoice;
import domain.booking.Booking;
import domain.booking.BookingStatus;
import domain.resource.Desk;
import domain.resource.DeskType;
import domain.resource.Resource;
import domain.user.IndividualUser;
import domain.user.User;
import money.Money;
import org.junit.jupiter.api.Test;
import pricing.StandardPricing;
import repo.booking.InMemoryBookingRepository;
import repo.resource.InMemoryResourceRepository;
import repo.user.InMemoryUserRepository;
import service.BillingService;
import service.BookingService;
import service.PaymentService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingFlowTest {

    @Test
    void shouldCompleteFullBookingFlow() {
        InMemoryBookingRepository bookingRepo = new InMemoryBookingRepository();
        InMemoryResourceRepository resourceRepo = new InMemoryResourceRepository();
        InMemoryUserRepository userRepo = new InMemoryUserRepository();

        BookingService bookingService = new BookingService(bookingRepo, new StandardPricing());
        PaymentService paymentService = new PaymentService(bookingRepo);
        BillingService billingService = new BillingService();

        User user = new IndividualUser("test@mail.com", "Jan");
        Resource resource = new Desk("D1", Money.of("60.00"), DeskType.HOT);
        resourceRepo.add(resource);
        userRepo.add(user);

        Booking booking = bookingService.book(
                user,
                resource,
                LocalDateTime.of(2026, 4, 4, 10, 0),
                LocalDateTime.of(2026, 4, 4, 12, 0)
        );

        assertEquals(BookingStatus.PENDING, booking.getStatus());

        bookingService.confirm(booking.getId());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());

        paymentService.pay(booking.getId(), "1234");

        assertNotNull(booking.getPayment());

        Invoice invoice = billingService.toInvoice(booking);

        assertNotNull(invoice);
        assertEquals(user, invoice.buyer());
        assertEquals(Money.of("120.00"), invoice.total());
    }
}
