package repo.booking;

import domain.booking.Booking;
import domain.resource.Resource;
import domain.user.User;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    void add(Booking b);
    Optional<Booking> findById(String id);
    List<Booking> findAll();
    List<Booking> findByResource(Resource r);
    List<Booking> findByUser(User u);
}
