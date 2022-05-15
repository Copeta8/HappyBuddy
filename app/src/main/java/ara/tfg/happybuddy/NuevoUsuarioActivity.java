package ara.tfg.happybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class NuevoUsuarioActivity extends AppCompatActivity {

    Spinner spGenero;
    Spinner spEstadoCivil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_usuario);

        spGenero = findViewById(R.id.spGenero);
        spEstadoCivil = findViewById(R.id.spEstadoCivil);

        ArrayAdapter adapterGenero = ArrayAdapter.createFromResource(this, R.array.generos, android.R.layout.simple_spinner_item);
        adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGenero.setAdapter(adapterGenero);

        ArrayAdapter adapterEstadoCivil = ArrayAdapter.createFromResource(this, R.array.estados_civiles, android.R.layout.simple_spinner_item);
        adapterEstadoCivil.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEstadoCivil.setAdapter(adapterEstadoCivil);
    }
}