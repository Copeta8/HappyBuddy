package ara.tfg.happybuddy.ui.homeProfesional;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.ChangeEventListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

import ara.tfg.happybuddy.R;
import ara.tfg.happybuddy.adapters.CitasAdapter;
import ara.tfg.happybuddy.databinding.FragmentHomeProfesionalBinding;
import ara.tfg.happybuddy.model.Citas;
import ara.tfg.happybuddy.model.FirebaseContract;


public class HomeProfesionalFragment extends Fragment {

    private FragmentHomeProfesionalBinding binding;
    RecyclerView rvCitasProf;

    CitasAdapter citasAdapter;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    String usuario_uid;
    String profesional_uid;
    String fecha_cita;
    boolean esAdmin;
    RecyclerView rvNose;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeProfesionalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();

        profesional_uid = mAuth.getCurrentUser().getUid();

        binding.rvCitasProf.setLayoutManager(new LinearLayoutManager(getContext()));


        Query query = FirebaseFirestore.getInstance()
                        .collection(FirebaseContract.ProfesionalEntry.NODE_NAME)
                                .document(profesional_uid).collection(FirebaseContract.CitasEntry.NODE_NAME);

        FirestoreRecyclerOptions<Citas> options = new FirestoreRecyclerOptions.Builder<Citas>()
                .setQuery(query, Citas.class).setLifecycleOwner(this).build();

        if (citasAdapter != null) {
            citasAdapter.stopListening();
        }

        citasAdapter = new CitasAdapter(options);
        binding.rvCitasProf.setAdapter(citasAdapter);
        citasAdapter.startListening();

        citasAdapter.getSnapshots().addChangeEventListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(@NonNull ChangeEventType type, @NonNull DocumentSnapshot snapshot, int newIndex, int oldIndex) {
                binding.rvCitasProf.smoothScrollToPosition(0);
            }

            @Override
            public void onDataChanged() {

            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {

            }
        });


        //binding.rvCitasProf.setAdapter(citasAdapter);

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    /*public void defineAdaptador(){

        Query query = FirebaseFirestore.getInstance()
                .collection(FirebaseContract.ProfesionalEntry.NODE_NAME).document(mAuth.getUid())
                .collection(FirebaseContract.CitasEntry.NODE_NAME);


        /*db.collection(FirebaseContract.CitasEntry.NODE_NAME).whereArrayContains("profesional_uid", mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

            }
        });*//*

        FirestoreRecyclerOptions<Citas> options = new FirestoreRecyclerOptions.Builder<Citas>()
                .setQuery(query, Citas.class).setLifecycleOwner(this).build();

        if (citasAdapter != null) {
            citasAdapter.stopListening();
        }

        citasAdapter = new CitasAdapter(options , usuario_uid, true);

        binding.rvCitasProf.setAdapter(citasAdapter);

        citasAdapter.startListening();

        citasAdapter.getSnapshots().addChangeEventListener(new ChangeEventListener() {

            @Override
            public void onChildChanged(@NonNull ChangeEventType type, @NonNull DocumentSnapshot snapshot, int newIndex, int oldIndex) {

            }

            @Override
            public void onDataChanged() {

            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {

            }
        });
    }*/


}