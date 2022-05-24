package ara.tfg.happybuddy.ui.ver_citas;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.ChangeEventListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ara.tfg.happybuddy.adapters.CitasUserAdapter;
import ara.tfg.happybuddy.databinding.FragmentVerCitasUserBinding;
import ara.tfg.happybuddy.model.Citas;
import ara.tfg.happybuddy.model.FirebaseContract;
import ara.tfg.happybuddy.model.Profesional;

public class VerCitasFragment extends Fragment {

    private FragmentVerCitasUserBinding binding;

    ArrayList<Profesional> listaProfesionales;
    Profesional profesionalElegido;
    ArrayList<Citas> listaCitas;

    CitasUserAdapter citasAdapter;

    FirebaseAuth mAuth;
    FirebaseFirestore db;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        VerCitasModel verCitasModel =
                new ViewModelProvider(this).get(VerCitasModel.class);

        binding = FragmentVerCitasUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textSlideshow;
        // verCitasModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        obtenerProfesionales();

        return root;
    }


    @Override
    public void onStart() {
        super.onStart();

        //obtenerProfesionales();

        binding.spnProfesionalVerCitas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                for (Profesional prof : listaProfesionales) {

                    if (prof.getNombre_completo().equals(binding.spnProfesionalVerCitas.getSelectedItem().toString())) {
                        profesionalElegido = prof;
                    }
                }

                defineAdaptador();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (listaProfesionales.size() > 0) {
                    profesionalElegido = listaProfesionales.get(0);
                }
            }
        });

        //defineAdaptador();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void obtenerProfesionales() {
        //Se crea una instancia de Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Se incializa la lista que contendra las conferencias
        listaProfesionales = new ArrayList<Profesional>();

        //Se llama a Firestore para obtener los documentos de conferencias
        db.collection(FirebaseContract.ProfesionalEntry.NODE_NAME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //Si se han cargado los documentos
                if (task.isSuccessful()) {
                    //Se van añadiendo a la lista de conferencias
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        listaProfesionales.add(document.toObject(Profesional.class));
                        System.out.println(document.getId() + " => " + document.getData());
                    }
                    //LLamada a un método
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
                cargaSpinner();
            }
        });


    }

    public void cargaSpinner() {
        //Se crea una lista que contenga el nombre de las conferencias
        List nombreConf = new ArrayList<>();

        //Se añaden los nombre a la lista
        for (Profesional prof :
                listaProfesionales) {
            nombreConf.add(prof.getNombre_completo());
            System.out.println(prof.getNombre_completo());
        }

        //Se crea un adaptador para el spinner y se le asignan los valores de la lista creada anteriormente
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, nombreConf);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnProfesionalVerCitas.setAdapter(adapter);

        if (!listaProfesionales.isEmpty()) {
            profesionalElegido = listaProfesionales.get(binding.spnProfesionalVerCitas.getSelectedItemPosition());
        }

    }

    public void defineAdaptador(){

        String usuarioUID = mAuth.getUid();
        System.out.println("Usuario UID: " + usuarioUID);

        binding.rvCitasUsuarios.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = FirebaseFirestore.getInstance()
                .collection(FirebaseContract.ProfesionalEntry.NODE_NAME)
                .document(profesionalElegido.getusuario_uid()).collection(FirebaseContract.CitasEntry.NODE_NAME).orderBy(FirebaseContract.CitasEntry.FECHA, Query.Direction.DESCENDING).whereEqualTo(FirebaseContract.CitasEntry.PACIENTE_UID, usuarioUID);

        FirestoreRecyclerOptions<Citas> options = new FirestoreRecyclerOptions.Builder<Citas>().setQuery(query, Citas.class).setLifecycleOwner(this).build();

        if (citasAdapter != null) {
            citasAdapter.stopListening();
        }

        citasAdapter = new CitasUserAdapter(options);
        binding.rvCitasUsuarios.setAdapter(citasAdapter);
        citasAdapter.startListening();

        citasAdapter.getSnapshots().addChangeEventListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(@NonNull ChangeEventType type, @NonNull DocumentSnapshot snapshot, int newIndex, int oldIndex) {
                //binding.rvCitasUsuarios.smoothScrollToPosition(0);
            }

            @Override
            public void onDataChanged() {

            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {

            }
        });


    }
}