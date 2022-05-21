package ara.tfg.happybuddy;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ara.tfg.happybuddy.databinding.ActivityMainProfesionalBinding;
import ara.tfg.happybuddy.model.Usuario;


public class MainProfesionalActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainProfesionalBinding binding;

    private FirebaseAuth auth;
    private FirebaseFirestore mDatabase;
    FirebaseUser userFB;

    ArrayList<Usuario> usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainProfesionalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar2);

        auth = FirebaseAuth.getInstance();
        userFB = auth.getCurrentUser();

        DrawerLayout drawer = binding.drawerLayoutProfesional;
        NavigationView navigationView = binding.navViewProfesional;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_prof, R.id.nav_gallery_prof)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_profesional);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        finishAffinity();
    }


    @Override
    protected void onStop() {
        super.onStop();

        finishAffinity();
    }

    /*public void defineMenu(Menu menu) {

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

                    if (esAdmin()) {
                        getMenuInflater().inflate(R.menu.inicio_happy_buddy_profesional, menu);
                    } else {
                        getMenuInflater().inflate(R.menu.inicio_happy_buddy, menu);
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }


    public boolean esAdmin() {

        boolean esAdmin = false;

        for (Usuario usuario : usuarios) {

            if (usuario.getEmail().equals(userFB.getEmail())) {

                usuario = usuario;

                if (usuario.isAdmin() == true) {
                    esAdmin = true;
                }
            }

        }
        return esAdmin;
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.inicio_happy_buddy, menu);
        //defineMenu(menu);
        getMenuInflater().inflate(R.menu.inicio_happy_buddy_profesional, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_logout:
                auth.signOut();
                startActivity(new Intent(MainProfesionalActivity.this, LoginActivity.class));
                finish();
                return true;
            case R.id.action_crearUsuario:
                startActivity(new Intent(MainProfesionalActivity.this, NuevoUsuarioActivity.class));
                finish();
                return true;
            default:
                return true;
        }
    }

        @Override
        public boolean onSupportNavigateUp () {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_profesional);
            return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                    || super.onSupportNavigateUp();
        }
    }