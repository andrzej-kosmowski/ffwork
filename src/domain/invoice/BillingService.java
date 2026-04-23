package domain.invoice;

import domain.booking.Booking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BillingService implements Billable {
    private String lastDate = "";
    private int counter = 1;

    @Override
    public Invoice toInvoice(Booking booking) {
        LocalDateTime now = LocalDateTime.now();

        String date = now.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        if(!date.equals(lastDate)) {
            lastDate = date;
            counter = 1;
        }

        String number = "INV-" + date + "-" + counter++;

        return new Invoice(
                number,
                now,
                booking.getUser(),
                booking.getCalculatedPrice(),
                "Reservation " + booking.getResource().getName()
        );
    }
}
