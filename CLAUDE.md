# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

### Development
```bash
# Run the application (development)
mvnw.cmd spring-boot:run

# Run with production profile
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod

# Build the project
mvnw.cmd clean install

# Run specific tests (if test files exist)
mvnw.cmd test

# Package as JAR
mvnw.cmd clean package
```

### Database Operations
```bash
# Create PostgreSQL database
psql -U postgres
CREATE DATABASE realestate;

# Backup database
pg_dump -U postgres -d realestate > backup.sql

# Restore database
psql -U postgres -d realestate < backup.sql
```

## Architecture

### Technology Stack
- **Framework**: Spring Boot 3.3.5 with Java 17
- **Database**: PostgreSQL (production), H2 (development fallback)
- **ORM**: Spring Data JPA / Hibernate
- **Security**: Spring Security with custom UserDetailsService
- **Templates**: Thymeleaf with AdminLTE v3.2.0
- **Build Tool**: Maven

### Project Structure
```
com.hunch.realestate/
├── config/                 # Configuration classes (Security, JPA)
├── domain/
│   ├── entity/             # JPA entities (Property, User, CompanyInfo)
│   └── dto/                # Data Transfer Objects
├── repository/             # Spring Data JPA repositories
├── service/                # Business logic layer
├── web/
│   ├── controller/pages/   # Thymeleaf page controllers
│   └── controller/api/     # REST API controllers
└── common/enums/           # Enums (PropertyType, TransactionType, RoomType)
```

### Database Schema
The application uses JPA entities with these main tables:
- `properties` - Real estate property listings with comprehensive details
- `users` - User authentication and authorization
- `company_info` - Company information management

Key entities follow the pattern: BaseEntity (audit fields) → specific entities.

### Security Configuration
- Uses Spring Security with database-backed authentication
- UserRepository provides custom UserDetailsService
- Form-based login with admin dashboard redirect
- Role-based access control (ADMIN, USER roles)
- Session management with proper logout handling

### Template Architecture
Currently undergoing AdminLTE v3 migration:
- Uses Thymeleaf Layout Dialect with `layouts/admin/layout.html`
- Fragment-based structure: head.html, navbar.html, sidebar.html, footer.html, scripts.html
- AdminLTE v3 components integrated (DataTables, Select2, Chart.js, etc.)

### Configuration Profiles
- **Development** (`application.yml`): PostgreSQL with SQL logging, hot reload enabled
- **Production** (`application-prod.yml`): Environment variables, connection pooling, caching enabled

### File Storage
Application uses configurable file storage paths:
- Base path: `C:/git/study/real-estate/data` (dev) or `${STORAGE_BASE_PATH}` (prod)
- Images: `{base-path}/images`
- Documents: `{base-path}/documents`
- JSON data files: `{base-path}/*.json` (legacy support)

## Important Notes

### Database Setup
PostgreSQL must be running on port 5432 with database `realestate` created. Default credentials are `postgres/postgres` but should be configured via environment variables in production.

### Environment Variables (Production)
```
DB_URL=jdbc:postgresql://localhost:5432/realestate
DB_USERNAME=postgres
DB_PASSWORD=your_secure_password
SERVER_PORT=8080
STORAGE_BASE_PATH=C:/realestate/data
LOG_PATH=C:/realestate/logs
```

### AdminLTE Migration Status
The project is actively migrating to AdminLTE v3. Some pages may still use independent templates instead of the layout system. Reference `ADMINLTE_REFACTORING.md` for detailed migration progress and requirements.

### Real Estate Domain
This is a comprehensive real estate management system with:
- Property listings with detailed information (price, location, features, images)
- Support for multiple transaction types (매매/전세/월세)
- Property types and room configurations
- Manager contact information and availability scheduling
- Image management and photo notes
- Administrative features for property status management

When working with property-related code, reference the Property entity for complete field mapping and the existing Thymeleaf templates for UI patterns.