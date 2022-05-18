package ara.tfg.happybuddy.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Usuario {

    private String UID;
    private boolean admin;
    private String apellidos;
    private String direccion;
    private String email;
    private String estado_civil;

    @ServerTimestamp
    private Timestamp fecha_nacimiento;
    private String genero;
    private String nombre;
    private String num_telefono;
    private String pais;


    public Usuario(String UID, boolean admin, String apellidos, String direccion, String email, String estado_civil, Timestamp fecha_nacimiento, String genero, String nombre, String num_telefono, String pais) {
        this.UID = UID;
        this.admin = admin;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.email = email;
        this.estado_civil = estado_civil;
        this.fecha_nacimiento = fecha_nacimiento;
        this.genero = genero;
        this.nombre = nombre;
        this.num_telefono = num_telefono;
        this.pais = pais;
    }

    public Usuario() {
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEstado_civil() {
        return estado_civil;
    }

    public void setEstado_civil(String estado_civil) {
        this.estado_civil = estado_civil;
    }

    public Timestamp getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Timestamp fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNum_telefono() {
        return num_telefono;
    }

    public void setNum_telefono(String num_telefono) {
        this.num_telefono = num_telefono;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String obtenerFechaFormatoLocal() {
        long unix_seconds = fecha_nacimiento.getSeconds();

        //convert seconds to milliseconds
        Date date = new Date(unix_seconds*1000L);

        // format of the date
        SimpleDateFormat jdf = new SimpleDateFormat("dd-MM-yyyy");

        return jdf.format(date);
    }
}
