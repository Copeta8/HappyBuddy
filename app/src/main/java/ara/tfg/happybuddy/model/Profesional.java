package ara.tfg.happybuddy.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class Profesional {

    private String nombre_completo;
    private String master;
    private String usuario_uid;

    @ServerTimestamp
    private Timestamp inicio_trabajo;

    public Profesional() {
    }

    public Profesional(String nombre_completo, String master, String usuario_uid, Timestamp inicio_trabajo) {
        this.nombre_completo = nombre_completo;
        this.master = master;
        this.usuario_uid = usuario_uid;
        this.inicio_trabajo = inicio_trabajo;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getusuario_uid() {
        return usuario_uid;
    }

    public void setusuario_uid(String usuario) {
        this.usuario_uid = usuario;
    }

    public Timestamp getInicio_trabajo() {
        return inicio_trabajo;
    }

    public void setInicio_trabajo(Timestamp inicio_trabajo) {
        this.inicio_trabajo = inicio_trabajo;
    }
}
