package de.notev2.notev2.dto;


public class UserResponse {
    private Long id;
    private String username;
    private boolean admin;

    public UserResponse(Long long1, String string, Boolean boolean1) {
        this.id = long1;
        this.username = string;
        this.admin = boolean1;
    }
   

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    
}
