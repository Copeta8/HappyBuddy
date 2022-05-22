package ara.tfg.happybuddy;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import ara.tfg.happybuddy.databinding.ActivityRegistroBinding;
import ara.tfg.happybuddy.model.FirebaseContract;
import ara.tfg.happybuddy.model.Profesional;
import ara.tfg.happybuddy.model.Usuario;

public class RegistroActivity extends AppCompatActivity {

    ActivityRegistroBinding binding;

    public final static String EXTRA_EMAIL = "ara.tfg.happybuddy.RegistroActivity.correo";
    public final static String EXTRA_PASSWORD = "ara.tfg.happybuddy.RegistroActivity.password";
    public final static String EXTRA_LAST_DOCUMENT_ID = "ara.tfg.happybuddy.RegistroActivity.last_document_id";
    public final static String EXTRA_USER = "ara.tfg.happybuddy.RegistroActivity.user";


    TextView tvCorreo;
    EditText etRegPassword, etRepPassword, etMaster;
    FloatingActionButton fabGuardarUsuario;
    CheckBox cbMostrarContrasenya;

    Usuario usuario;

    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    String email, password, lastDocumentID, master;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();

        tvCorreo = findViewById(R.id.tvCorreo);
        etRegPassword = findViewById(R.id.etRegistroPassword);
        etRepPassword = findViewById(R.id.etRegistroPasswordRepe);
        etMaster = findViewById(R.id.etMaster);
        fabGuardarUsuario = findViewById(R.id.fabGuardarUsuario);
        cbMostrarContrasenya = findViewById(R.id.cbMostrarContrasenya);

        email = getIntent().getStringExtra(EXTRA_EMAIL);
        password = getIntent().getStringExtra(EXTRA_PASSWORD);
        lastDocumentID = getIntent().getStringExtra(EXTRA_LAST_DOCUMENT_ID);

        Bundle bundle = getIntent().getExtras();
        usuario = bundle.getParcelable(EXTRA_USER);


        tvCorreo.setText(getResources().getText(R.string.tu_correo) + " " + email);
        etRegPassword.setText(password);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (usuario.isAdmin()) {
            etMaster.setVisibility(View.VISIBLE);
        }

        cbMostrarContrasenya.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    etRegPassword.setTransformationMethod(new PasswordTransformationMethod());
                    etRepPassword.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    etRegPassword.setTransformationMethod(null);
                    etRepPassword.setTransformationMethod(null);
                }
            }
        });


        fabGuardarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String password = etRegPassword.getText().toString();
                String passwordRepe = etRepPassword.getText().toString();

                if (password.equals(passwordRepe)) {

                    if (!usuario.isAdmin()) {
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "createUserWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();

                                            usuario.setUID(user.getUid());

                                            db = FirebaseFirestore.getInstance();
                                            db.collection(FirebaseContract.UsuariosEntry.NODE_NAME).document(user.getUid()).set(usuario);
                                            db.collection(FirebaseContract.UsuariosEntry.NODE_NAME).document(lastDocumentID).delete();

                                            //Si el usuario es admin se lleva a cabo el proceso de creacion del profesional

                                            startActivity(new Intent(RegistroActivity.this, MainUsuarioActivity.class));
                                            finish();
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(RegistroActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    } else if (usuario.isAdmin() && !etMaster.getText().toString().isEmpty()) {

                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "createUserWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();

                                            usuario.setUID(user.getUid());

                                            db = FirebaseFirestore.getInstance();
                                            db.collection(FirebaseContract.UsuariosEntry.NODE_NAME).document(user.getUid()).set(usuario);
                                            db.collection(FirebaseContract.UsuariosEntry.NODE_NAME).document(lastDocumentID).delete();

                                            master = etMaster.getText().toString();

                                            Profesional profesional = new Profesional(usuario.getNombre() + " " + usuario.getApellidos(), master, usuario.getUID(), new Timestamp(new Date()));

                                            db.collection(FirebaseContract.ProfesionalEntry.NODE_NAME).document(profesional.getusuario_uid()).set(profesional);

                                            //Si el usuario es admin se lleva a cabo el proceso de creacion del profesional

                                            startActivity(new Intent(RegistroActivity.this, MainProfesionalActivity.class));
                                            finish();


                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(RegistroActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }else{
                        Toast.makeText(RegistroActivity.this, "Debe introducir un máster", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    etRepPassword.setError(getResources().getText(R.string.contrasenya_no_coincide));
                }
            }
        });

    }
}