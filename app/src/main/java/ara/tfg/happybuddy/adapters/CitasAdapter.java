package ara.tfg.happybuddy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ara.tfg.happybuddy.R;
import ara.tfg.happybuddy.model.Citas;
import ara.tfg.happybuddy.model.FirebaseContract;
import ara.tfg.happybuddy.model.Usuario;

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
    protected void onBindViewHolder(@NonNull CitasAdapter.CitasHolder holder, int position, @NonNull Citas model) {
        //holder.tvNombre.setText(model.getPaciente_UID());
        //System.out.println("Esta es la fecha: " + model.getFecha());

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

        holder.tvFechaCita.setText(model.fechaFormatoLocal());

    }

    @NonNull
    @Override
    public CitasAdapter.CitasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.citas_item, parent, false);
        return new CitasHolder(itemView);
    }


    public class CitasHolder extends RecyclerView.ViewHolder {

        TextView tvNombre;
        TextView tvFechaCita;

        public CitasHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre = itemView.findViewById(R.id.tvNombreProfesionalUsuario);
            tvFechaCita = itemView.findViewById(R.id.tvFechaCita);

        }
    }
}

