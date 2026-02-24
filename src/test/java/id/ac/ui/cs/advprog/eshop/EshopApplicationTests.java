package id.ac.ui.cs.advprog.eshop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class EshopApplicationTests {

    @Test
    void main_shouldRunWithNonWebApplicationType() {
        assertDoesNotThrow(() -> EshopApplication.main(new String[] {
                "--spring.main.web-application-type=none",
                "--spring.main.banner-mode=off"
        }));
    }
}