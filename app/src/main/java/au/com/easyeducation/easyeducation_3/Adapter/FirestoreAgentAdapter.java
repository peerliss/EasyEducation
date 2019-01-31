package au.com.easyeducation.easyeducation_3.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;

import au.com.easyeducation.easyeducation_3.Model.Agent;
import au.com.easyeducation.easyeducation_3.R;

public class FirestoreAgentAdapter extends FirestoreRecyclerAdapter<Agent, FirestoreAgentAdapter.AgentHolder> {

    private OnItemClickListener listener;

    public FirestoreAgentAdapter(@NonNull FirestoreRecyclerOptions<Agent> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AgentHolder holder, int position, @NonNull Agent agent) {
        holder.agent_name.setText(agent.getName());
        holder.agent_username.setText(agent.getUsername());
        holder.agent_description.setText(agent.getDescription());
        holder.agent_visa.setText(agent.getVisa());
        holder.agent_hours.setText(agent.getHours());
        holder.agent_distance.setText(String.valueOf(agent.getDistance()));
        holder.agent_reviews.setText(String.valueOf(agent.getReviews()));
        holder.agent_rating.setText(String.valueOf(agent.getRating()));
    }

    @NonNull
    @Override
    public AgentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_agent, viewGroup, false);
        return new AgentHolder(view);
    }

    class AgentHolder extends RecyclerView.ViewHolder {
        TextView agent_name;
        TextView agent_username;
        TextView agent_description;
        TextView agent_visa;
        TextView agent_hours;
        TextView agent_distance;
        TextView agent_reviews;
        TextView agent_rating;
        CardView cardView;

        public AgentHolder(@NonNull View itemView) {
            super(itemView);

            agent_name = itemView.findViewById(R.id.agent_name);
            agent_username = itemView.findViewById(R.id.agent_username);
            agent_description = itemView.findViewById(R.id.agent_description);
            agent_visa = itemView.findViewById(R.id.agent_visa);
            agent_hours = itemView.findViewById(R.id.agent_hours);
            agent_distance = itemView.findViewById(R.id.agent_distance);
            agent_reviews = itemView.findViewById(R.id.agent_reviews);
            agent_rating = itemView.findViewById(R.id.agent_rating);
            cardView = itemView.findViewById(R.id.fragment_agent);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position).getReference());
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentReference documentReference);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
