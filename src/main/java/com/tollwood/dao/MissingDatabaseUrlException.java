package com.tollwood.dao;

class MissingDatabaseUrlException extends RuntimeException {
    MissingDatabaseUrlException() {
        super("System env DATABASE_URL must be set using this format: postgres://{USERNAME}:{PASSWORD}@{HOST}");
    }
}
