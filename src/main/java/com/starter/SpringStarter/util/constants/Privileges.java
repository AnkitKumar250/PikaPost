package com.starter.SpringStarter.util.constants;

public enum Privileges {
    RESET_ANY_USER_PASSWORD(1l, "RESET_ANY_USER_PASSWORD"),
    ACCESS_ADMIN_PANEL(2l, "ACCESS_ADMIN_PANEL");

    private Long id;
    private String privilege;
    private Privileges(Long id, String privilege){
        this.id = id;
        this.privilege = privilege;
    }

    public Long getId(){
        return id;
    }

    public String getPrivilege(){
        return privilege;
    }

    //uthorities are specific permissions or privileges granted to a user (e.g., READ_PRIVILEGE, WRITE_PRIVILEGE). 
    //They are finer-grained than roles and usually represent specific actions a user can perform.
    //Authorities are used to check if a user has permission to perform certain operations, regardless of their role.    
}
