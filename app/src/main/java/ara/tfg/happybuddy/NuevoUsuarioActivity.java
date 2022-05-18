package ara.tfg.happybuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import ara.tfg.happybuddy.databinding.ActivityNuevoUsuarioBinding;
import ara.tfg.happybuddy.databinding.FragmentCitasBinding;
import ara.tfg.happybuddy.model.FirebaseContract;
import ara.tfg.happybuddy.model.Usuario;

public class NuevoUsuarioActivity extends AppCompatActivity {

    Spinner spGenero, spEstadoCivil;
    EditText etNombre, etApellidos, etEmail, etDireccion, etTelefono, etPais;
    CheckBox cbTrabajador;
    FloatingActionButton fabGuardar;

    FirebaseAuth auth;

    String userUID, nombre, apellidos, email, direccion, telefono, pais, genero, estadoCivil;
    boolean esAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_usuario);

        FirebaseAuth.getInstance().signOut();


        spGenero = findViewById(R.id.spGenero);
        spEstadoCivil = findViewById(R.id.spEstadoCivil);
        etNombre = findViewById(R.id.etNombre);
        etApellidos = findViewById(R.id.etApellidos);
        etEmail = findViewById(R.id.etNuevoEmail);
        etDireccion = findViewById(R.id.etDireccion);
        etTelefono = findViewById(R.id.etTelefono);
        etPais = findViewById(R.id.etPais);
        cbTrabajador = findViewById(R.id.cbTrabajador);
        fabGuardar = findViewById(R.id.fabGuardarNuevoUsuario);

        ArrayAdapter adapterGenero = ArrayAdapter.createFromResource(this, R.array.generos, android.R.layout.simple_spinner_item);
        adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGenero.setAdapter(adapterGenero);

        ArrayAdapter adapterEstadoCivil = ArrayAdapter.createFromResource(this, R.array.estados_civiles, android.R.layout.simple_spinner_item);
        adapterEstadoCivil.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEstadoCivil.setAdapter(adapterEstadoCivil);

        fabGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                auth = FirebaseAuth.getInstance();

                if (etNombre.getText().toString().isEmpty() || etApellidos.getText().toString().isEmpty() || etEmail.getText().toString().isEmpty() ||
                        etDireccion.getText().toString().isEmpty() || etTelefono.getText().toString().isEmpty() || etPais.getText().toString().isEmpty()) {

                    Toast.makeText(NuevoUsuarioActivity.this, "Los campos no pueden estar vacios", Toast.LENGTH_SHORT).show();

                } else {

                    nombre = etNombre.getText().toString();
                    apellidos = etApellidos.getText().toString();
                    email = etEmail.getText().toString();
                    direccion = etDireccion.getText().toString();
                    telefono = etTelefono.getText().toString();
                    pais = etPais.getText().toString();
                    genero = spGenero.getSelectedItem().toString();
                    estadoCivil = spEstadoCivil.getSelectedItem().toString();

                    if (cbTrabajador.isChecked()) {
                        esAdmin = true;
                    } else {
                        esAdmin = false;
                    }

                    Usuario usuario = new Usuario(userUID, esAdmin, apellidos, direccion, email, estadoCivil,
                            new Timestamp(new Date()), genero, nombre, telefono, pais);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection(FirebaseContract.UsuariosEntry.NODE_NAME).add(usuario);

                }
            }
        });


    }

}




