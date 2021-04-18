package com.example.SocialMediaSite;

import javax.servlet.http.HttpSession;

public class User {
    private static String user;
    private static String userID;
    private static HttpSession session;
    public User(){
    }

    public static void setUser(String user){
        User.user = user;
    }
    public static String getUser(){
        return user;
    }

    public static void setUserID(String user){
        User.user = user;
    }
    public static String getUserID(){
        return user;
    }

    public static void setSession(HttpSession session, String username){
        User.session = (HttpSession) session.getAttribute(username);
    }
    public static String getSession(String username){
        return (String) session.getAttribute(username);
    }




}
