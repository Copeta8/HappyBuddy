package ara.tfg.happybuddy.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import ara.tfg.happybuddy.R;
import ara.tfg.happybuddy.model.Mensaje;

public class MensajeAdapter extends FirestoreRecyclerAdapter<Mensaje, MensajeAdapter.MensajeViewHolder> {

    String remitente;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MensajeAdapter(@NonNull FirestoreRecyclerOptions<Mensaje> options, String remitente) {
        super(options);
        this.remitente = remitente;
    }

    @Override
    protected void onBindViewHolder(@NonNull MensajeAdapter.MensajeViewHolder holder, int position, @NonNull Mensaje model) {

    }

    @NonNull
    @Override
    public MensajeAdapter.MensajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public class MensajeViewHolder extends RecyclerView.ViewHolder {

        TextView tvHora;
        TextView tvMensaje;
        CardView cvMensaje;



        public MensajeViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMensaje = itemView.findViewById(R.id.tvCuerpoMensaje);
            cvMensaje = itemView.findViewById(R.id.cvMensaje);
            tvHora = itemView.findViewById(R.id.tvHoraMensaje);

        }
    }
}

