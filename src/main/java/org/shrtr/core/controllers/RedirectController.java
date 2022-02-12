package org.shrtr.core.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shrtr.core.domain.entities.User;
import org.shrtr.core.services.LinkService;
import org.shrtr.core.services.RateLimiting;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/r")
@RequiredArgsConstructor
public class RedirectController {

    private final LinkService linkService;
    private final RateLimiting rateLimiting;

    @GetMapping("/{shortened}")
    public RedirectView getLinks(@PathVariable("shortened") String shortened, @AuthenticationPrincipal User user) throws LinkException {
        log.info("Asked for redirect {}", shortened);
        LocalDateTime from = LocalDateTime.now().minus(Duration.ofMillis(user.getMax_requests_time_window_ms()));
        if(rateLimiting.hasNotExceedRateLimiting(user, from, LocalDateTime.now())){
            return linkService.findForRedirect(shortened)
                    .map(link -> {
                        // Generate redirect
                        RedirectView redirectView = new RedirectView();
                        redirectView.setUrl(link.getOriginal());
                        // If link last updated_at is over 10 min, set count to 0
                        rateLimiting.resetCounter(link.getShortened());
                        // Add 1 to counter of the link is between the 10 min gap
                        rateLimiting.incrementCounter(link);
                        log.info("Found redirect from {} to {}", shortened, link.getOriginal());
                        return redirectView;
                    })
                    .orElseThrow(() -> {
                        log.info("Not found redirect from {}", shortened);
                        return new NotFoundException();
                    });
        }else{
            throw new LinkException("User has passed the rate limiting");
        }
    }
}

