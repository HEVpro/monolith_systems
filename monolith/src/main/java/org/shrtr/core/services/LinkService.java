
package org.shrtr.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shrtr.core.domain.entities.Link;
import org.shrtr.core.domain.entities.LinkMetric;
import org.shrtr.core.domain.entities.User;
import org.shrtr.core.domain.repositories.LinkMetricsRepository;
import org.shrtr.core.domain.repositories.LinksRepository;
import org.shrtr.core.events.EventService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkService {
  private final LinksRepository linksRepository;
    private final LinkMetricsRepository linkMetricsRepository;
    private final EventService eventService;


  @Transactional
  public Link create(String targetUrl, User user) {

    Link link = new Link();
    link.setOriginal(targetUrl);

    link.setOwner(user);
    link.setCounter(0);
    link.setShortened(randomStringAlphaNumeric(8));
      linksRepository.save(link);
      eventService.entityCreated(link);
    return link;
  }
  public List<LinkMetric> findLinkMetrics(Link link, LocalDate from, LocalDate to) {
    return linkMetricsRepository.findAllByDateBetweenAndLink(from, to, link);
  }
  public void resetCounter(String shortened){
    Optional<Link> linkToReset = linksRepository.findByShortened(shortened);
    if(linkToReset.isPresent()){
      long millisecondsBetweenDates = Duration.between(linkToReset.get().getUpdatedOn(), LocalDateTime.now()).toMillis();
      long minutesBetweenDates = TimeUnit.MILLISECONDS.toMinutes(millisecondsBetweenDates);
      if(minutesBetweenDates > 10){
        linkToReset.get().setCounter(0);
        linksRepository.save(linkToReset.get());
      }
    }
  }
  public boolean verifyRateLimit(LocalDateTime from, LocalDateTime to, User user) {
    List<Link> redirectsIn10Minutes = linksRepository.findAllByUpdatedOnBetweenAndOwner(from, to, user);
    int totalRedirects = redirectsIn10Minutes.stream().mapToInt(Link::getCounter).sum();
    return totalRedirects < user.getMax_requests();
  }

  @Transactional
  public Optional<Link> findForRedirect(String shortened) {
    Optional<Link> byShortened = linksRepository.findByShortened(shortened);
    if (byShortened.isPresent()) {
      LocalDate date = LocalDate.now();
      Link link = byShortened.get();
      Optional<LinkMetric> byLinkAndDate = linkMetricsRepository.findByLinkAndDate(link, date);
      if (byLinkAndDate.isPresent()) {
        LinkMetric linkMetric = byLinkAndDate.get();
        linkMetric.setCount(linkMetric.getCount() + 1);
        log.info("Count of {} is {}", link.getShortened(), linkMetric.getCount());
        linkMetricsRepository.save(linkMetric);
      } else {
        LinkMetric linkMetric = new LinkMetric();
        linkMetric.setLink(link);
        linkMetric.setDate(date);
        linkMetric.setCount(1);
        log.info("Count of {} is {}", link.getShortened(), linkMetric.getCount());
        linkMetricsRepository.save(linkMetric);
      }
    }
    return byShortened;
  }

  @Transactional
  public List<Link> getAllLinks(User user) {
    return linksRepository.findByOwner(user);
  }

  @Transactional
  public Optional<Link> getLink(User user, UUID id) {
    return linksRepository.findByOwnerAndId(user, id);
  }

  @Transactional
  public Optional<Link> deleteLink(User user, UUID id) {
    return linksRepository.findByOwnerAndId(user, id)
            .stream()
            .peek(linksRepository::delete)
            .findAny();

  }
  private static String randomStringAlphaNumeric(int size) {
    return randomString(AB, size);
  }
  private static String randomString(String candidates, int size) {
    SecureRandom secureRandom = new SecureRandom();
    StringBuilder sb = new StringBuilder(size);
    for( int i = 0; i < size; i++ )
      sb.append( candidates.charAt( secureRandom.nextInt(candidates.length()) ) );
    return sb.toString();
  }

  private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";


}
