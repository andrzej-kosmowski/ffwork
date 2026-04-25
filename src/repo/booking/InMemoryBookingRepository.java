package repo.booking;

import domain.booking.Booking;
import domain.resource.Resource;
import domain.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryBookingRepository implements BookingRepository {
    private final Map<String, Booking> bookings = new HashMap<>();


    @Override
    public void add(Booking b) {
        bookings.put(b.getId(), b);
    }

    @Override
    public Optional<Booking> findById(String id) {
        return Optional.ofNullable(bookings.get(id));
    }

    @Override
    public List<Booking> findAll() {
        return bookings.values()
                .stream()
                .toList();
    }

    @Override
    public List<Booking> findByResource(Resource r) {
        return bookings.values()
                .stream()
                .filter(b -> b.getResource().equals(r))
                .toList();
    }

    @Override
    public List<Booking> findByUser(User u) {
        return bookings.values()
                .stream()
                .filter(b -> b.getUser().equals(u))
                .toList();
    }
}
