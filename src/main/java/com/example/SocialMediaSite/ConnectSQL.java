package com.example.SocialMediaSite;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConnectSQL implements java.io.Serializable {
    static Connection connection = null;
    static PreparedStatement statement = null;
    static ResultSet resultSet = null;
    private static HashMap<String, String> postTagAndUsers = new HashMap<String, String>();
    private static List<String> postsAndTags =new ArrayList<String>();
    private static List<String> usernames =new ArrayList<String>();
    private static String filePath = ".\\connectSQL.ser";

    public ConnectSQL() throws SQLException {
        //ConnectDatabase(database);
    }

    public boolean ConnectDatabase(String database) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.out.println("Exception " + e);
            return false;
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database + "?serverTimezone=UTC",
                    "root", "");
            return true;

        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION");
            System.out.println("SQLState");
            System.out.println("VendorError");
            /*
            System.out.println("SQL EXCEPTION" + e.getMessage());
            System.out.println("SQLState" + e.getSQLState());
            System.out.println("VendorError" + e.getErrorCode());
             */
            return false;
        }

    }
    public  boolean checkUsernamePassword(String userName, String password, PrintWriter writer,
                                             HttpServletResponse response, HttpServletRequest request){
        boolean correctLogin = false;
        try {
            ConnectDatabase("users");
            if(ConnectSQL.SQLStatement(userName).equals(password)){
                response.sendRedirect(request.getContextPath()+"/main.jsp");
                connection.close();
                //response.sendRedirect("/PostPageServlet");
                correctLogin = true;
            }
            else{
                writer.print("<script>alert(\"Wrong username or password!\");</script>");
                response.sendRedirect("index.jsp");
                correctLogin = false;
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return correctLogin;
    }

    public static String SQLStatement(String userName) throws SQLException {
        try {
            String requestQuery = String.format("SELECT password, Username\n" +
                    "FROM users\n" +
                    "INNER JOIN password ON Users.UserID=password.UserID\n" +
                    "WHERE Username = \"%s\"", userName);

            statement = connection.prepareStatement(requestQuery);
            resultSet = statement.executeQuery(requestQuery);

            while (resultSet.next()) {
                return resultSet.getString(1);
            }
            connection.endRequest();
            connection.close();
            return resultSet.getString(1);

        } catch (SQLException e) {
            /*
            System.out.println("SQL EXCEPTION" + e.getMessage());
            System.out.println("SQLState" + e.getSQLState());
            System.out.println("VendorError" + e.getErrorCode());
            */
        }
        return resultSet.getString(1);


    }


    public String getPostsSQL() throws SQLException{
        ConnectDatabase("posts");
        String post = "";
        String username = "";
        String tag = "";

        try{
            String requestQuery = "SELECT * FROM posts\n" +
                    "INNER JOIN users.users ON users.UserID=posts.UserID\n" +
                    "INNER JOIN tag ON tag.PostID=posts.PostID";
            statement = connection.prepareStatement(requestQuery);

            resultSet = statement.executeQuery(requestQuery);

            while(resultSet.next()){
                /*
                System.out.println("---------------------------------");
                System.out.println("POSTS: " + resultSet.getString("PostID"));
                System.out.println("POSTS: " + resultSet.getString("Post"));
                System.out.println("UserID: " + resultSet.getString("UserID"));
                System.out.println("USERNAME: " + resultSet.getString("Username"));
                System.out.println("---------------------------------");
                 */
                post = resultSet.getString("Post");
                username = resultSet.getString("Username");
                tag = resultSet.getString("Tag");

                postTagAndUsers.put(post+tag, username);
            }

        }catch (Exception e){
            //e.printStackTrace();
        }

        connection.close();
        for(Map.Entry<String, String> posts : postTagAndUsers.entrySet()){
            postsAndTags.add(posts.getKey());
            usernames.add(posts.getValue());
        }
        return postTagAndUsers.keySet().stream()
                .map((key) -> key + ":</br>" + postTagAndUsers.get(key) + "</h2>")
                .collect(Collectors.joining("</br></br>"));

    }
    public static void getHashMap(PrintWriter writer){
        for(Map.Entry<String, String> entry : postTagAndUsers.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            writer.print("<div class=\"posts2\">" + key + ":</br> " + value + "</br></br></div></br>");
        }
    }


    public static String generateID(char letter){
        Random rand = new Random();
        int randNum = rand.nextInt(1000);
        return letter + Integer.toString(randNum);
    }

    public static void serializeObject(ConnectSQL connectSQL) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(connectSQL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Object deSerializeObject() {
        Object object = null;
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            object = objectInputStream.readObject();
        }catch (FileNotFoundException e){
            // New Library Will be created if file is not found.
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }


    public String getUserID(String username) throws SQLException {
        ConnectDatabase("users");
        String userID = "";

        try{
            String requestQuery = String.format("SELECT UserID FROM users WHERE Username = \"%s\"", username);

            statement = connection.prepareStatement(requestQuery);

            resultSet = statement.executeQuery();
            while(resultSet.next()){
                userID += resultSet.getString(1);

            }

            connection.close();
        }catch(Exception e){

        }
        return userID;
    }


}
