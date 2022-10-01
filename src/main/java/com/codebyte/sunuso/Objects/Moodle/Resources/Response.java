package com.codebyte.sunuso.Objects.Moodle.Resources;

public class Response {
    String ID;

    String Username;

    public Response() {}

    public Response(String ID, String Username) {
        this.ID = ID;
        this.Username = Username;
    }

    public String getID() {
        return this.ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return this.Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String toString() {
        return "Response{ID=" + this.ID + ", Username=" + this.Username + "}";
    }

    public boolean IsEmpty() {
        return (this.ID.isBlank() && this.Username.isBlank());
    }

    public boolean isNullData() {
        return (this.ID == null && this.Username == null);
    }
}
