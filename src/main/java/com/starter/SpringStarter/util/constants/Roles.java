package com.starter.SpringStarter.util.constants;

//An enum (short for "enumeration") in Java is a special data type that allows you to 
//define a collection of constants that are related to each other. These constants are 
//usually used to represent a set of predefined options or states, like days of the week, 
//directions (NORTH, SOUTH, EAST, WEST), or roles in a system (USER, ADMIN, EDITOR).
public enum Roles { // An enum defines a fixed set of constants, and no other values can be added later.
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN"), EDITOR("ROLE_EDITOR"); 
    //These constants are the only valid values for the Roles enum. No other values can be added to this enum after it has been defined.

    private String role; //This is a private field in the Roles enum that stores the string value associated with each enum constant.
    private Roles(String role) {
        this.role = role;
    }

    public String getRole(){
        return role;
    }

    //A role is a group of authorities that are typically assigned to a user or a group of users. 
    //It represents a user's position or function within the application (e.g., ADMIN, USER, MODERATOR).
    //Roles are used to determine what a user can access or do within the application. They are usually 
    //prefixed with "ROLE_" (e.g., ROLE_ADMIN, ROLE_USER).
}
