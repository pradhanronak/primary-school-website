-- Insert sample roles
INSERT INTO roles (name, description) VALUES 
('ADMIN', 'Administrator role with full access'),
('USER', 'Regular user role');

-- Insert sample content pages
INSERT INTO content_pages (title, slug, content, meta_description, is_published, display_order, created_at, updated_at, created_by, updated_by) VALUES
('About Us', 'about', '<h2>Welcome to Primary School</h2><p>We are dedicated to providing quality education in a nurturing environment. Our experienced teachers and modern facilities ensure that every child receives the best possible start to their educational journey.</p><p>Founded in 1995, we have been serving the community for over 25 years, helping thousands of students develop into confident, capable learners.</p>', 'Learn about our school, mission, and values', true, 1, NOW(), NOW(), 'system', 'system'),
('Academics', 'academics', '<h2>Academic Programs</h2><p>Our comprehensive academic program is designed to challenge and inspire students from Pre-K through Grade 8.</p><h3>Core Subjects</h3><ul><li>English Language Arts</li><li>Mathematics</li><li>Science</li><li>Social Studies</li></ul><h3>Special Programs</h3><ul><li>Art & Music</li><li>Physical Education</li><li>Computer Science</li><li>Foreign Languages</li></ul>', 'Explore our academic programs and curriculum', true, 2, NOW(), NOW(), 'system', 'system'),
('Contact', 'contact', '<h2>Contact Information</h2><p><strong>Address:</strong><br>123 Education Street<br>Learning City, LC 12345</p><p><strong>Phone:</strong> (555) 123-4567<br><strong>Email:</strong> info@primaryschool.edu</p><p><strong>Office Hours:</strong><br>Monday - Friday: 8:00 AM - 4:00 PM</p>', 'Get in touch with our school', true, 3, NOW(), NOW(), 'system', 'system');

-- Insert sample news and events
INSERT INTO news_events (title, slug, summary, content, type, is_featured, is_published, created_at, updated_at, created_by, updated_by) VALUES
('Welcome Back to School!', 'welcome-back-2024', 'We are excited to welcome all students back for the 2024-2025 academic year.', '<p>We are thrilled to welcome back all our students and families for another exciting academic year! This year promises to be filled with learning, growth, and new opportunities.</p><p>Please remember to check your supply lists and mark your calendars for upcoming events.</p>', 'NEWS', true, true, NOW(), NOW(), 'system', 'system'),
('Fall Festival - October 15th', 'fall-festival-2024', 'Join us for our annual Fall Festival with games, food, and fun for the whole family.', '<p>Our annual Fall Festival is just around the corner! Join us on October 15th from 2:00 PM to 6:00 PM for an afternoon of family fun.</p><p>Activities include:<ul><li>Pumpkin decorating</li><li>Face painting</li><li>Games and prizes</li><li>Food trucks</li><li>Live music</li></ul></p>', 'EVENT', true, true, NOW(), NOW(), 'system', 'system');

-- Insert sample gallery images
INSERT INTO gallery (title, description, image_url, category, is_featured, is_published, display_order, created_at, updated_at, uploaded_by) VALUES
('School Playground', 'Our newly renovated playground equipment', '/images/gallery/playground.jpg', 'Facilities', true, true, 1, NOW(), NOW(), 'system'),
('Science Lab', 'Students conducting experiments in our modern science lab', '/images/gallery/science-lab.jpg', 'Academics', true, true, 2, NOW(), NOW(), 'system'),
('Art Class', 'Creative expression in our art classroom', '/images/gallery/art-class.jpg', 'Activities', false, true, 3, NOW(), NOW(), 'system');
