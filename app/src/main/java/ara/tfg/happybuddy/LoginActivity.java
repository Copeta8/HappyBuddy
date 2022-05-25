package ara.tfg.happybuddy;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.PasswordTransformationMethod;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ara.tfg.happybuddy.model.FirebaseContract;
import ara.tfg.happybuddy.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    public final static String EXTRA_EMAIL = "ara.tfg.happubuddy.LoginActivity.EMAIL";

    private FirebaseAuth mAuth;

    private String email = "";
    private String password = "";
    private final String SharedPreferences_user = "application_user";

    private EditText etEmail;
    private EditText etPassword;
    private TextView tvResetPass;
    private TextView tvDireccion;
    private Button btLogin;
    private ImageView ivShowPass;

    private boolean isShowPassword = false;

    ArrayList<Usuario> listaUsuarios;
    ArrayList<String> usuariosDocumentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        comprobarAutenticacion();
        obtenerUsuarios();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvResetPass = findViewById(R.id.tvResetPass);
        tvDireccion = findViewById(R.id.tvDireccion);
        btLogin = findViewById(R.id.btLogin);
        ivShowPass = findViewById(R.id.ivShowPassword);
    }

    @Override
    public void onStart() {
        super.onStart();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()) {

                    Usuario usuario = null;
                    String documentID = "";

                    for (int i = 0; i < listaUsuarios.size(); i++) {

                        if (listaUsuarios.get(i).getEmail().equals(email)) {
                            usuario = listaUsuarios.get(i);
                            System.out.println("Usuario encontrado: " + usuario.getEmail() + usuario.getNombre());
                            documentID = usuariosDocumentID.get(i);
                        }
                    }

                    if (usuario != null && usuario.getUID() == null) {

                        saveActualUser(usuario);

                        Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);

                        //Se envian los datos del email, password e id del documento que referencia los datos del
                        //usuario en Firestore
                        intent.putExtra(RegistroActivity.EXTRA_EMAIL, email);
                        intent.putExtra(RegistroActivity.EXTRA_PASSWORD, password);
                        intent.putExtra(RegistroActivity.EXTRA_LAST_DOCUMENT_ID, documentID);

                        //Se envia el objeto usuario a la actividad de registro
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(RegistroActivity.EXTRA_USER, usuario);
                        intent.putExtras(bundle);

                        System.out.println("Accediendo a main desde usuarioUID nulo");

                        startActivity(intent);
                        finish();

                    } else if (usuario.getUID() != null) {

                        saveActualUser(usuario);

                        if (usuario.isAdmin()) {


                            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        System.out.println("Accediendo a mainProf desde usuarioUID no nulo");
                                        startActivity(new Intent(LoginActivity.this, MainProfesionalActivity.class));
                                        finish();

                                    } else {
                                        Toast.makeText(LoginActivity.this, R.string.error_login, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {

                            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        System.out.println("Accediendo a mainUser desde usuarioUID no nulo");

                                        startActivity(new Intent(LoginActivity.this, MainUsuarioActivity.class));
                                        finish();

                                    } else {
                                        Toast.makeText(LoginActivity.this, R.string.error_login, Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                        }
                    }
                }
            }
        });

        ivShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowPassword) {
                    etPassword.setTransformationMethod(new PasswordTransformationMethod());
                    ivShowPass.setImageDrawable(getResources().getDrawable(R.drawable.hide));
                    isShowPassword = false;
                } else {
                    etPassword.setTransformationMethod(null);
                    ivShowPass.setImageDrawable(getResources().getDrawable(R.drawable.view));
                    isShowPassword = true;
                }
            }
        });

        tvDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Se crea un intent encargado de abrir un mapa con la localizacion de la empresa
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/KZ39U9V8dGjxoEmo7"));
                //Se inicia el intent
                startActivity(intent);
            }
        });

        tvResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = etEmail.getText().toString();

                if (!email.isEmpty()) {
                    AlertDialog.Builder dialogo = new AlertDialog.Builder(LoginActivity.this);
                    dialogo.setTitle(R.string.aviso);
                    //Para que muestre correctamente el texto no puedo invocarlo desde los recursos de string.xml

                    String msgPart1 = getString(R.string.envio_contra);
                    SpannableString msg = new SpannableString(msgPart1 + "\n\n" + etEmail.getText().toString());
                    msg.setSpan(new StyleSpan(Typeface.BOLD), msgPart1.length() + 1, msgPart1.length() + etEmail.getText().toString().length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    dialogo.setMessage(msg);

                    dialogo.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mAuth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Email enviado.");
                                            }
                                        }
                                    });
                        }
                    });
                    dialogo.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    dialogo.show();
                } else {
                    Toast.makeText(LoginActivity.this, R.string.empty_fields, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void comprobarAutenticacion() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        boolean isAdmin = isAdmin();

        System.out.println("isAdmin: " + isAdmin);

        // Primero, verificamos la existencia de una sesión.
        if (auth.getCurrentUser() != null) {

            if (isAdmin) {
                startActivity(new Intent(LoginActivity.this, MainProfesionalActivity.class));
                finish();

            } else {
                // Abrimos la actividad que contiene el inicio de la funcionalidad de la app.
                startActivity(new Intent(this, MainUsuarioActivity.class));
                finish();// Cerramos la actividad.

            }
        }
    }

    public boolean isAdmin() {

        SharedPreferences prefs = getSharedPreferences(SharedPreferences_user, Context.MODE_PRIVATE);

        boolean esAdmin = false;

        String es = prefs.getString("user_admin", "");

        System.out.println(es);

        if (es.equals("true")) {
            esAdmin = true;
        } else {
            esAdmin = false;
        }

        return esAdmin;
    }

    public void obtenerUsuarios() {

        listaUsuarios = new ArrayList<Usuario>();
        usuariosDocumentID = new ArrayList<String>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(FirebaseContract.UsuariosEntry.NODE_NAME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Log.d(TAG, document.getId() + " => " + document.getData());


                        listaUsuarios.add(document.toObject(Usuario.class));
                        usuariosDocumentID.add(document.getId());
                    }
                }
            }
        });
    }

    private void saveActualUser(Usuario user) {
        SharedPreferences sharedPref = getSharedPreferences(SharedPreferences_user, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("user_name", user.getNombre());
        editor.putString("user_lasName", user.getApellidos());
        editor.putString("user_email", user.getEmail());
        editor.putString("user_telf", user.getNum_telefono());
        editor.putString("user_uid", user.getUID());

        if (user.isAdmin()) {
            editor.putString("user_admin", "true");
        } else {
            editor.putString("user_admin", "false");
        }

        editor.putString("user_direction", user.getDireccion());
        editor.putString("user_country", user.getPais());
        editor.putString("user_gender", user.getGenero());
        editor.putString("user_marital_status", user.getEstado_civil());

        editor.apply();
    }

}