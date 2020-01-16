#!/bin/bash
java -Dspring.profiles.active=production -Dserver.port=80 -jar moj-autoservis-backend/target/my-auto-service-1.0.0-SNAPSHOT.jar
