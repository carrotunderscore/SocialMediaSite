package com.example.SocialMediaSite;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "PostPageServlet", value = "/PostPageServlet")
public class PostPageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {



    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        getSession(session, request, response );
        try {
            ConnectSQL connectSQL = new ConnectSQL();
            ConnectSQL.serializeObject(connectSQL);
        } catch (Exception e ) {
        }
        response.setContentType("text/html");
        response.sendRedirect("index.jsp");

        PrintWriter writer = response.getWriter();

        try {
            addPostsAndTag(request.getParameter("w3review"), request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ConnectSQL.deSerializeObject();
    }

    public static void getSession(HttpSession session, HttpServletRequest request,  HttpServletResponse response)
            throws IOException, ServletException {
        if(session.getAttribute("username") == null){
            RequestDispatcher view = request.getRequestDispatcher("index.jsp");
            view.forward(request, response);
        }
    }

    public static void addPostsAndTag(String post, HttpServletRequest request) throws SQLException {
        ConnectSQL connectSQL = new ConnectSQL();
        HttpSession session = request.getSession();


        String userID = connectSQL.getUserID((String) session.getAttribute("username"));
        connectSQL.ConnectDatabase("posts");
        List<String> hashtagList =new ArrayList<String>();

        String postID = ConnectSQL.generateID('P');

        String regexPattern = "(#\\w+)";

        String postWithoutHashtag = post;

        try{
            Pattern p = Pattern.compile(regexPattern);
            Matcher m = p.matcher(post);
            while (m.find()) {
                String hashtag = m.group(1);
                hashtagList.add(hashtag);
                postWithoutHashtag = post.replace(hashtag, "");
            }
        }catch(Exception e){
        }

        try{


            String insertIntoPosts = "INSERT INTO posts(PostID, Post, UserID)" +
                    "VALUES(?, ? , ? )";
            ConnectSQL.statement = ConnectSQL.connection.prepareStatement(insertIntoPosts);
            ConnectSQL.statement.setString(1, postID);
            ConnectSQL.statement.setString(2, postWithoutHashtag);
            ConnectSQL.statement.setString(3, userID);
            ConnectSQL.statement.executeUpdate();

            String insertIntoTags = "INSERT INTO tag(TagID, Tag, PostID)\n" +
                    "VALUES(?, ?, ?)";

            if(hashtagList.size() == 0){
                ConnectSQL.statement = ConnectSQL.connection.prepareStatement(insertIntoTags);
                ConnectSQL.statement.setString(1, ConnectSQL.generateID('T'));
                ConnectSQL.statement.setString(2, "#");
                ConnectSQL.statement.setString(3, postID);
                ConnectSQL.statement.executeUpdate();
            }

            for (String s : hashtagList) {
                ConnectSQL.statement = ConnectSQL.connection.prepareStatement(insertIntoTags);
                ConnectSQL.statement.setString(1, ConnectSQL.generateID('T'));
                ConnectSQL.statement.setString(2, s);
                ConnectSQL.statement.setString(3, postID);
                ConnectSQL.statement.executeUpdate();
            }
            ConnectSQL.connection.close();
        }catch(Exception e){
        }

    }


}
