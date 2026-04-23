package domain.invoice;

import domain.booking.Booking;

public interface Billable {
    Invoice toInvoice(Booking booking);
}
