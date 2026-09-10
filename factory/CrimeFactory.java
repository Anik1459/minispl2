package com.example.oopproject.factory;

public final class CrimeFactory {
    private CrimeFactory() {}

    public static Crime createCrime(String type) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Crime type is required.");
        }

        return switch (type.trim().toLowerCase()) {
            case "theft", "burglary", "theft/burglary" -> new TheftCrime();
            case "robbery" -> new RobberyCrime();
            case "fraud" -> new FraudCrime();
            case "cybercrime" -> new CybercrimeCrime();
            case "kidnapping" -> new KidnappingCrime();
            default -> new GenericCrime(type.trim());
        };
    }

    private static final class GenericCrime implements Crime {
        private final String type;

        private GenericCrime(String type) {
            this.type = type;
        }

        @Override
        public String getType() {
            return type;
        }
    }
}
