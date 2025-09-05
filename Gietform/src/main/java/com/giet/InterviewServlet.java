package com.giet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/InterviewServlet")
@MultipartConfig(maxFileSize = 16177215) // 16 MB
public class InterviewServlet extends HttpServlet {
    private static final String URL = "jdbc:mysql://interchange.proxy.rlwy.net:36326/placementdb";
    private static final String USER = "root";
    private static final String PASS = "TMsIzGaWwzbSzddgNOmkPpqiHQgYUsVT";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        String rollno = req.getParameter("rollno");
        String name = req.getParameter("name");
        String clgmail = req.getParameter("clgmail");
        String branch = req.getParameter("branch");
        int semester = Integer.parseInt(req.getParameter("semester"));
        double cgpa = Double.parseDouble(req.getParameter("cgpa"));
        String skills = req.getParameter("skills");

        Part photoPart = req.getPart("photo");
        InputStream photoInput = (photoPart != null && photoPart.getSize() > 0) ? photoPart.getInputStream() : null;

        Part resumePart = req.getPart("resume");
        InputStream resumeInput = (resumePart != null && resumePart.getSize() > 0) ? resumePart.getInputStream() : null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);

            String sql = "INSERT INTO interview_students (rollno, name, clgmail, branch, semester, cgpa, skills, photo, resume) VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, rollno);
            ps.setString(2, name);
            ps.setString(3, clgmail);
            ps.setString(4, branch);
            ps.setInt(5, semester);
            ps.setDouble(6, cgpa);
            ps.setString(7, skills);
            if (photoInput != null) ps.setBlob(8, photoInput); else ps.setNull(8, Types.BLOB);
            if (resumeInput != null) ps.setBlob(9, resumeInput); else ps.setNull(9, Types.BLOB);

            ps.executeUpdate();

            out.println("<html><body style='font-family:Arial;text-align:center;padding:50px;'>");
            out.println("<h2 style='color:green;'>✅ Application Submitted Successfully!</h2>");
            out.println("<p><b>Roll No:</b> " + rollno + "</p>");
            out.println("<p><b>Name:</b> " + name + "</p>");
            out.println("<p><b>Email:</b> " + clgmail + "</p>");
            out.println("<p><b>Branch:</b> " + branch + "</p>");
            out.println("<p><b>Semester:</b> " + semester + "</p>");
            out.println("<p><b>CGPA:</b> " + cgpa + "</p>");
            out.println("<p><b>Skills:</b> " + skills + "</p>");
            out.println("<p>Your <b>Resume</b> has been uploaded.</p>");
            out.println("</body></html>");

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
        }
    }
}
