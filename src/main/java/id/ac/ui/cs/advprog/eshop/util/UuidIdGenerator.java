package id.ac.ui.cs.advprog.eshop.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidIdGenerator implements IdGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}