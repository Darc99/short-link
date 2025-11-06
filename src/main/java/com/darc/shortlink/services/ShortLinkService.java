package com.darc.shortlink.services;

import com.darc.shortlink.domain.models.ShortLinkDto;
import com.darc.shortlink.repositories.ShortLinkRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortLinkService {

    private final ShortLinkRepository shortLinkRepository;
    private final EntityMapper entityMapper;

    public ShortLinkService(ShortLinkRepository shortLinkRepository, EntityMapper entityMapper) {
        this.shortLinkRepository = shortLinkRepository;
        this.entityMapper = entityMapper;
    }

    public List<ShortLinkDto> findAllPublicShortUrls() {
        return shortLinkRepository.findAllPublicShortUrls()
                .stream().map(entityMapper::toShortLinkDto).toList();
    }
}
