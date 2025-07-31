package com.primaryschool.website.service;

import com.primaryschool.website.entity.ContentPage;
import com.primaryschool.website.entity.NewsEvent;
import com.primaryschool.website.entity.Gallery;
import com.primaryschool.website.repository.ContentPageRepository;
import com.primaryschool.website.repository.NewsEventRepository;
import com.primaryschool.website.repository.GalleryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ContentService {

    private final ContentPageRepository contentPageRepository;
    private final NewsEventRepository newsEventRepository;
    private final GalleryRepository galleryRepository;

    // Content Pages
    public List<ContentPage> getAllPublishedPages() {
        return contentPageRepository.findPublishedPagesOrdered();
    }

    public Optional<ContentPage> getPageBySlug(String slug) {
        return contentPageRepository.findBySlug(slug);
    }

    // News and Events
    public List<NewsEvent> getLatestNews() {
        return newsEventRepository.findLatestPublishedContent();
    }

    public List<NewsEvent> getFeaturedContent() {
        return newsEventRepository.findByFeaturedTrueAndPublishedTrueOrderByCreatedAtDesc();
    }

    public Optional<NewsEvent> getNewsEventBySlug(String slug) {
        return newsEventRepository.findBySlug(slug);
    }

    // Gallery
    public List<Gallery> getGalleryImages() {
        return galleryRepository.findByPublishedTrueOrderByDisplayOrderAsc();
    }

    public List<Gallery> getFeaturedGalleryImages() {
        return galleryRepository.findByFeaturedTrueAndPublishedTrueOrderByDisplayOrderAsc();
    }

    public List<String> getGalleryCategories() {
        return galleryRepository.findDistinctCategories();
    }
}
