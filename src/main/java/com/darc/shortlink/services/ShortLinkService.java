package com.darc.shortlink.services;

import com.darc.shortlink.ApplicationProperties;
import com.darc.shortlink.domain.entities.ShortLink;
import com.darc.shortlink.domain.models.CreateShortUrlCmd;
import com.darc.shortlink.domain.models.ShortLinkDto;
import com.darc.shortlink.repositories.ShortLinkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ShortLinkService {

    private final ShortLinkRepository shortLinkRepository;
    private final EntityMapper entityMapper;
    private final ApplicationProperties properties;

    public ShortLinkService(ShortLinkRepository shortLinkRepository, EntityMapper entityMapper, ApplicationProperties properties) {
        this.shortLinkRepository = shortLinkRepository;
        this.entityMapper = entityMapper;
        this.properties = properties;
    }

    public List<ShortLinkDto> findAllPublicShortUrls() {
        return shortLinkRepository.findAllPublicShortUrls()
                .stream().map(entityMapper::toShortLinkDto).toList();
    }

    @Transactional
    public ShortLinkDto createShortUrl(CreateShortUrlCmd cmd) {
        if(properties.validateOriginalUrl()) {
            boolean urlExists = UrlExistenceValidator.isUrlExists(cmd.originalUrl());
            if(!urlExists) {
                throw new RuntimeException("Original url does not exist"+ cmd.originalUrl());
            }
        }
        var shortKey = generateUniqueShortKey();
        var shortUrl = new ShortLink();
        shortUrl.setOriginalUrl(cmd.originalUrl());
        shortUrl.setShortKey(shortKey);
        shortUrl.setCreatedBy(null);
        shortUrl.setIsPrivate(false);
        shortUrl.setClickCount(0L);
        shortUrl.setCreatedAt(Instant.now());
        shortUrl.setExpiresAt(Instant.now().plus(properties.defaultExpiryInDays(), ChronoUnit.DAYS));
        shortLinkRepository.save(shortUrl);
        return entityMapper.toShortLinkDto(shortUrl);
    }

//    to check if random key exists
    private String generateUniqueShortKey() {
        String shortKey;
        do {
            shortKey = generateRandomShortKey();
        } while (shortLinkRepository.existsByShortKey(shortKey));
        return shortKey;
    }

//    to generate random keys
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SHORT_KEY_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomShortKey() {
        StringBuilder sb = new StringBuilder(SHORT_KEY_LENGTH);
        for (int i = 0; i < SHORT_KEY_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    @Transactional // doing a "write" operation
    public Optional<ShortLinkDto> accessShortUrl(String shortKey) {
       Optional<ShortLink> shortLinkOptional = shortLinkRepository.findByShortKey(shortKey);
       if(shortLinkOptional.isEmpty()) {
           return Optional.empty();
       }
       ShortLink shortLink = shortLinkOptional.get();
       if (shortLink.getExpiresAt() != null && shortLink.getExpiresAt().isBefore(Instant.now())) {
           return Optional.empty();
       }
       shortLink.setClickCount(shortLink.getClickCount() + 1);
       shortLinkRepository.save(shortLink);
       return Optional.of(entityMapper.toShortLinkDto(shortLink));
    }
}
