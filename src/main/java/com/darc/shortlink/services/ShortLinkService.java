package com.darc.shortlink.services;

import com.darc.shortlink.domain.entities.ShortUrl;
import com.darc.shortlink.repositories.ShortLinkRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortLinkService {

    private final ShortLinkRepository shortLinkRepository;

    public ShortLinkService(ShortLinkRepository shortLinkRepository) {
        this.shortLinkRepository = shortLinkRepository;
    }

    public List<ShortUrl> findPublicShortUrls() {
        return shortLinkRepository.findPublicShortUrls();
    }
}
