package ara.tfg.happybuddy.ui.ver_citas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ara.tfg.happybuddy.databinding.FragmentVerCitasUserBinding;

public class VerCitasFragment extends Fragment {

    private FragmentVerCitasUserBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        VerCitasModel verCitasModel =
                new ViewModelProvider(this).get(VerCitasModel.class);

        binding = FragmentVerCitasUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        verCitasModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}