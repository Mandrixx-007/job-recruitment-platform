\# JobHub — Job Recruitment Platform



A full-stack job recruitment platform built with \*\*Spring Boot\*\*, \*\*PostgreSQL\*\*, and \*\*Thymeleaf\*\*. Supports three roles — Job Seeker, Employer, and Admin — with a complete application lifecycle.



\## Tech Stack



\- Java 21

\- Spring Boot 4.0.8

\- Spring Security (role-based auth)

\- Spring Data JPA / Hibernate

\- PostgreSQL 16

\- Thymeleaf + Bootstrap 5

\- Maven



\## Features



\### Job Seeker

\- Register / login

\- Edit profile (bio, CV URL, experience)

\- Browse and search jobs

\- Apply to jobs with CV URL + cover letter

\- Track application status (Pending → Reviewing → Shortlisted → Interview → Accepted/Rejected)



\### Employer

\- Register / login

\- Company profile setup

\- Post, close, and delete jobs

\- View applicants per job

\- Update application status

\- Schedule interviews



\### Admin

\- Platform statistics dashboard

\- User management (enable/disable accounts)

\- Job moderation (delete inappropriate jobs)



\## Getting Started



\### Prerequisites

\- Java 21+

\- PostgreSQL 16+

\- Maven (or use the bundled Maven Wrapper `mvnw`)



\### Setup



1\. Clone the repository:

&#x20;  git clone https://github.com/Mandrixx-007/job-recruitment-platform.git

&#x20;  cd job-recruitment-platform



2\. Create the PostgreSQL database:

&#x20;  CREATE DATABASE jobportal\_db;



3\. Configure database credentials in src/main/resources/application.properties:

&#x20;  spring.datasource.url=jdbc:postgresql://localhost:5432/jobportal\_db

&#x20;  spring.datasource.username=postgres

&#x20;  spring.datasource.password=YOUR\_PASSWORD



4\. Run the application:

&#x20;  ./mvnw spring-boot:run



5\. Open: http://localhost:8080



\## Project Structure



src/main/java/com/example/jobportal/

├── config/          # Security configuration

├── controller/      # Web controllers

├── dto/             # Form objects

├── model/           # JPA entities

├── repository/      # Spring Data JPA repositories

└── service/         # Business logic



src/main/resources/

├── templates/       # Thymeleaf HTML pages

└── static/          # Static assets



\## Database Schema



9 entities: User, Company, EmployerProfile, JobSeekerProfile, Job, Application, Skill, Education, Interview



Key relationships:

\- User (OneToOne) JobSeekerProfile

\- User (OneToOne) EmployerProfile

\- EmployerProfile (ManyToOne) Company

\- Job (ManyToOne) Company

\- Application (ManyToOne) Job + JobSeekerProfile



\## Deployment



Deployed on Render — Live URL: coming soon



\## License



Built for educational purposes.

