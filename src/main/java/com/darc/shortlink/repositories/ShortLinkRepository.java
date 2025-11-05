package com.darc.shortlink.repositories;

import com.darc.shortlink.domain.entities.ShortLink;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShortLinkRepository extends JpaRepository<ShortLink, Long> {

//    List<ShortUrl> findByIsPrivateIsFalseOrderByCreatedAtDesc();

//    @EntityGraph(attributePaths = {"createdBy"})
//    using join fetch to fetch associated data
    @Query("select su from ShortLink su left join fetch su.createdBy where su.isPrivate = false order by su.createdAt desc")
    List<ShortLink> findPublicShortUrls();
}
