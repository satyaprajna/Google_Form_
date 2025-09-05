package com.giet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/DownloadResume")
public class DownloadResume extends HttpServlet {
    private static final String URL = "jdbc:mysql://interchange.proxy.rlwy.net:36326/placementdb";
    private static final String USER = "root";
    private static final String PASS = "TMsIzGaWwzbSzddgNOmkPpqiHQgYUsVT"; // change if needed

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter("id");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT resume FROM interview_students WHERE id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                byte[] resumeData = rs.getBytes("resume");
                if (resumeData != null) {
                    resp.setContentType("application/pdf");
                    resp.setHeader("Content-Disposition", "attachment; filename=resume_" + id + ".pdf");
                    OutputStream os = resp.getOutputStream();
                    os.write(resumeData);
                    os.flush();
                } else {
                    resp.setContentType("text/html");
                    resp.getWriter().println("<h3>No resume uploaded for this student.</h3>");
                }
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            resp.setContentType("text/html");
            resp.getWriter().println("<h3 style='color:red;'>❌ Error: " + e.getMessage() + "</h3>");
        }
    }
}
