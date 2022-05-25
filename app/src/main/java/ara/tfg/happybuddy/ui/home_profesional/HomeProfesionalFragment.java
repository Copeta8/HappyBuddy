package ara.tfg.happybuddy.ui.home_profesional;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.ChangeEventListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;


import ara.tfg.happybuddy.adapters.CitasProfAdapter;
import ara.tfg.happybuddy.databinding.FragmentHomeProfesionalBinding;
import ara.tfg.happybuddy.model.Citas;
import ara.tfg.happybuddy.model.FirebaseContract;


public class HomeProfesionalFragment extends Fragment {

    private FragmentHomeProfesionalBinding binding;
    RecyclerView rvCitasProf;

    CitasProfAdapter citasProfAdapter;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    String usuario_uid;
    String profesional_uid;
    String fecha_cita;
    boolean esAdmin;
    RecyclerView rvNose;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeProfesionalViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeProfesionalViewModel.class);

        binding = FragmentHomeProfesionalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();

        //profesional_uid = mAuth.getUid();

        binding.rvCitasProf.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onStart() {
        super.onStart();

        Query query = FirebaseFirestore.getInstance()
                .collection(FirebaseContract.ProfesionalEntry.NODE_NAME)
                .document(mAuth.getUid()).collection(FirebaseContract.CitasEntry.NODE_NAME).orderBy(FirebaseContract.CitasEntry.FECHA, Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Citas> options = new FirestoreRecyclerOptions.Builder<Citas>()
                .setQuery(query, Citas.class).setLifecycleOwner(this).build();

        if (citasProfAdapter != null) {
            citasProfAdapter.stopListening();
        }

        citasProfAdapter = new CitasProfAdapter(options);
        binding.rvCitasProf.setAdapter(citasProfAdapter);
        citasProfAdapter.startListening();

        citasProfAdapter.getSnapshots().addChangeEventListener(new ChangeEventListener() {
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
    }

}