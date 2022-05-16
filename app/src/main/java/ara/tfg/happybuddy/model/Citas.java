package ara.tfg.happybuddy.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Citas {

    @ServerTimestamp
    Timestamp fecha;

    private String paciente_UID;

    public Citas() {
    }

    public Citas(Timestamp fecha, String paciente_UID) {
        this.fecha = fecha;
        this.paciente_UID = paciente_UID;
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

    public String fechaFormatoLocal() {
        long unix_seconds = fecha.getSeconds();

        //convert seconds to milliseconds
        Date date = new Date(unix_seconds*1000L);

        // format of the date
        SimpleDateFormat jdf = new SimpleDateFormat("dd-MM-yyyy");

        return jdf.format(date);
    }
}
