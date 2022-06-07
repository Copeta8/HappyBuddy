package ara.tfg.happybuddy.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * * Clase utilizada para contener y trabajar con los datos de las conferencias.
 */
public class Mensaje {

    String usuario;
    String mensaje;

    @ServerTimestamp //permite que sea el servidor el que asigne la hora al crear el documento
    Date fechaEnvio;

    public Mensaje() {
    }

    public Mensaje(String usuario, String body) {
        this.usuario = usuario;
        this.mensaje = body;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
}
