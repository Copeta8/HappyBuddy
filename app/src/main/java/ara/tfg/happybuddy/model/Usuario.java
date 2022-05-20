package ara.tfg.happybuddy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Usuario implements Parcelable {

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

    protected Usuario(Parcel in) {
        UID = in.readString();
        admin = in.readByte() != 0;
        apellidos = in.readString();
        direccion = in.readString();
        email = in.readString();
        estado_civil = in.readString();
        fecha_nacimiento = in.readParcelable(Timestamp.class.getClassLoader());
        genero = in.readString();
        nombre = in.readString();
        num_telefono = in.readString();
        pais = in.readString();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(UID);
        parcel.writeByte((byte) (admin ? 1 : 0));
        parcel.writeString(apellidos);
        parcel.writeString(direccion);
        parcel.writeString(email);
        parcel.writeString(estado_civil);
        parcel.writeParcelable(fecha_nacimiento, i);
        parcel.writeString(genero);
        parcel.writeString(nombre);
        parcel.writeString(num_telefono);
        parcel.writeString(pais);
    }
}
