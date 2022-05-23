package ara.tfg.happybuddy.ui.ver_citas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VerCitasModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public VerCitasModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}