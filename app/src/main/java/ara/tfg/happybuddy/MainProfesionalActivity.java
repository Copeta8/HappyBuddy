package ara.tfg.happybuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import ara.tfg.happybuddy.databinding.ActivityMainProfesionalBinding;
import ara.tfg.happybuddy.model.Usuario;
import ara.tfg.happybuddy.ui.home_usuario.HomeUsuarioViewModel;


public class MainProfesionalActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainProfesionalBinding binding;

    private FirebaseAuth auth;
    private FirebaseFirestore mDatabase;
    FirebaseUser userFB;

    //ArrayList<Usuario> usuarios;
    Usuario usuario;
    TextView tvNombre, tvEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HomeUsuarioViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeUsuarioViewModel.class);

        binding = ActivityMainProfesionalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMainProfesional.toolbarProfesional);

        auth = FirebaseAuth.getInstance();
        userFB = auth.getCurrentUser();

        usuario = homeViewModel.getUsuario();


        System.out.println("usuario: " + usuario.getNombre());

        DrawerLayout drawer = binding.drawerLayoutProfesional;
        NavigationView navigationView = binding.navViewProfesional;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_prof, R.id.nav_crear_usuario)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_profesional);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view_profesional);
        View headerView = navView.getHeaderView(0);
        tvNombre = (TextView) headerView.findViewById(R.id.tvNavHNombreProf);
        tvEmail = (TextView) headerView.findViewById(R.id.tvNavHCorreoProf);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayoutProfesional, binding.appBarMainProfesional.toolbarProfesional, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        binding.drawerLayoutProfesional.addDrawerListener(actionBarDrawerToggle);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (tvNombre != null && tvEmail != null) {

            tvNombre.setText(usuario.getNombre() + " " + usuario.getApellidos());
            tvEmail.setText(usuario.getEmail());
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

    public Usuario defineUsuario() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String name, apellidos, email, telf, uid, direccion, pais, genero, estado_civil;
        boolean isAdmin;

        name = prefs.getString("user_name", "");
        apellidos = prefs.getString("user_lasName", "");
        email = prefs.getString("user_email", "");
        telf = prefs.getString("user_telf", "");
        uid = prefs.getString("user_uid", "");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.inicio_happy_buddy, menu);
        //defineMenu(menu);
        getMenuInflater().inflate(R.menu.inicio_main, menu);

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
            default:
                return true;
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_profesional);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}