package ara.tfg.happybuddy;

import static android.content.ContentValues.TAG;

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

public class LoginActivity extends AppCompatActivity {

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

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()) {

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

                } else {
                    Toast.makeText(LoginActivity.this, R.string.empty_fields, Toast.LENGTH_LONG).show();
                }
            }
        });

        tvResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = etEmail.getText().toString();

                String mensaje = R.string.envio_contra + etEmail.getText().toString();

                if (!email.isEmpty()) {
                    AlertDialog.Builder dialogo = new AlertDialog.Builder(LoginActivity.this);
                    dialogo.setTitle(R.string.aviso);
                    //Para que muestre correctamente el texto no puedo invocarlo desde los recursos de string.xml

                    String msgPart1 = getString(R.string.envio_contra);
                    SpannableString msg = new SpannableString(msgPart1 + "\n" + etEmail.getText().toString());
                    msg.setSpan(new StyleSpan(Typeface.BOLD), msgPart1.length() + 1, msgPart1.length() + etEmail.getText().toString().length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    dialogo.setMessage(msg);

                    //dialogo.setMessage(getString(R.string.envio_contra) + "\n" + etEmail.getText().toString());

                    dialogo.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mAuth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Email sent.");
                                            }
                                        }
                                    });
                        }
                    });
                    dialogo.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
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
}