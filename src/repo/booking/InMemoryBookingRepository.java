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
    public void add(Booking booking) {
        bookings.put(booking.getId(), booking);
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
    public List<Booking> findByResource(Resource resource) {
        return bookings.values()
                .stream()
                .filter(b -> b.getResource().equals(resource))
                .toList();
    }

    @Override
    public List<Booking> findByUser(User user) {
        return bookings.values()
                .stream()
                .filter(b -> b.getUser().equals(user))
                .toList();
    }
}
