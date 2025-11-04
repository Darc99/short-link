package com.darc.shortlink.repositories;

import com.darc.shortlink.domain.entities.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShortLinkRepository extends JpaRepository<ShortUrl, Long> {

//    List<ShortUrl> findByIsPrivateIsFalseOrderByCreatedAtDesc();

    @Query("select su from ShortUrl su where su.isPrivate = false order by su.createdAt desc")
    List<ShortUrl> findPublicShortUrls();
}
