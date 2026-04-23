package domain.resource.repository;

import domain.resource.Resource;

import java.util.*;

public class InMemoryResourceRepository implements ResourceRepository {
    private final Map<String, Resource> resources = new HashMap<>();
    @Override
    public void add(Resource r) {
        resources.put(r.getName(), r);
    }

    @Override
    public Optional<Resource> findByName(String name) {
        return Optional.ofNullable(resources.get(name));
    }

    @Override
    public List<Resource> findAll() {
        return resources.values()
                .stream()
                .toList();
    }

    @Override
    public List<Resource> findByType(Class<? extends Resource> type) {
        return resources.values()
                .stream()
                .filter(type::isInstance)
                .toList();
    }
}
