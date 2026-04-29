package domain.booking;

import payment.Payment;
import domain.resource.Resource;
import domain.user.User;
import money.Money;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.Duration.between;

public class Booking {
    private final String id;
    private final User user;
    private final Resource resource;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private BookingStatus status;
    private Money calculatedPrice;
    private Payment payment;

    public Booking(String id, User user, Resource resource, LocalDateTime start, LocalDateTime end, BookingStatus status, Money calculatedPrice) {
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("End must be after start");
        }

        this.id = id;
        this.user = user;
        this.resource = resource;
        this.start = start;
        this.end = end;
        this.status = status;
        this.calculatedPrice = calculatedPrice;
        this.payment = null;
    }

    public long durationMinutes() {
        return  Duration.between(start, end).toMinutes();
    }

    public void confirm() {
        if (status != BookingStatus.PENDING) {
            throw new IllegalStateException("Invalid transition");
        }
        status = BookingStatus.CONFIRMED;
    }

    public void cancel() {
        if (status == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed booking");
        }
        status = BookingStatus.CANCELLED;
    }

    public void complete() {
        if (status != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Invalid transition");
        }
        status = BookingStatus.COMPLETED;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Resource getResource() {
        return resource;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public Money getCalculatedPrice() {
        return calculatedPrice;
    }

    public void setCalculatedPrice(Money calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
