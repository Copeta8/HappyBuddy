package ara.tfg.happybuddy.ui.nueva_cita;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ara.tfg.happybuddy.R;
import ara.tfg.happybuddy.databinding.FragmentNuevaCitaBinding;
import ara.tfg.happybuddy.model.Citas;
import ara.tfg.happybuddy.model.FirebaseContract;
import ara.tfg.happybuddy.model.Profesional;

public class NuevasCitasFragment extends Fragment {

    private FragmentNuevaCitaBinding binding;
    int hour, minute;

    private FirebaseAuth auth;


    ArrayList<Profesional> listaProfesionales;
    private Profesional profesionalElegido;

    ArrayList<Citas> listaCitas;

    String hora, dia;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NuevasCitasViewModel nuevasCitasViewModel =
                new ViewModelProvider(this).get(NuevasCitasViewModel.class);

        binding = FragmentNuevaCitaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        auth = FirebaseAuth.getInstance();

        binding.ivHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int iHour, int iMinute) {
                        hour = iHour;
                        minute = iMinute;

                        binding.tvChosenHour.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                        hora = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), onTimeSetListener, hour, minute, true);

                timePickerDialog.setTitle("Selecciona una hora");
                timePickerDialog.show();

            }


        });

        binding.cvApointmentDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                //String selectedDate = sdf.format(new Date(binding.cvApointmentDate.getDate()));

                binding.tvChosenDate.setText(String.valueOf(i2) + "/" + String.valueOf(i1 + 1) + "/" + String.valueOf(i));
                dia = String.valueOf(i2) + "/" + String.valueOf(i1 + 1) + "/" + String.valueOf(i);
            }
        });

        binding.spnProfesional.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //Bucle que comprueba cual es la conferencia de la lista seleccionada en el spinner
                //para asignarla a la variable global.
                for (Profesional prof : listaProfesionales
                ) {
                    if (prof.getNombre_completo().equals(binding.spnProfesional.getSelectedItem().toString())) {
                        profesionalElegido = prof;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    guardarCita(dia + " " + hora);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        obtenerProfesionales();
        /*final TextView textView = binding.tvChooseDate2;
        citasViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        return root;
    }

    private void guardarCita(String fecha) throws ParseException {

        System.out.println(fecha);
        System.out.println(binding.cvApointmentDate.getDate() + (hour - 20000) + minute);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Timestamp fechaFormateada = new Timestamp(sdf.parse(fecha));


        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        dialogo.setTitle(R.string.aviso);
        dialogo.setMessage("¿Deseas guarda la siguiente cita? \n" + dia + " " + hora);
        dialogo.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialogo.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) { // Borramos
                if (fechaFormateada != null && profesionalElegido != null) {

                    //usuario y mensaje
                    Citas cita = new Citas(fechaFormateada, auth.getUid(), profesionalElegido.getusuario_uid());

                    //Se obtiene una instancia de Firestore
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection(FirebaseContract.ProfesionalEntry.NODE_NAME).document(profesionalElegido.getusuario_uid())
                            .collection(FirebaseContract.CitasEntry.NODE_NAME).add(cita);
                            //.add(cita);

                    /*db.collection(FirebaseContract.ProfesionalEntry.NODE_NAME)//documento conferencia actual
                    .document(profesionalElegido.getusuario_uid()) //subcolección de la conferencia
                    .collection(FirebaseContract.CitasEntry.NODE_NAME) //añadimos el mensaje nuevo
                    .add(cita);*/
                }
            }
        });
        dialogo.show();

        //Si el contenido del control no está vacío,

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void obtenerProfesionales() {
        //Se crea una instancia de Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Se incializa la lista que contendra las conferencias
        listaProfesionales = new ArrayList<Profesional>();

        //Se llama a Firestore para obtener los documentos de conferencias
        db.collection(FirebaseContract.ProfesionalEntry.NODE_NAME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //Si se han cargado los documentos
                if (task.isSuccessful()) {
                    //Se van añadiendo a la lista de conferencias
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        listaProfesionales.add(document.toObject(Profesional.class));
                    }
                    //LLamada a un método
                    cargaSpinner();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void cargaSpinner() {
        //Se crea una lista que contenga el nombre de las conferencias
        List nombreConf = new ArrayList<>();

        //Se añaden los nombre a la lista
        for (Profesional prof :
                listaProfesionales) {
            nombreConf.add(prof.getNombre_completo());
        }

        //Se crea un adaptador para el spinner y se le asignan los valores de la lista creada anteriormente
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, nombreConf);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnProfesional.setAdapter(adapter);

        if (!listaProfesionales.isEmpty()) {
            profesionalElegido = listaProfesionales.get(binding.spnProfesional.getSelectedItemPosition());
        }

    }
}