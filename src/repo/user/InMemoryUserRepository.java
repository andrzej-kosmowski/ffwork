package repo.user;

import domain.user.User;

import java.util.*;

public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> users = new HashMap<>();
    @Override
    public void add(User user) {
        users.put(user.getEmail(), user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(users.get(email));
    }

    @Override
    public List<User> findAll() {
        return users.values()
                .stream()
                .toList();
    }
}
