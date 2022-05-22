package ara.tfg.happybuddy.ui.crear_usuario;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CrearUsuarioViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CrearUsuarioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}