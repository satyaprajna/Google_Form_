package com.giet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/AdminPortalServlet")
public class AdminPortalServlet extends HttpServlet {
    private static final String URL = "jdbc:mysql://interchange.proxy.rlwy.net:36326/placementdb";
    private static final String USER = "root";
    private static final String PASS = "TMsIzGaWwzbSzddgNOmkPpqiHQgYUsVT"; // change as needed

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String password = req.getParameter("password");

        if (!"12345".equals(password)) {
            resp.setContentType("text/html");
            resp.getWriter().println("<h2 style='color:red; text-align:center;'>❌ Wrong Password</h2>"
                    + "<p style='text-align:center;'><a href='adminPortal.html'>Try Again</a></p>");
            return;
        }

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
        out.println("<title>Admin Dashboard</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; background:#f1f3f4; }");
        out.println("header { background:#1a73e8; color:white; padding:20px; text-align:center; }");
        out.println("table { border-collapse: collapse; width:95%; margin:20px auto; background:white; box-shadow:0 6px 20px rgba(0,0,0,0.1);} ");
        out.println("th, td { border:1px solid #ddd; padding:10px; text-align:center; }");
        out.println("th { background:#1a73e8; color:white; }");
        out.println("tr:nth-child(even){ background:#f9f9f9; }");
        out.println("tr:hover{ background:#e3f2fd; }");
        out.println("a.download { padding:6px 12px; background:#4CAF50; color:white; text-decoration:none; border-radius:4px; }");
        out.println("a.photo { padding:6px 12px; background:#2196F3; color:white; text-decoration:none; border-radius:4px; }");
        out.println("</style></head><body>");

        out.println("<header><h1>📊 GIET University - Admin Dashboard</h1><p>All Registered Students</p></header>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM interview_students");

            out.println("<table>");
            out.println("<tr><th>ID</th><th>Roll No</th><th>Name</th><th>Email</th><th>Branch</th><th>Sem</th><th>CGPA</th><th>Skills</th><th>Photo</th><th>Resume</th></tr>");

            while (rs.next()) {
                int id = rs.getInt("id");
                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + rs.getString("rollno") + "</td>");
                out.println("<td>" + rs.getString("name") + "</td>");
                out.println("<td>" + rs.getString("clgmail") + "</td>");
                out.println("<td>" + rs.getString("branch") + "</td>");
                out.println("<td>" + rs.getInt("semester") + "</td>");
                out.println("<td>" + rs.getDouble("cgpa") + "</td>");
                out.println("<td>" + rs.getString("skills") + "</td>");
                out.println("<td><a class='photo' href='DownloadPhoto?id=" + id + "'>📷 View</a></td>");
                out.println("<td><a class='download' href='DownloadResume?id=" + id + "'>📥 Download</a></td>");
                out.println("</tr>");
            }
            out.println("</table>");

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3 style=color:red; text-align:center;'>❌ Error: " + e.getMessage() + "</h3>");
        }

        out.println("</body></html>");
    }
}
