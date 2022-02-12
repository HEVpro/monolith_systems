package org.shrtr.core.services;

import org.shrtr.core.domain.entities.Link;
import org.shrtr.core.domain.entities.User;

import java.time.LocalDateTime;

public interface RateLimiting {
    Boolean hasNotExceedRateLimiting(User user, LocalDateTime from, LocalDateTime to);
    Void incrementCounter(Link link);
    Void resetCounter(String shortened);
}
