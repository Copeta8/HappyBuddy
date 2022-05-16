package ara.tfg.happybuddy.ui.home;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ara.tfg.happybuddy.model.Usuario;

public class HomeViewModel extends ViewModel {


    private FirebaseFirestore mDatabase;
    ArrayList<Usuario> usuarios;
    Usuario usuario;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();

    String nombre;

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        //mText.setValue(usuario.getNombre());
    }

    public LiveData<String> getText() {
        return mText;
    }


    public Usuario findByUID(String uid) {
        for (Usuario usuario : usuarios) {
            if (usuario.getUID().equals(uid)) {
                this.usuario = usuario;
            }
        }
        return usuario;
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


}




