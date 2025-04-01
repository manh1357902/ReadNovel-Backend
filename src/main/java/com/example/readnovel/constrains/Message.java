package com.example.readnovel.constrains;

public class Message {
    public static final String SUCCESS = "Success";
    public static final String FAILED = "Failed";
    public static final String LOGIN_FAILED = "Email or password is incorrect";

    //email
    public static final String EMAIL_NOT_FOUND = "Email not found";
    public static final String EMAIL_NOT_EMPTY = "Email cant not be empty";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String EMAIL_NOT_FORMAT = "Email not formatted correctly";

    //password
    public static final String PASSWORD_NOT_EMPTY = "Password cant not be empty";
    public static final String PASSWORD_NOT_CORRECT_FORMAT =  "Password must include at least one uppercase letter, one lowercase letter, one number, and be at least 6 characters long";
    public static final String CONFIRM_PASSWORD_NOT_CORRECT = "Confirm password not correct";
    public static final String CONFIRM_EMAIL_NOT_EMPTY = "Confirm email not empty";

    //validate
    public static final String CREATE_ACCOUNT_FAILED = "Create account failed";

    //novel
    public static final String TITLE_NOT_BLANK = "Title not blank";
    public static final String DESCRIPTION_NOT_BLANK = "Description not blank";
    public static final String IMAGE_NOT_BLANK = "Image not blank";
    public static final String AUTHOR_NOT_BLANK = "Author not blank";
    public static final String CATEGORY_NOT_EMPTY = "Category not empty";
    public static final String NOVEL_NOT_FOUND = "Novel not found";
    public static final String FILE_SIZE_TOO_LARGE = "File size must be less than 5MB";
    public static final String RESOURCE_NOT_FOUND = "API not exists!";
}
