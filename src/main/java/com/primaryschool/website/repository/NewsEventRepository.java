package com.primaryschool.website.repository;

import com.primaryschool.website.entity.NewsEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsEventRepository extends JpaRepository<NewsEvent, Long> {

    Optional<NewsEvent> findBySlug(String slug);
    List<NewsEvent> findByPublishedTrueOrderByCreatedAtDesc();
    List<NewsEvent> findByFeaturedTrueAndPublishedTrueOrderByCreatedAtDesc();
    List<NewsEvent> findByTypeAndPublishedTrueOrderByCreatedAtDesc(NewsEvent.ContentType type);
    boolean existsBySlug(String slug);

    @Query("SELECT n FROM NewsEvent n WHERE n.published = true ORDER BY n.createdAt DESC")
    List<NewsEvent> findLatestPublishedContent();

    @Query("SELECT n FROM NewsEvent n WHERE n.published = true AND n.type = :type ORDER BY n.eventDate ASC")
    List<NewsEvent> findUpcomingEventsByType(@Param("type") NewsEvent.ContentType type);
}
