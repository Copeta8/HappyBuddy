package ara.tfg.happybuddy.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Citas {

    @ServerTimestamp
    Timestamp fecha;

    private String paciente_UID;
    private String profesional_UID;

    public Citas() {
    }

    public Citas(Timestamp fecha, String paciente_UID, String profesional_UID) {
        this.fecha = fecha;
        this.paciente_UID = paciente_UID;
        this.profesional_UID = profesional_UID;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public String getPaciente_UID() {
        return paciente_UID;
    }

    public void setPaciente_UID(String paciente_UID) {
        this.paciente_UID = paciente_UID;
    }

    public String getProfesional_UID() {
        return profesional_UID;
    }

    public void setProfesional_UID(String profesional_UID) {
        this.profesional_UID = profesional_UID;
    }

    public String fechaFormatoLocal() {

        long unix_seconds = fecha.getSeconds();

        //convert seconds to milliseconds
        Date date = new Date(unix_seconds * 1000);

        // format of the date
        SimpleDateFormat jdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        return jdf.format(date);
    }
}
