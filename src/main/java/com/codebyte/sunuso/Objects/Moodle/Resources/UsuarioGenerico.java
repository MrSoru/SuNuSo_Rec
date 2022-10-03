package com.codebyte.sunuso.Objects.Moodle.Resources;

public class UsuarioGenerico {
    private String ID;
    private String UserName;
    private String FirstName;
    private String LastName;
    private String FullName;
    private String email;
    private String idNumber;
    private String Departament;
    private String Institution;
    private String Suspended;
    private String Reason;
    public UsuarioGenerico() {}

    public UsuarioGenerico(String ID, String UserName, String FirstName, String LastName, String FullName, String email, String idNumber, String Departament, String Institution) {
        this.ID = ID;
        this.UserName = UserName;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.FullName = FullName;
        this.email = email;
        this.idNumber = idNumber;
        this.Departament = Departament;
        this.Institution = Institution;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String Reason) {
        this.Reason = Reason;
    }
    
    public String getSuspended() {
        return Suspended;
    }

    public void setSuspended(String Suspended) {
        this.Suspended = Suspended;
    }

    public String getDepartament() {
        return this.Departament;
    }

    public void setDepartament(String Departament) {
        this.Departament = Departament;
    }

    public String getInstitution() {
        return this.Institution;
    }

    public void setInstitution(String Institution) {
        this.Institution = Institution;
    }

    public String getID() {
        return this.ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return this.UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getFirstName() {
        return this.FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public String getLastName() {
        return this.LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public String getFullName() {
        return this.FullName;
    }

    public void setFullName(String FullName) {
        this.FullName = FullName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdNumber() {
        return this.idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String toString() {
        return "Usuario{ID=" + this.ID + ", UserName=" + this.UserName + ", FirstName=" + this.FirstName + ", LastName=" + this.LastName + ", FullName=" + this.FullName + ", email=" + this.email + ", idNumber=" + this.idNumber + ", Departament=" + this.Departament + ", Institution=" + this.Institution + "}";
    }

    public String DataStirngCSV() {
        return this.ID + "," + this.ID + "," + this.UserName + "," + this.FirstName + "," + this.LastName + "," + this.idNumber + "," + this.Departament;
    }

    public String DataStringHeaderCSV() {
        return "ID_Moodle,Usuario,Nombre,Apellidos,NumeroÚnico,Departamento,Institución";
    }

    public boolean IsEmpty() {
        return (this.ID.isBlank() && this.FirstName.isBlank() && this.FullName.isBlank() && this.LastName.isBlank() && this.email.isBlank() && this.idNumber.isBlank() && this.UserName.isBlank() && this.Departament.isBlank() && this.Institution.isBlank());
    }

    public boolean isNullData() {
        return (this.ID == null && this.FirstName == null && this.FullName == null && this.LastName == null && this.email == null && this.idNumber == null && this.UserName == null && this.Departament == null && this.Institution == null);
    }
}
