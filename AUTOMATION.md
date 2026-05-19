# One-Click Automation

**Double-click `START.bat`** — that is the only command you need.

See **START-HERE.txt** for the 3-step user workflow.

The launcher automatically:
- Checks Java
- Finds and connects to MySQL (tries common passwords; asks once if needed)
- Attempts to start MySQL Windows service
- Updates `hibernate.cfg.xml`
- Imports `researchsphere_db`
- Downloads Maven to `.tools/` if missing
- Builds the WAR
- Runs embedded Tomcat 9
- Opens the login page in your browser

No Eclipse. No VS Code. No manual Maven. No Tomcat install.
