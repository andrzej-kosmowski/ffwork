package test;

import domain.user.CompanyUser;
import domain.user.IndividualUser;
import domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repo.user.InMemoryUserRepository;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserTest {
    private InMemoryUserRepository repo;

    @BeforeEach
    void setUp() {
       repo = new InMemoryUserRepository();
    }

    @Test
    void shouldAddAndFindUserByEmail() {
        User u = new IndividualUser("a@b.com", "Jan");
        repo.add(u);
        Optional<User> found = repo.findByEmail("a@b.com");
        assertTrue(found.isPresent());
        assertEquals("Jan", found.get().getDisplayName());
    }

    @Test
    void shouldReturnEmptyForUnknownEmail() {
        assertTrue(repo.findByEmail("a@a.com").isEmpty());
    }

    @Test
    void shouldListAllUsers() {
        repo.add(new IndividualUser("a@b.com", "A"));
        repo.add(new CompanyUser("c@d.com", "B", "Company", "123-456-78-90"));
        assertEquals(2, repo.findAll().size());
    }

    @Test
    void shouldHaveCompanyFields() {
        CompanyUser cu = new CompanyUser("company@x.com", "CompanyB", "CompanyB", "123-456-78-90");
        assertEquals("CompanyB", cu.getCompanyName());
        assertEquals("123-456-78-90", cu.getTaxId());
    }
}
