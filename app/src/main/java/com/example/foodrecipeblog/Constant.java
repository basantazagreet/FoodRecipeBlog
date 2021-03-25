package com.example.foodrecipeblog;

public class Constant {

    public static final String URL = "http://192.168.1.126:8000/";
    public static final String HOME = URL+"api";
    public static final String LOGIN = HOME+"/login";
    public static final String PROFILE = HOME+"/profileinfo";


    public static final String LOGOUT = HOME+"/logout";
    public static final String REGISTER = HOME+"/register";
    public static final String SAVE_USER_INFO = HOME+"/save_user_info";
    public static final String RECIPES = HOME+"/recipes";
    public static final String ADD_RECIPE = RECIPES+"/create";
    public static final String UPDATE_RECIPE = RECIPES+"/update";
    public static final String DELETE_RECIPE = RECIPES+"/delete";
    public static final String LIKE_RECIPE = RECIPES+"/like";
    public static final String COMMENTS = RECIPES+"/comments";
    public static final String CREATE_COMMENT = HOME+"/comments/create";
    public static final String DELETE_COMMENT = HOME+"/comments/delete";
    public static final String MY_RECIPE = RECIPES+"/my_posts";



}
