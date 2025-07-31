# Primary School Website

A comprehensive Spring Boot web application for a primary school with secure admission system and Razorpay payment integration.

## Features

### Public Website
- Responsive design optimized for all devices
- Home page with featured content and news
- About Us, Academics, Contact pages
- News and Events section
- Photo Gallery
- Online Admission Form with Payment Integration

### Admin Panel
- Secure admin authentication
- Application management dashboard
- Content management system
- User management
- Payment tracking

### Security Features
- Spring Security with BCrypt password encoding
- Role-based access control
- CSRF protection
- Secure headers configuration
- Session management

### Payment Integration
- Razorpay payment gateway integration
- Secure payment verification
- Payment status tracking
- Receipt generation

## Technologies Used

- **Backend**: Spring Boot 3.2.0, Spring Security, Spring Data JPA
- **Database**: MySQL (Production), H2 (Development)
- **Frontend**: HTML5, CSS3, JavaScript, Bootstrap
- **Payment**: Razorpay Java SDK
- **Build Tool**: Maven
- **Java Version**: 17

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+ (for production)
- Razorpay account (for payment processing)

## Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd primary-school-website
```

### 2. Database Configuration

#### For Development (H2 Database)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### For Production (MySQL)
1. Create a MySQL database named `primary_school_db`
2. Update `src/main/resources/application.properties` with your database credentials
3. Run the application:
```bash
mvn spring-boot:run
```

### 3. Razorpay Configuration
1. Sign up at [Razorpay](https://razorpay.com/)
2. Get your API keys from the dashboard
3. Update the following properties in `application.properties`:
```properties
razorpay.key.id=your_key_id
razorpay.key.secret=your_secret_key
```

### 4. Run the Application
```bash
mvn clean install
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

## Default Admin Credentials
- **Username**: admin
- **Password**: admin123

**⚠️ Important**: Change these credentials before deploying to production!

## API Endpoints

### Public Endpoints
- `GET /` - Home page
- `GET /admissions` - Admission form page
- `POST /api/admission/apply` - Submit admission application
- `POST /api/payment/verify` - Verify payment

### Admin Endpoints (Requires Authentication)
- `GET /admin/dashboard` - Admin dashboard
- `GET /admin/applications` - Manage applications
- `GET /admin/content` - Content management

## Payment Flow

1. User fills out admission form
2. Application is created with PAYMENT_PENDING status
3. Razorpay order is generated
4. User completes payment through Razorpay
5. Payment is verified using webhook/callback
6. Application status is updated to UNDER_REVIEW
7. Admin can review and approve/reject applications

## Development Profiles

- **default**: Production configuration (MySQL)
- **dev**: Development configuration (H2 in-memory database)
- **test**: Test configuration (H2 in-memory database)

## Security Considerations

1. **HTTPS**: Enable HTTPS in production
2. **Database Security**: Use strong database credentials
3. **API Keys**: Keep Razorpay keys secure and use environment variables
4. **Session Security**: Configure secure session cookies
5. **Input Validation**: All forms include server-side validation
6. **SQL Injection**: Using JPA with parameterized queries
7. **XSS Protection**: Content Security Policy headers configured

## Deployment

### Docker Deployment
```bash
# Build the application
mvn clean package

# Create and run Docker container
docker build -t primary-school-website .
docker run -p 8080:8080 primary-school-website
```

### Traditional Deployment
1. Build the JAR file: `mvn clean package`
2. Copy `target/primary-school-website-0.0.1-SNAPSHOT.jar` to your server
3. Run: `java -jar primary-school-website-0.0.1-SNAPSHOT.jar`

## Testing

### Run Tests
```bash
mvn test
```

### Test Payment Integration
Use Razorpay test cards:
- **Success**: 4111 1111 1111 1111
- **Failure**: 4000 0000 0000 0002

## Support

For technical support or questions, please contact [admin@primaryschool.edu](mailto:admin@primaryschool.edu)

## License

This project is licensed under the MIT License - see the LICENSE file for details.
