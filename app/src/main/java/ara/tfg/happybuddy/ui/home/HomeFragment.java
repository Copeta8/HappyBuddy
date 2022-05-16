package ara.tfg.happybuddy.ui.home;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;

import ara.tfg.happybuddy.databinding.FragmentHomeBinding;
import ara.tfg.happybuddy.model.Usuario;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private FirebaseFirestore mDatabase;
    ArrayList<Usuario> usuarios;
    Usuario usuario;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();

    String nombre;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        defineUsuario();
        usuario = findByUID(user.getUid());

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.tvSaludo;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        //nombre = usuario.getNombre();
        //textView.setText(nombre);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void defineUsuario() {

        mDatabase = FirebaseFirestore.getInstance();

        usuarios = new ArrayList<Usuario>();

        mDatabase.collection("usuarios").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        usuarios.add(document.toObject(Usuario.class));
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public Usuario findByUID(String uid) {
        for (Usuario usuario : usuarios) {
            if (usuario.getUID().equals(uid)) {
                this.usuario = usuario;
            }
        }
        return usuario;
    }
}