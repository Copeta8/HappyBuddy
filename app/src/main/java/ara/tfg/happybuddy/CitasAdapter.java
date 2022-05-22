package ara.tfg.happybuddy;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import ara.tfg.happybuddy.model.Citas;

public class CitasAdapter extends FirestoreRecyclerAdapter<Citas, CitasAdapter.CitasHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CitasAdapter(@NonNull FirestoreRecyclerOptions<Citas> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CitasHolder holder, int position, @NonNull Citas cita) {

    }

    @NonNull
    @Override
    public CitasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public class CitasHolder extends RecyclerView.ViewHolder {



        public CitasHolder(@NonNull View itemView) {
            super(itemView);


        }

    }
}

