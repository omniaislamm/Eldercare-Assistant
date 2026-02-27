@echo off
cd /d "%~dp0"
java -cp "target\classes;lib\mysql-connector-j-9.5.0.jar" com.mycompany.project.Main
pause
