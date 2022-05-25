package ara.tfg.happybuddy.ui.crear_usuario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import ara.tfg.happybuddy.R;
import ara.tfg.happybuddy.databinding.FragmentCrearUsuarioBinding;
import ara.tfg.happybuddy.model.FirebaseContract;
import ara.tfg.happybuddy.model.Usuario;

public class CrearUsuarioFragment extends Fragment {

    private FragmentCrearUsuarioBinding binding;

    FirebaseAuth auth;

    String userUID, nombre, apellidos, email, direccion, telefono, pais, genero, estadoCivil;
    boolean esAdmin;

    String master;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CrearUsuarioViewModel crearUsuarioViewModel =
                new ViewModelProvider(this).get(CrearUsuarioViewModel.class);

        binding = FragmentCrearUsuarioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textGallery;
        //crearUsuarioViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        ArrayAdapter adapterGenero = ArrayAdapter.createFromResource(getContext(), R.array.generos, android.R.layout.simple_spinner_item);
        adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spGenero.setAdapter(adapterGenero);

        ArrayAdapter adapterEstadoCivil = ArrayAdapter.createFromResource(getContext(), R.array.estados_civiles, android.R.layout.simple_spinner_item);
        adapterEstadoCivil.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spEstadoCivil.setAdapter(adapterEstadoCivil);

        binding.fabGuardarNuevoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                auth = FirebaseAuth.getInstance();

                if (binding.etNombre.getText().toString().isEmpty() || binding.etApellidos.getText().toString().isEmpty() || binding.etNuevoEmail.getText().toString().isEmpty() ||
                        binding.etDireccion.getText().toString().isEmpty() || binding.etTelefono.getText().toString().isEmpty() || binding.etPais.getText().toString().isEmpty()) {

                    Toast.makeText(getContext(), "Los campos no pueden estar vacios", Toast.LENGTH_SHORT).show();

                } else {

                    nombre = binding.etNombre.getText().toString();
                    apellidos = binding.etApellidos.getText().toString();
                    email = binding.etNuevoEmail.getText().toString();
                    direccion = binding.etDireccion.getText().toString();
                    telefono = binding.etTelefono.getText().toString();
                    pais = binding.etPais.getText().toString();
                    genero = binding.spGenero.getSelectedItem().toString();
                    estadoCivil = binding.spEstadoCivil.getSelectedItem().toString();

                    if (binding.cbTrabajador.isChecked()) {
                        esAdmin = true;
                    } else {
                        esAdmin = false;
                    }

                    Usuario usuario = new Usuario(userUID, esAdmin, apellidos, direccion, email, estadoCivil,
                            new Timestamp(new Date()), genero, nombre, telefono, pais);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection(FirebaseContract.UsuariosEntry.NODE_NAME).add(usuario);

                    borrarCampos();
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void borrarCampos() {
        binding.etNombre.setText("");
        binding.etApellidos.setText("");
        binding.etNuevoEmail.setText("");
        binding.etDireccion.setText("");
        binding.etTelefono.setText("");
        binding.etPais.setText("");
        binding.cbTrabajador.setChecked(false);
    }

}