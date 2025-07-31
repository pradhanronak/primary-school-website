package com.primaryschool.website.repository;

import com.primaryschool.website.entity.ContentPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentPageRepository extends JpaRepository<ContentPage, Long> {

    Optional<ContentPage> findBySlug(String slug);
    List<ContentPage> findByPublishedTrueOrderByDisplayOrderAsc();
    boolean existsBySlug(String slug);

    @Query("SELECT c FROM ContentPage c WHERE c.published = true ORDER BY c.displayOrder ASC, c.title ASC")
    List<ContentPage> findPublishedPagesOrdered();
}
