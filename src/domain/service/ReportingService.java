package domain.service;

import domain.booking.Booking;
import domain.booking.BookingStatus;
import domain.booking.repository.BookingRepository;
import domain.resource.Resource;
import domain.resource.repository.ResourceRepository;
import money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportingService {
    private final BookingRepository bookingRepo;
    private final ResourceRepository resourceRepo;

    public ReportingService(BookingRepository bookingRepo, ResourceRepository resourceRepo) {
        this.bookingRepo = bookingRepo;
        this.resourceRepo = resourceRepo;
    }

    public Map<Resource, Double> utilization(LocalDateTime from, LocalDateTime to) {
        if (!to.isAfter(from)) {
            throw new IllegalArgumentException("'to' must be after 'from'");
        }

        long totalMinutes = Duration.between(from, to).toMinutes();

        Map<Resource, Double> result = new LinkedHashMap<>();

        for (Resource resource : resourceRepo.findAll()) {

            long usedMinutes = 0;

            List<Booking> bookings = bookingRepo.findByResource(resource);

            for (Booking booking : bookings) {

                if (!isActive(booking)) continue;

                usedMinutes += overlapMinutes(booking, from, to);
            }

            double percent = (usedMinutes * 100.0) / totalMinutes;

            result.put(resource, round(percent));
        }

        return result;
    }

    public Map<String, Money> revenueByResource(LocalDateTime from, LocalDateTime to) {
        Map<String, Money> result = new HashMap<>();

        for (Booking booking : bookingRepo.findAll()) {
            if (!isActive(booking)) continue;
            if (booking.getPayment() == null) continue;

            long overlap = overlapMinutes(booking, from, to);
            if (overlap <= 0) continue;
            String key = booking.getResource().getName();

            result.put(
                    key,
                    result.getOrDefault(key, new Money(BigDecimal.ZERO))
                            .add(booking.getCalculatedPrice())
            );
        }
        return result;
    }

    public Money totalRevenue(LocalDateTime from, LocalDateTime to) {
        Money total = new Money(BigDecimal.ZERO);

        for (Booking booking : bookingRepo.findAll()) {
            if (!isActive(booking)) continue;
            if (booking.getPayment() == null) continue;

            long overlap = overlapMinutes(booking, from, to);
            if (overlap <= 0) continue;

            total = total.add(booking.getCalculatedPrice());
        }
        return total;
    }

    private boolean isActive(Booking booking) {
        return booking.getStatus() == BookingStatus.CONFIRMED ||
                booking.getStatus() == BookingStatus.COMPLETED;
    }

    private long overlapMinutes(Booking b, LocalDateTime from, LocalDateTime to) {

        LocalDateTime start = b.getStart().isAfter(from) ? b.getStart() : from;
        LocalDateTime end = b.getEnd().isBefore(to) ? b.getEnd() : to;

        return start.isBefore(end)
                ? Duration.between(start, end).toMinutes()
                : 0;
    }

    private double round(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
