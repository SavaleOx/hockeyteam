package com.savaleox.hockeyteam.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s with id %d not found", resourceName, id));
    }

    public ResourceNotFoundException(String resourceName, String name) {
        super(String.format("%s with name '%s' not found", resourceName, name));
    }
}
