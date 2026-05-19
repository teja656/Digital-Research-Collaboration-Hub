<div align="center">

# 🔬 ResearchSphere

### Digital Research Collaboration Hub

*A full-stack Java web platform for managing research projects, teams, tasks, milestones, discussions, and file sharing — with role-based access for admins, faculty, and students.*

<br/>

[![Java](https://img.shields.io/badge/Java-11-orange?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring MVC](https://img.shields.io/badge/Spring_MVC-5.3-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/)
[![Hibernate](https://img.shields.io/badge/Hibernate-5.6-59666C?style=for-the-badge&logo=hibernate&logoColor=white)](https://hibernate.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)](https://getbootstrap.com/)

<br/>

[Quick Start](#-quick-start) · [Features](#-features) · [Demo Login](#-demo-accounts) · [Docs](#-documentation) · [Troubleshooting](#-troubleshooting)

</div>

---

## ✨ Overview

**ResearchSphere** helps academic and research teams collaborate in one place: create projects, assign tasks, track milestones, host discussions, upload documents, and view progress on an interactive dashboard — all secured by authentication and role-based permissions.

| Role | What they can do |
|------|------------------|
| **ADMIN** | Full access — users, projects, teams, tasks, milestones, discussions, file uploads, admin panel |
| **FACULTY** | Manage projects, teams, tasks, milestones, discussions, and uploads |
| **STUDENT** | View projects, manage **My Tasks**, participate in discussions |

---

## 🚀 Quick Start

### Prerequisites

| Tool | Notes |
|------|--------|
| **JDK 11+** (17+ recommended) | [Adoptium](https://adoptium.net/) |
| **Maven** | Bundled via `START.bat`, or install globally |
| **MySQL** | [XAMPP](https://www.apachefriends.org/) or standalone MySQL 8 |
| **Git** | Optional — for cloning the repo |

### Run in 3 steps

```text
1. Start MySQL (XAMPP Control Panel → MySQL → Running)
2. Double-click START.bat
3. Open http://localhost:8080/ResearchSphere/login
```

`START.bat` will:

- Build the project (`mvn clean package`)
- Start **Jetty** on port **8080**
- Import seed data **only on first run** (your data is kept on restart)
- Reset demo passwords if needed

> **First run** may take a few minutes while Maven downloads dependencies.

### Manual setup (Eclipse / Tomcat)

<details>
<summary><b>Click to expand alternative setup</b></summary>

1. Import `sql/researchsphere_db.sql` into MySQL → database `researchsphere_db`
2. Set MySQL credentials in `src/main/resources/hibernate.cfg.xml` (default: `root` / `root`)
3. Build: `mvn clean package`
4. Deploy `target/ResearchSphere.war` to **Tomcat 9**, or run `mvn jetty:run`
5. Open [http://localhost:8080/ResearchSphere/login](http://localhost:8080/ResearchSphere/login)

Copy `config/local.properties.example` → `config/local.properties` for local overrides (gitignored).

</details>

---

## 🔐 Demo Accounts

| Email | Password | Role |
|-------|----------|------|
| `admin@researchsphere.edu` | `password123` | ADMIN |
| `faculty@researchsphere.edu` | `password123` | FACULTY |
| `student@researchsphere.edu` | `password123` | STUDENT |
| `emily@researchsphere.edu` | `password123` | STUDENT |

---

## 🧩 Features

<table>
<tr>
<td width="50%">

### 📊 Dashboard
- Project & task statistics
- Chart.js visualizations
- Activity overview

### 📁 Projects
- Create, edit, delete projects
- Link teams & members
- Upload / download files (PDF, DOC, TXT ≤ 10 MB)

### ✅ Tasks
- CRUD with status updates
- Role-scoped views (students see **My Tasks**)

</td>
<td width="50%">

### 👥 Teams
- Team CRUD
- Add / remove members

### 🎯 Milestones
- Track progress & deadlines

### 💬 Discussions
- Threads, comments, delete

### 🛡️ Security
- Login, register, logout, remember-me
- Session filters & SHA-256 passwords
- Role-based servlet & JSP access

</td>
</tr>
</table>

**Also included:** Admin panel · HTTP methods demo (`/ResearchSphere/http-methods`) · Notifications & activity log

---

## 🏗️ Tech Stack

```text
┌─────────────────────────────────────────────────────────┐
│  Browser  →  JSP + Bootstrap 5 + Chart.js + JSTL        │
├─────────────────────────────────────────────────────────┤
│  Spring MVC 5.3  (Controllers)                          │
│  Servlets      (Auth, File upload/download)               │
│  Filters       (Encoding, Authentication)                 │
├─────────────────────────────────────────────────────────┤
│  Services  →  DAOs  →  Hibernate 5.6  →  MySQL           │
├─────────────────────────────────────────────────────────┤
│  Jetty (dev) / Tomcat 9 (deploy)  ·  Maven WAR build      │
└─────────────────────────────────────────────────────────┘
```

---

## 📂 Project Structure

```text
ResearchSphere/
├── START.bat                 # One-click build + run
├── scripts/                  # PowerShell automation
├── sql/researchsphere_db.sql # Schema + seed data
├── src/main/java/com/researchsphere/
│   ├── controller/           # Spring MVC
│   ├── servlet/              # Login, files, register
│   ├── service/ · dao/ · entity/
│   ├── filter/ · util/
├── src/main/webapp/
│   ├── WEB-INF/jsp/          # App pages
│   ├── jsp/                  # Login, register
│   └── css/ · js/
├── docs/                     # Viva & flow guides
└── uploads/                  # User files (gitignored)
```

---

## 📖 Documentation

| Guide | Description |
|-------|-------------|
| [docs/VIVA_AND_CONCEPTS.md](docs/VIVA_AND_CONCEPTS.md) | Viva Q&A, architecture concepts |
| [docs/FLOW_AND_FILE_GUIDE.md](docs/FLOW_AND_FILE_GUIDE.md) | Request flow & file map |
| [docs/SETUP_QUICK.txt](docs/SETUP_QUICK.txt) | Short setup reference |
| [START-HERE.txt](START-HERE.txt) | Run guide & troubleshooting |

---

## 🛠️ Troubleshooting

| Issue | Fix |
|-------|-----|
| **503 / context unavailable** | Run `START.bat` again (rebuilds with latest fixes) |
| **Invalid login** | Ensure MySQL is running; `START.bat` repairs demo passwords |
| **Data missing after restart** | Normal on first fix — now data persists; only empty DB gets seed import |
| **Upload fails** | Use PDF/DOC/TXT under 10 MB; check `uploads/researchsphere-uploads/` |
| **Port 8080 in use** | Stop other servers or change port in `pom.xml` |

**Stop the server:** `Ctrl+C` in the `START.bat` window

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 License

This project is provided for **academic and educational use**. Add a license file if you plan to distribute it publicly.

---

<div align="center">

**Built with ☕ for research collaboration**

[⬆ Back to top](#-researchsphere)

</div>
