package repo.resource;

import domain.resource.Resource;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository {
    void add(Resource resource);
    Optional<Resource> findByName(String name);
    List<Resource> findAll();
    List<Resource> findByType(Class<? extends Resource> type);
}
