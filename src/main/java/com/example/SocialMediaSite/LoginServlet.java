package com.example.SocialMediaSite;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            ConnectSQL connectSQL = new ConnectSQL();
            doGet(request, response);
            response.setContentType("text/html");

            PrintWriter writer = response.getWriter();
            String userName = request.getParameter("username");
            String password = request.getParameter("password");
            if(connectSQL.checkUsernamePassword(userName, password, writer, response, request)){
                HttpSession session = request.getSession();
                session.setAttribute("username", userName);
                //User.setUserID(connectSQL.getUserID(User.getUser()));
            };
        } catch (Exception e) {
        }

    }



}
