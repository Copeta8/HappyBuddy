package ara.tfg.happybuddy;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ara.tfg.happybuddy.databinding.ActivityMainUsuarioBinding;
import ara.tfg.happybuddy.model.Usuario;
import ara.tfg.happybuddy.ui.homeProfesional.HomeViewModel;
import ara.tfg.happybuddy.ui.homeUsuario.HomeUsuarioViewModel;

public class MainUsuarioActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainUsuarioBinding binding;

    private FirebaseAuth auth;
    private FirebaseFirestore mDatabase;
    FirebaseUser userFB;

    Usuario usuario;
    TextView tvNombre, tvEmail;

    String lastDocumetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeUsuarioViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeUsuarioViewModel.class);

        binding = ActivityMainUsuarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        userFB = auth.getCurrentUser();

        usuario = homeViewModel.getUsuario();

        setSupportActionBar(binding.appBarInicioUsuario.toolbarUsuario);

        DrawerLayout drawer = binding.drawerLayoutUsuario;
        NavigationView navigationView = binding.navViewUsuario;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cita, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_usuario);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view_usuario);
        View headerView = navView.getHeaderView(0);
        tvNombre = (TextView) headerView.findViewById(R.id.tvNavHNombreUser);
        tvEmail = (TextView) headerView.findViewById(R.id.tvNavHCorreoUser);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (tvEmail != null && tvNombre != null) {
            tvEmail.setText(usuario.getEmail());
            tvNombre.setText(usuario.getNombre());
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inicio_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_logout:
                auth.signOut();
                startActivity(new Intent(MainUsuarioActivity.this, LoginActivity.class));
                finish();
                return true;
            default:
                return true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_usuario);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



}