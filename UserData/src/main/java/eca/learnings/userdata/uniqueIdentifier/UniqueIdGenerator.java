package eca.learnings.userdata.uniqueIdentifier;

import java.util.UUID;

public class UniqueIdGenerator {
    public static String generateUniqueId() {
        return UUID.randomUUID().toString();
    }
}

