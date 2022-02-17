package org.shrtr.core.services;

import lombok.RequiredArgsConstructor;
import org.shrtr.core.domain.entities.Link;
import org.shrtr.core.domain.entities.User;
import org.shrtr.core.domain.repositories.LinksRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Primary
@RequiredArgsConstructor
public class MariaDBRateLimiting implements RateLimiting{
    private final LinkService linkService;
    private final LinksRepository linksRepository;

    @Override
    public Boolean hasNotExceedRateLimiting(User user, LocalDateTime from, LocalDateTime to) {
        return linkService.verifyRateLimit(from, LocalDateTime.now(), user);
    }

    @Override
    public void incrementCounter(Link link) {
        link.setCounter(link.getCounter() + 1);
        link.setUpdatedOn(LocalDateTime.now());
        linksRepository.save(link);
    }

    @Override
    public void resetCounter(String shortened) {
        linkService.resetCounter(shortened);
    }

}
