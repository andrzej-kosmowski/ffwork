package domain.payment;

import domain.booking.Booking;
import domain.booking.BookingStatus;
import domain.booking.repository.BookingRepository;

public class PaymentService {
    private final BookingRepository bookingRepo;

    public PaymentService(BookingRepository bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    public Payment pay(String bookingId, String cardLast4) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Payment allowed only for CONFIRMED bookings");
        }

        CardPayment payment = new CardPayment(
                booking.getCalculatedPrice(),
                bookingId,
                cardLast4
        );

        payment.capture();

        booking.setPayment(payment);
        return payment;
    }
}
