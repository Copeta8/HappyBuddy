package ara.tfg.happybuddy;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ara.tfg.happybuddy.databinding.ActivityInicioHappyBuddyBinding;
import ara.tfg.happybuddy.model.Usuario;

public class InicioHappyBuddyActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityInicioHappyBuddyBinding binding;

    private FirebaseAuth auth;
    private FirebaseFirestore mDatabase;
    FirebaseUser userFB;

    ArrayList<Usuario> usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInicioHappyBuddyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        userFB = auth.getCurrentUser();


        setSupportActionBar(binding.appBarInicioHappyBuddy.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cita, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_inicio_happy_buddy);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    public void defineMenu(Menu menu) {

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

                    for (Usuario usuario : usuarios) {
                        if (usuario.getUID().equals(userFB.getUid())) {
                            if (usuario.isAdmin() == true){
                                getMenuInflater().inflate(R.menu.inicio_happy_buddy_profesional, menu);
                            }else{
                                getMenuInflater().inflate(R.menu.inicio_happy_buddy, menu);
                            }
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.inicio_happy_buddy, menu);
        defineMenu(menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_logout:
                auth.signOut();
                startActivity(new Intent(InicioHappyBuddyActivity.this, LoginActivity.class));
                finish();
                return true;
            case R.id.action_crearUsuario:
                startActivity(new Intent(InicioHappyBuddyActivity.this, NuevoUsuarioActivity.class));
                finish();
                return true;
            default:
                return true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_inicio_happy_buddy);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}