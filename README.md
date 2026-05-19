# ResearchSphere – Digital Research Collaboration Hub

Full-stack Java web app: Servlet, JSP, Hibernate, Spring MVC, MySQL, Bootstrap 5.

**Path:** C:\Users\Puneeth\ResearchSphere

## Quick start

1. Import `sql/researchsphere_db.sql` into MySQL (database: researchsphere_db).
2. Set MySQL user/password in `src/main/resources/hibernate.cfg.xml` (default root/root).
3. Build: Maven goal `clean package` in Eclipse or `mvn clean package`.
4. Deploy `target/ResearchSphere.war` to Tomcat 9.
5. Open http://localhost:8080/ResearchSphere/login

**Demo accounts** (password: password123):
- admin@researchsphere.edu (ADMIN)
- faculty@researchsphere.edu (FACULTY)
- student@researchsphere.edu (STUDENT)

## Docs

- docs/VIVA_AND_CONCEPTS.md
- docs/FLOW_AND_FILE_GUIDE.md

## Eclipse

Import as Maven project, add Tomcat 9 runtime, Run on Server.

See full structure and viva notes in docs folder.
