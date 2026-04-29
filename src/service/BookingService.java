package service;

import domain.booking.Booking;
import domain.booking.BookingStatus;
import repo.booking.BookingRepository;
import pricing.PricingPolicy;
import domain.resource.Device;
import domain.resource.Resource;
import domain.user.User;
import money.Money;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingService {
    private final BookingRepository bookingRepo;
    private final PricingPolicy pricingPolicy;

    private String lastDate = "";
    private int dailyCounter = 1;

    public BookingService(BookingRepository bookingRepo, PricingPolicy pricingPolicy) {
        this.bookingRepo = bookingRepo;
        this.pricingPolicy = pricingPolicy;
    }

    public Booking book(User user, Resource resource, LocalDateTime start, LocalDateTime end) {
        validateTime(start, end);
        checkCollisions(resource, start, end);
        String id = generateId(start);
        Booking booking = new Booking(id, user, resource, start, end, BookingStatus.PENDING, null);
        Money base = pricingPolicy.price(booking);
        booking.setCalculatedPrice(base);

        bookingRepo.add(booking);
        return booking;
    }

    public Booking book(User user, Resource resource, LocalDateTime start, int durationMinutes) {
        LocalDateTime end = start.plusMinutes(durationMinutes);
        return book(user, resource, start, end);
    }

    public void confirm(String bookingId) {
        Booking booking = getOrThrow(bookingId);
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Only PENDING can be confirmed");
        }

        booking.confirm();
    }

    public void cancel(String bookingId) {
        Booking booking = getOrThrow(bookingId);
        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed booking");
        }

        booking.cancel();
    }

    public void complete(String bookingId) {
        Booking booking = getOrThrow(bookingId);
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only CONFIRMED can be completed");
        }

        booking.complete();
    }

    public List<Booking> list() {
        return bookingRepo.findAll();
    }

    public List<Booking> list(User user) {
        return bookingRepo.findByUser(user);
    }

    public List<Booking> list(Resource resource) {
        return bookingRepo.findByResource(resource);
    }

    private void validateTime(LocalDateTime start, LocalDateTime end) {
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("End must be after start");
        }
    }

    private void checkCollisions(Resource resource, LocalDateTime start, LocalDateTime end) {
        long overlappingBookings = bookingRepo.findByResource(resource)
                .stream()
                .filter(b -> b.getStatus() != BookingStatus.CANCELLED)
                .filter(b -> overlaps(b, start, end))
                .count();

        if (resource instanceof Device device) {
            if (overlappingBookings >= device.getQuantity()) {
                throw new IllegalStateException("No available devices: " + resource.getName());
            }
        } else {
            if (overlappingBookings > 0) {
                throw new IllegalStateException("Resource already booked: " + resource.getName());
            }
        }
    }

    private boolean overlaps(Booking booking, LocalDateTime start, LocalDateTime end) {
        return booking.getStart().isBefore(end) && start.isBefore(booking.getEnd());
    }

    private String generateId(LocalDateTime start) {
        String date = start.toLocalDate()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        if (!date.equals(lastDate)) {
            lastDate = date;
            dailyCounter = 1;
        }

        return "BK-" + date + "-" + dailyCounter++;
    }

    private Booking getOrThrow(String id) {
        return bookingRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + id));
    }
}
