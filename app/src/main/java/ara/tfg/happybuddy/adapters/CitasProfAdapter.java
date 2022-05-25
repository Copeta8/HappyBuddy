package ara.tfg.happybuddy.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Date;

import ara.tfg.happybuddy.R;
import ara.tfg.happybuddy.model.Citas;
import ara.tfg.happybuddy.model.FirebaseContract;
import ara.tfg.happybuddy.model.Usuario;

public class CitasProfAdapter extends FirestoreRecyclerAdapter<Citas, CitasProfAdapter.CitasHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CitasProfAdapter(@NonNull FirestoreRecyclerOptions<Citas> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CitasProfAdapter.CitasHolder holder, int position, @NonNull Citas model) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(FirebaseContract.UsuariosEntry.NODE_NAME).document(model.getPaciente_UID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                        Usuario usuario = document.toObject(Usuario.class);
                        holder.tvNombre.setText(usuario.getNombre() + " " + usuario.getApellidos());

                }else{
                    holder.tvNombre.setText("ERROR XD");
                }
            }
        });

        System.out.println("Esta es la fecha de la cita: " + model.getFecha().getNanoseconds());
        System.out.println("Esta es la fecha actual: " + new Timestamp(new Date()).getNanoseconds());

        if (model.getFecha().getSeconds() < new Timestamp(new Date()).getSeconds()) {
            holder.cvFechas.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFDC7C7")));
            holder.ivCitaDone.setVisibility(View.VISIBLE);
        }else{
            holder.cvFechas.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0F2F1")));
        }
        holder.tvFechaCita.setText(model.fechaFormatoLocal());
    }

    @NonNull
    @Override
    public CitasProfAdapter.CitasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.citas_item, parent, false);
        return new CitasHolder(itemView);
    }

    public class CitasHolder extends RecyclerView.ViewHolder {

        TextView tvNombre;
        TextView tvFechaCita;
        CardView cvFechas;
        ConstraintLayout clCita;
        ImageView ivCitaDone;

        public CitasHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre = itemView.findViewById(R.id.tvNombreProfesionalUsuario);
            tvFechaCita = itemView.findViewById(R.id.tvFechaCita);
            cvFechas = itemView.findViewById(R.id.cvCita);
            clCita = itemView.findViewById(R.id.constrainLayout);
            ivCitaDone = itemView.findViewById(R.id.ivCitaDone);
        }
    }
}

