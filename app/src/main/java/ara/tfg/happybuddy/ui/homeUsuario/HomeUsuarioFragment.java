package ara.tfg.happybuddy.ui.homeUsuario;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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
        HomeUsuarioViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeUsuarioViewModel.class);

        usuario = homeViewModel.getUsuario();

        System.out.println("Usuario: " + usuario.getNombre());

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

        String nombre = usuario.getNombre();


       // binding.tvNombreProf.setText(nombre);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}