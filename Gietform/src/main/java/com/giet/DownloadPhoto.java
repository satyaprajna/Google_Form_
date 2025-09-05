package com.giet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/DownloadPhoto")
public class DownloadPhoto extends HttpServlet {
    private static final String URL = "jdbc:mysql://interchange.proxy.rlwy.net:36326/placementdb";
    private static final String USER = "root";
    private static final String PASS = "TMsIzGaWwzbSzddgNOmkPpqiHQgYUsVT";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter("id");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT photo FROM interview_students WHERE id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                byte[] photoData = rs.getBytes("photo");
                if (photoData != null) {
                    resp.setContentType("image/jpeg"); // or image/png if you upload PNG
                    OutputStream os = resp.getOutputStream();
                    os.write(photoData);
                    os.flush();
                } else {
                    resp.setContentType("text/html");
                    resp.getWriter().println("<h3>No photo uploaded for this student.</h3>");
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
