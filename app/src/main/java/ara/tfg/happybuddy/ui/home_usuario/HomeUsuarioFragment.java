package ara.tfg.happybuddy.ui.home_usuario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import ara.tfg.happybuddy.databinding.FragmentHomeUsuarioBinding;
import ara.tfg.happybuddy.model.Usuario;

public class HomeUsuarioFragment extends Fragment {

    private FragmentHomeUsuarioBinding binding;

    private FirebaseFirestore mDatabase;
    ArrayList<Usuario> usuarios;
    Usuario usuario;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeUsuarioViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeUsuarioViewModel.class);

        usuario = homeViewModel.getUsuario();

        System.out.println("Usuario: " + usuario.getNombre());

        binding = FragmentHomeUsuarioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.tvProximaCita;
        final TextView tvProximaCita = binding.tvProximasCitas;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        //nombre = usuario.getNombre();
        //textView.setText(nombre);

        //homeViewModel.getCitas().observe(getViewLifecycleOwner(), tvProximaCita::setText);

        //tvProximaCita.setText(""+homeViewModel.getCitas().get(1).getFecha());


        return root;



    }


    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }




}