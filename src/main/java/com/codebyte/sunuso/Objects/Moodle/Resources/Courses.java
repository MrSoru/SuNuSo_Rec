package com.codebyte.sunuso.Objects.Moodle.Resources;

public class Courses {
    private String ID;

    private String nombreCorto;

    private String NombreCompleto;

    private String NombreMostrado;

    private String IDNumber;

    private String Idioma;

    private String Descripcion;

    public Courses() {}

    public Courses(String ID, String nombreCorto, String NombreCompleto, String NombreMostrado, String IDNumber, String Idioma) {
        this.ID = ID;
        this.nombreCorto = nombreCorto;
        this.NombreCompleto = NombreCompleto;
        this.NombreMostrado = NombreMostrado;
        this.IDNumber = IDNumber;
        this.Idioma = Idioma;
    }

    public String getDescripcion() {
        return this.Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public String getID() {
        return this.ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNombreCorto() {
        return this.nombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    public String getNombreCompleto() {
        return this.NombreCompleto;
    }

    public void setNombreCompleto(String NombreCompleto) {
        this.NombreCompleto = NombreCompleto;
    }

    public String getNombreMostrado() {
        return this.NombreMostrado;
    }

    public void setNombreMostrado(String NombreMostrado) {
        this.NombreMostrado = NombreMostrado;
    }

    public String getIDNumber() {
        return this.IDNumber;
    }

    public void setIDNumber(String IDNumber) {
        this.IDNumber = IDNumber;
    }

    public String getIdioma() {
        return this.Idioma;
    }

    public void setIdioma(String Idioma) {
        this.Idioma = Idioma;
    }

    public boolean IsEmpty() {
        return (this.ID.isBlank() && this.IDNumber.isBlank() && this.Idioma.isBlank() && this.NombreCompleto.isBlank() && this.NombreMostrado.isBlank() && this.nombreCorto.isBlank());
    }

    public boolean isNulldata() {
        return (this.ID == null && this.IDNumber == null && this.Idioma == null && this.NombreCompleto == null && this.NombreMostrado == null && this.nombreCorto == null);
    }

    public String toString() {
        return "Courses{ID=" + this.ID + ", nombreCorto=" + this.nombreCorto + ", NombreCompleto=" + this.NombreCompleto + ", NombreMostrado=" + this.NombreMostrado + ", IDNumber=" + this.IDNumber + ", Idioma=" + this.Idioma + "}";
    }
}
