package ara.tfg.happybuddy.ui.homeUsuario;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import ara.tfg.happybuddy.databinding.FragmentHomeUsuarioBinding;
import ara.tfg.happybuddy.model.Usuario;

public class HomeUsuarioFragment extends Fragment {

    private FragmentHomeUsuarioBinding binding;

    private FirebaseFirestore mDatabase;
    ArrayList<Usuario> usuarios;
    Usuario usuario;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();

    String name, apellidos, email, telf, uid, direccion, pais, genero, estado_civil;
    boolean isAdmin;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        usuario = defineUsuario();

        binding = FragmentHomeUsuarioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.tvNombreProf;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        //nombre = usuario.getNombre();
        //textView.setText(nombre);

        return root;
    }


    @Override
    public void onStart() {
        super.onStart();



        binding.tvNombreProf.setText(name);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public Usuario defineUsuario() {

        SharedPreferences prefs = getActivity().getSharedPreferences("application_user", Context.MODE_PRIVATE);

        name = prefs.getString("user_name", "");
        apellidos = prefs.getString("user_lasName", "");
        email = prefs.getString("user_email", "");
        telf = prefs.getString("user_telf", "");
        uid = prefs.getString("user_uid", "");

        if (prefs.getString("user_admin", "false").equals("true")) {
            System.out.println(prefs.getString("user_admin", "false"));
            isAdmin = true;
        }else{
           isAdmin = false;
        }

        direccion = prefs.getString("user_direction","");
        pais = prefs.getString("user_country", "");
        genero = prefs.getString("user_gender", "");
        estado_civil= prefs.getString("user_marital_status", "");

        return new Usuario( uid,  isAdmin,  apellidos,  direccion,  email,  estado_civil, new Timestamp(new Date()),  genero,  name,  telf,  pais);

        /*Usuario usuario = new Usuario();

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
        });*/
    }

    /*public Usuario findByUID(String uid) {
        for (Usuario usuario : usuarios) {
            if (usuario.getUID().equals(uid)) {
                this.usuario = usuario;
            }
        }
        return usuario;
    }*/
}