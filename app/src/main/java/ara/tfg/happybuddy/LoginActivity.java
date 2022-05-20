package ara.tfg.happybuddy;

import static android.content.ContentValues.TAG;

import static ara.tfg.happybuddy.RegistroActivity.EXTRA_PASSWORD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ara.tfg.happybuddy.model.FirebaseContract;
import ara.tfg.happybuddy.model.Profesional;
import ara.tfg.happybuddy.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    public final static String EXTRA_EMAIL = "ara.tfg.happubuddy.LoginActivity.EMAIL";

    private FirebaseAuth mAuth;

    private String email = "";
    private String password = "";

    private EditText etEmail;
    private EditText etPassword;
    private TextView tvResetPass;
    private TextView tvDireccion;
    private Button btLogin;
    private ImageView ivShowPass;

    private boolean isShowPassword = false;

    ArrayList<Usuario> listaUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        comprobarAutenticacion();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvResetPass = findViewById(R.id.tvResetPass);
        tvDireccion = findViewById(R.id.tvDireccion);
        btLogin = findViewById(R.id.btLogin);
        ivShowPass = findViewById(R.id.ivShowPassword);

        //obtenerUsuarios();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()) {

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    //Se incializa la lista que contendra las conferencias
                   // listaUsuarios = new ArrayList<Usuario>();

                    //Se llama a Firestore para obtener los documentos de conferencias
                    db.collection(FirebaseContract.UsuariosEntry.NODE_NAME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            //Si se han cargado los documentos
                            if (task.isSuccessful()) {
                                //Se van añadiendo a la lista de usuarios
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    //listaUsuarios.add(document.toObject(Usuario.class));

                                    //Si el UID del objeto almacenado en Firestore es nulo, entonces se crea inicia un intent
                                    //para que el usuario pueda terminar el registro en FirebaseAuth.
                                    if (document.toObject(Usuario.class).getEmail().equals(email) && document.toObject(Usuario.class).getUID() == null) {

                                        Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);

                                        //Se envian los datos del email, password e id del documento que referencia los datos del
                                        //usuario en Firestore
                                        intent.putExtra(RegistroActivity.EXTRA_EMAIL, email);
                                        intent.putExtra(RegistroActivity.EXTRA_PASSWORD, password);
                                        intent.putExtra(RegistroActivity.EXTRA_LAST_DOCUMENT_ID, document.getId());

                                        //Se envia el objeto usuario a la actividad de registro
                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable(RegistroActivity.EXTRA_USER, document.toObject(Usuario.class));
                                        intent.putExtras(bundle);

                                        startActivity(intent);
                                        finish();

                                    } else if(document.toObject(Usuario.class).getUID() != null){
                                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                if (task.isSuccessful()) {

                                                    if (document.toObject(Usuario.class).isAdmin()) {
                                                        startActivity(new Intent(LoginActivity.this, MainActivityProfesional.class));
                                                        finish();
                                                    }else{
                                                        startActivity(new Intent(LoginActivity.this, InicioHappyBuddyActivity.class));
                                                        finish();
                                                    }

                                                } else {
                                                    Toast.makeText(LoginActivity.this, R.string.error_login, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });


                    /*for (Usuario usuario : listaUsuarios) {

                        if (usuario.getEmail().equals(email) && usuario.getUID() == null) {
                            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);

                            intent.putExtra(RegistroActivity.EXTRA_EMAIL, email);
                            intent.putExtra(RegistroActivity.EXTRA_PASSWORD, password);
                            startActivity(intent);
                            finish();

                        } else {
                            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(LoginActivity.this, InicioHappyBuddyActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, R.string.error_login, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }*/

                } else {
                    Toast.makeText(LoginActivity.this, R.string.empty_fields, Toast.LENGTH_LONG).show();
                }
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
    }

    private void comprobarAutenticacion() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        // Primero, verificamos la existencia de una sesión.
        if (auth.getCurrentUser() != null) {
            finish();// Cerramos la actividad.
            // Abrimos la actividad que contiene el inicio de la funcionalidad de la app.
            startActivity(new Intent(this, InicioHappyBuddyActivity.class));
        }
    }

    /*private void obtenerUsuarios() {
        //Se crea una instancia de Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Se incializa la lista que contendra las conferencias
        listaUsuarios = new ArrayList<Usuario>();

        //Se llama a Firestore para obtener los documentos de conferencias
        db.collection(FirebaseContract.UsuariosEntry.NODE_NAME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //Si se han cargado los documentos
                if (task.isSuccessful()) {
                    //Se van añadiendo a la lista de usuarios
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        listaUsuarios.add(document.toObject(Usuario.class));
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }*/
}