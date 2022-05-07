package ara.tfg.happybuddy.ui.gallery;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ara.tfg.happybuddy.databinding.FragmentCitasBinding;

public class CitasFragment extends Fragment {

    private FragmentCitasBinding binding;
    int hour, minute;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CitasViewModel citasViewModel =
                new ViewModelProvider(this).get(CitasViewModel.class);

        binding = FragmentCitasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.ivHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int iHour, int iMinute) {
                        hour = iHour;
                        minute = iMinute;

                        binding.tvChosenHour.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
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
                String selectedDate = sdf.format(new Date(binding.cvApointmentDate.getDate()));

                binding.tvChosenDate.setText(String.valueOf(i2) + "/" + String.valueOf(i1) + "/" + String.valueOf(i));
            }
        });

        /*final TextView textView = binding.tvChooseDate2;
        citasViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}