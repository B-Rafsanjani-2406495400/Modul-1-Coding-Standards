package id.ac.ui.cs.advprog.eshop.util;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UuidIdGeneratorTest {

    @Test
    void generate_shouldReturnValidUuid() {
        IdGenerator gen = new UuidIdGenerator();
        String id = gen.generate();

        assertNotNull(id);
        assertDoesNotThrow(() -> UUID.fromString(id));
    }
}