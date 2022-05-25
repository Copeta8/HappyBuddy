package ara.tfg.happybuddy.ui.home_usuario;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import ara.tfg.happybuddy.model.Usuario;

public class HomeUsuarioViewModel extends AndroidViewModel {


    private FirebaseFirestore mDatabase;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();

    private final MutableLiveData<String> mText;
    private final Usuario usuario;

    public HomeUsuarioViewModel(@NonNull Application application) {
        super(application);
        mText = new MutableLiveData<>();
        //mText.setValue(usuario.getNombre());
        mDatabase = FirebaseFirestore.getInstance();

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

}








