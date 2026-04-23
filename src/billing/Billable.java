package billing;

import domain.booking.Booking;

public interface Billable {
    Invoice toInvoice(Booking booking);
}
