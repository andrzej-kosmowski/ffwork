package payment;

import money.Money;

import java.util.Objects;

public abstract class Payment {
    private final Money amount;
    private final String paymentId;
    private PaymentStatus status;

    public Payment(Money amount, String paymentId) {
        this.amount = Objects.requireNonNull(amount, "amount cannot be null");
        this.paymentId = Objects.requireNonNull(paymentId, "paymentId cannot be null");
        this.status = PaymentStatus.INITIATED;
    }

    public abstract void capture();

    public Money getAmount() {
        return amount;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
