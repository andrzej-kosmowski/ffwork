package domain.service;

import domain.booking.Booking;
import domain.booking.BookingStatus;
import domain.booking.repository.BookingRepository;
import domain.pricing.PricingPolicy;
import domain.resource.Device;
import domain.resource.Resource;
import domain.resource.repository.ResourceRepository;
import domain.user.User;
import domain.user.repository.UserRepository;
import money.Money;

import java.awt.print.Book;
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

    public Booking book(User u, Resource r, LocalDateTime start, LocalDateTime end) {
        validateTime(start, end);
        checkCollisions(r, start, end);
        String id = generateId(start);
        Booking booking = new Booking(id, u, r, start, end, BookingStatus.PENDING, null);
        Money base = pricingPolicy.price(booking);
        booking.setCalculatedPrice(base);

        bookingRepo.add(booking);
        return booking;
    }

    public Booking book(User u, Resource r, LocalDateTime start, int durationMinutes) {
        LocalDateTime end = start.plusMinutes(durationMinutes);
        return book(u, r, start, end);
    }

    public void confirm(String bookingId) {
        Booking b = getOrThrow(bookingId);
        if (b.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Only PENDING can be confirmed");
        }

        b.setStatus(BookingStatus.CONFIRMED);
    }

    public void cancel(String bookingId) {
        Booking b = getOrThrow(bookingId);
        if (b.getStatus() == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed booking");
        }

        b.setStatus(BookingStatus.CANCELLED);
    }

    public void complete(String bookingId) {
        Booking b = getOrThrow(bookingId);
        if (b.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only CONFIRMED can be completed");
        }

        b.setStatus(BookingStatus.COMPLETED);
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

    private boolean overlaps(Booking b, LocalDateTime start, LocalDateTime end) {
        return b.getStart().isBefore(end) && start.isBefore(b.getEnd());
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
