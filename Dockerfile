# Use official Tomcat image with Java 17
FROM tomcat:10.1-jdk17

# Remove default ROOT webapp
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy your WAR file into Tomcat
COPY myproject.war /usr/local/tomcat/webapps/ROOT.war

# Expose port 8080
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]

