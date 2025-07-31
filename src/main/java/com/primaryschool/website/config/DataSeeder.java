package com.primaryschool.website.config;

import com.primaryschool.website.entity.Role;
import com.primaryschool.website.entity.User;
import com.primaryschool.website.entity.ContentPage;
import com.primaryschool.website.repository.RoleRepository;
import com.primaryschool.website.repository.UserRepository;
import com.primaryschool.website.repository.ContentPageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ContentPageRepository contentPageRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedRoles();
        seedUsers();
        seedContentPages();
    }

    private void seedRoles() {
        if (roleRepository.count() == 0) {
            log.info("Seeding roles...");

            Role adminRole = new Role("ADMIN", "Administrator role with full access");
            Role userRole = new Role("USER", "Regular user role");

            roleRepository.save(adminRole);
            roleRepository.save(userRole);

            log.info("Roles seeded successfully");
        }
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            log.info("Seeding users...");

            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@primaryschool.com");
            admin.setFullName("School Administrator");
            admin.setPhoneNumber("+1234567890");
            admin.setEnabled(true);
            admin.setRoles(Set.of(adminRole));

            userRepository.save(admin);

            log.info("Admin user created successfully");
        }
    }

    private void seedContentPages() {
        if (contentPageRepository.count() == 0) {
            log.info("Seeding content pages...");

            createContentPage("About Us", "about", 
                "Welcome to our Primary School! We are dedicated to providing quality education...", 1);
            createContentPage("Academics", "academics", 
                "Our academic program is designed to nurture young minds...", 2);
            createContentPage("Contact", "contact", 
                "Get in touch with us for any inquiries...", 3);

            log.info("Content pages seeded successfully");
        }
    }

    private void createContentPage(String title, String slug, String content, int order) {
        ContentPage page = new ContentPage();
        page.setTitle(title);
        page.setSlug(slug);
        page.setContent(content);
        page.setPublished(true);
        page.setDisplayOrder(order);
        page.setCreatedBy("system");
        page.setUpdatedBy("system");

        contentPageRepository.save(page);
    }
}
