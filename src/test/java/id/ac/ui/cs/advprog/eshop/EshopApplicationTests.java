package id.ac.ui.cs.advprog.eshop;

import org.junit.jupiter.api.Test;

class EshopApplicationTests {

    @Test
    void main_shouldRunWithNonWebApplicationType() {
        EshopApplication.main(new String[] {
                "--spring.main.web-application-type=none",
                "--spring.main.banner-mode=off"
        });
    }
}