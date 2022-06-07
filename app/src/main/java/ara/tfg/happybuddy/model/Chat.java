package ara.tfg.happybuddy.model;

public class Chat {

    private String profesional_nombre;
    private String profesional_uid;
    private String usuario_nombre;
    private String usuario_uid;

    public Chat() {
    }

    public Chat(String profesional_nombre, String profesional_uid, String usuario_nombre, String usuario_uid) {
        this.profesional_nombre = profesional_nombre;
        this.profesional_uid = profesional_uid;
        this.usuario_nombre = usuario_nombre;
        this.usuario_uid = usuario_uid;
    }

    public String getProfesional_nombre() {
        return profesional_nombre;
    }

    public void setProfesional_nombre(String profesional_nombre) {
        this.profesional_nombre = profesional_nombre;
    }

    public String getProfesional_uid() {
        return profesional_uid;
    }

    public void setProfesional_uid(String profesional_uid) {
        this.profesional_uid = profesional_uid;
    }

    public String getUsuario_nombre() {
        return usuario_nombre;
    }

    public void setUsuario_nombre(String usuario_nombre) {
        this.usuario_nombre = usuario_nombre;
    }

    public String getUsuario_uid() {
        return usuario_uid;
    }

    public void setUsuario_uid(String usuario_uid) {
        this.usuario_uid = usuario_uid;
    }
}
