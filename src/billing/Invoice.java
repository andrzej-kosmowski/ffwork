package billing;

import domain.user.User;
import money.Money;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Invoice(String invoiceNumber, LocalDateTime issueDate, User buyer, Money total, String itemDescription) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public String toString() {
        return String.format("Invoice %s%nDate: %s%nBuyer: %s%nAmount: %s%nDescription: %s",
                invoiceNumber,
                issueDate.format(FORMATTER),
                buyer,
                total,
                itemDescription);
    }
}
