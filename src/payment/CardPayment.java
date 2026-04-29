package payment;

import money.Money;

import java.util.Objects;

public class CardPayment extends Payment {
    private final String last4;

    public CardPayment(Money amount, String paymentId, String last4) {
        super(amount, paymentId);
        if (!last4.matches("\\d{4}")) {
            throw new IllegalArgumentException("last4 must be exactly 4 digits");
        }
        this.last4 = Objects.requireNonNull(last4, "last4 cannot be null");
    }

    @Override
    public void capture() {
        if (getStatus() != PaymentStatus.INITIATED) {
            throw new IllegalStateException("Payment already captured");
        }
        setStatus(PaymentStatus.CAPTURED);
    }

    public String getLast4() {
        return last4;
    }
}
