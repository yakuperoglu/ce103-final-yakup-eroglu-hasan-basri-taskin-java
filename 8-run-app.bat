@echo off
@setlocal enableextensions
@cd /d "%~dp0"

echo Running Application
java -jar librarysystem-app/target/librarysystem-app-1.0-SNAPSHOT.jar

echo Operation Completed!
pause