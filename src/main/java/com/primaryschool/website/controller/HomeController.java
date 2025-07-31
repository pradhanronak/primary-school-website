package com.primaryschool.website.controller;

import com.primaryschool.website.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ContentService contentService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("featuredNews", contentService.getFeaturedContent());
        model.addAttribute("featuredGallery", contentService.getFeaturedGalleryImages());
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        return contentService.getPageBySlug("about")
                .map(page -> {
                    model.addAttribute("page", page);
                    return "pages/about";
                })
                .orElse("redirect:/");
    }

    @GetMapping("/academics")
    public String academics(Model model) {
        return contentService.getPageBySlug("academics")
                .map(page -> {
                    model.addAttribute("page", page);
                    return "pages/academics";
                })
                .orElse("redirect:/");
    }

    @GetMapping("/admissions")
    public String admissions(Model model) {
        return "pages/admissions";
    }

    @GetMapping("/news")
    public String news(Model model) {
        model.addAttribute("newsEvents", contentService.getLatestNews());
        return "pages/news";
    }

    @GetMapping("/news/{slug}")
    public String newsDetail(@PathVariable String slug, Model model) {
        return contentService.getNewsEventBySlug(slug)
                .map(newsEvent -> {
                    model.addAttribute("newsEvent", newsEvent);
                    return "pages/news-detail";
                })
                .orElse("redirect:/news");
    }

    @GetMapping("/gallery")
    public String gallery(Model model) {
        model.addAttribute("galleryImages", contentService.getGalleryImages());
        model.addAttribute("categories", contentService.getGalleryCategories());
        return "pages/gallery";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        return contentService.getPageBySlug("contact")
                .map(page -> {
                    model.addAttribute("page", page);
                    return "pages/contact";
                })
                .orElse("redirect:/");
    }
}
