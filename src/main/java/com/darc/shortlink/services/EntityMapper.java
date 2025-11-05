package com.darc.shortlink.services;

import com.darc.shortlink.domain.entities.ShortLink;
import com.darc.shortlink.domain.entities.User;
import com.darc.shortlink.domain.models.ShortLinkDto;
import com.darc.shortlink.domain.models.UserDto;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    public ShortLinkDto toShortLinkDto(ShortLink shortLink) {
        UserDto userDto = null;
        if(shortLink.getCreatedBy() != null) {
            userDto = toUserDto(shortLink.getCreatedBy());
        }

        return new ShortLinkDto(
                shortLink.getId(),
                shortLink.getShortKey(),
                shortLink.getOriginalUrl(),
                shortLink.getIsPrivate(),
                shortLink.getExpiresAt(),
                userDto,
                shortLink.getClickCount(),
                shortLink.getCreatedAt()
        );
    }

    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName());
    }
}
