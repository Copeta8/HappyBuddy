package ara.tfg.happybuddy.ui.home_profesional;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ara.tfg.happybuddy.model.Citas;
import ara.tfg.happybuddy.model.FirebaseContract;

public class HomeProfesionalViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mText;

    private MutableLiveData<ArrayList<Citas>> listaCitasLive;

    FirebaseAuth mAuth;

    public HomeProfesionalViewModel(@NonNull Application application) {
        super(application);

        listaCitasLive = new MutableLiveData<>(new ArrayList<Citas>());

        mAuth = FirebaseAuth.getInstance();

        listaCitasLive.setValue(setListCitas());

        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }


    public LiveData<ArrayList<Citas>> getListCitas() {
        return listaCitasLive;
    }


    public ArrayList<Citas> setListCitas() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        ArrayList<Citas> listaCitas = new ArrayList<>();

        db.collection(FirebaseContract.ProfesionalEntry.NODE_NAME).document(mAuth.getCurrentUser().getUid()).collection(FirebaseContract.CitasEntry.NODE_NAME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        listaCitas.add(document.toObject(Citas.class));
                    }
                }
            }
        });
        return listaCitas;
    }
}