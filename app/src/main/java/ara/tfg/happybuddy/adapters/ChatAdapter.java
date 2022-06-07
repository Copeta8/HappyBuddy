package ara.tfg.happybuddy.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import ara.tfg.happybuddy.model.Chat;
import ara.tfg.happybuddy.model.Mensaje;

public class ChatAdapter extends FirestoreRecyclerAdapter<Chat, ChatAdapter.ChatViewHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ChatAdapter(@NonNull FirestoreRecyclerOptions<Chat> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull Chat model) {

    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

        }


    }

}
