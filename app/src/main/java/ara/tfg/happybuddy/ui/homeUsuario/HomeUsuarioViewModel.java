package ara.tfg.happybuddy.ui.homeUsuario;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

import ara.tfg.happybuddy.model.Citas;
import ara.tfg.happybuddy.model.FirebaseContract;
import ara.tfg.happybuddy.model.Usuario;

public class HomeUsuarioViewModel extends AndroidViewModel {


    private FirebaseFirestore mDatabase;
    MutableLiveData<String> citas;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();


    private final MutableLiveData<String> mText;
    private final Usuario usuario;

    public HomeUsuarioViewModel(@NonNull Application application) {
        super(application);
        mText = new MutableLiveData<>();
        //mText.setValue(usuario.getNombre());
        mDatabase = FirebaseFirestore.getInstance();

        citas = new MutableLiveData<>();

        usuario = defineUsuario();
    }


    public Usuario getUsuario() {
        return usuario;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public Usuario defineUsuario() {

        SharedPreferences prefs = getApplication().getSharedPreferences("application_user", Context.MODE_PRIVATE);

        String name, apellidos, email, telf, uid, direccion, pais, genero, estado_civil, fecha_cumple;
        boolean isAdmin;

        name = prefs.getString("user_name", "");
        apellidos = prefs.getString("user_lasName", "");
        email = prefs.getString("user_email", "");
        telf = prefs.getString("user_telf", "");
        uid = prefs.getString("user_uid", "");
        fecha_cumple = prefs.getString("user_birthday", "");

        if (prefs.getString("user_admin", "false").equals("true")) {
            System.out.println(prefs.getString("user_admin", "false"));
            isAdmin = true;
        } else {
            isAdmin = false;
        }

        direccion = prefs.getString("user_direction", "");
        pais = prefs.getString("user_country", "");
        genero = prefs.getString("user_gender", "");
        estado_civil = prefs.getString("user_marital_status", "");

        return new Usuario(uid, isAdmin, apellidos, direccion, email, estado_civil, new Timestamp(new Date()), genero, name, telf, pais);
    }

    public LiveData<String> getCitas() {

        mDatabase = FirebaseFirestore.getInstance();

        mDatabase.collection(FirebaseContract.CitasEntry.NODE_NAME).whereArrayContains(FirebaseContract.CitasEntry.PACIENTE_UID, usuario.getUID()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        citas.setValue(document.toObject(Citas.class).fechaFormatoLocal());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        System.out.println(citas);

        return citas;
    }
}








