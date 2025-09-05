# Use official Tomcat image with Java 17
FROM tomcat:10.1-jdk17   # ✅ picks Tomcat 10 + JDK 17 (good for Servlets/JSP)

# Remove default ROOT webapp
RUN rm -rf /usr/local/tomcat/webapps/ROOT   # ✅ removes the Tomcat welcome page

# Copy your WAR file into Tomcat
COPY myproject.war /usr/local/tomcat/webapps/ROOT.war  
# ✅ takes your WAR (must be in same folder as this Dockerfile) 
# ✅ renames it to ROOT.war → app runs at "/"

# Expose port 8080
EXPOSE 8080   

# Start Tomcat
CMD ["catalina.sh", "run"]   # ✅ runs Tomcat in foreground
