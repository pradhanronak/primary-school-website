package com.primaryschool.website.repository;

import com.primaryschool.website.entity.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {

    List<Gallery> findByPublishedTrueOrderByDisplayOrderAsc();
    List<Gallery> findByFeaturedTrueAndPublishedTrueOrderByDisplayOrderAsc();
    List<Gallery> findByCategoryAndPublishedTrueOrderByDisplayOrderAsc(String category);

    @Query("SELECT DISTINCT g.category FROM Gallery g WHERE g.published = true AND g.category IS NOT NULL ORDER BY g.category")
    List<String> findDistinctCategories();
}
