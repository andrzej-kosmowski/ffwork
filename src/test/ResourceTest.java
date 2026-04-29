package test;

import domain.resource.Desk;
import domain.resource.DeskType;
import domain.resource.Device;
import domain.resource.Room;
import money.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ResourceTest {

    @Test
    void roomShouldReturnBaseRate() {
        Room room = new Room("Room", 10, Set.of("Projector"));
        assertEquals(new BigDecimal("15.00"), room.hourlyRate().amount());
    }

    @Test
    void shouldOverridesBaseRate() {
        Room room = new Room("Premium Room", Money.of("80.00"), 8, Set.of());
        assertEquals(new BigDecimal("80.00"), room.hourlyRate().amount());
    }

    @Test
    void deviceShouldReturnBaseRate() {
        Device device = new Device("Notebook", 2);
        assertEquals(new BigDecimal("5.00"), device.hourlyRate().amount());
    }

    @Test
    void deskShouldDescribeCorrectly() {
        Desk desk = new Desk("Desk 1", DeskType.FIXED);
        assertTrue(desk.describe().contains("FIXED"));
    }
}
