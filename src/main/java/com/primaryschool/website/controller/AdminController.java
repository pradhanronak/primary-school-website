package com.primaryschool.website.controller;

import com.primaryschool.website.entity.AdmissionApplication;
import com.primaryschool.website.service.AdmissionService;
import com.primaryschool.website.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdmissionService admissionService;
    private final ContentService contentService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pendingApplications", 
            admissionService.getApplicationCountByStatus(AdmissionApplication.ApplicationStatus.PAYMENT_PENDING));
        model.addAttribute("underReviewApplications", 
            admissionService.getApplicationCountByStatus(AdmissionApplication.ApplicationStatus.UNDER_REVIEW));
        model.addAttribute("approvedApplications", 
            admissionService.getApplicationCountByStatus(AdmissionApplication.ApplicationStatus.APPROVED));

        return "admin/dashboard";
    }

    @GetMapping("/applications")
    public String applications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AdmissionApplication> applications;

        if (status != null && !status.isEmpty()) {
            AdmissionApplication.ApplicationStatus applicationStatus = 
                AdmissionApplication.ApplicationStatus.valueOf(status);
            applications = Page.empty(); // Would implement filtered query
        } else {
            applications = admissionService.findAll(pageable);
        }

        model.addAttribute("applications", applications);
        model.addAttribute("currentStatus", status);
        model.addAttribute("statuses", AdmissionApplication.ApplicationStatus.values());

        return "admin/applications";
    }

    @GetMapping("/applications/{id}")
    public String applicationDetail(@PathVariable Long id, Model model) {
        return admissionService.findById(id)
                .map(application -> {
                    model.addAttribute("application", application);
                    return "admin/application-detail";
                })
                .orElse("redirect:/admin/applications");
    }

    @PostMapping("/applications/{id}/status")
    public String updateApplicationStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String notes,
            Model model) {

        try {
            AdmissionApplication.ApplicationStatus newStatus = 
                AdmissionApplication.ApplicationStatus.valueOf(status);
            admissionService.updateStatus(id, newStatus, "admin"); // In real app, get current user

            return "redirect:/admin/applications/" + id + "?success=true";
        } catch (Exception e) {
            return "redirect:/admin/applications/" + id + "?error=true";
        }
    }

    @GetMapping("/content")
    public String contentManagement(Model model) {
        model.addAttribute("pages", contentService.getAllPublishedPages());
        model.addAttribute("news", contentService.getLatestNews());
        return "admin/content";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        return "admin/settings";
    }
}
