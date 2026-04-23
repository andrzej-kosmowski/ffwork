package payment;

import money.Money;

public class CardPayment extends Payment {
    private final String last4;

    public CardPayment(Money amount, String paymentId, String last4) {
        super(amount, paymentId);
        this.last4 = last4;
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
