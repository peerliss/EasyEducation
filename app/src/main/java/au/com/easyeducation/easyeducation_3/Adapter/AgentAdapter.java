package au.com.easyeducation.easyeducation_3.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import au.com.easyeducation.easyeducation_3.Model.Agent;
import au.com.easyeducation.easyeducation_3.*;

public class AgentAdapter extends RecyclerView.Adapter<AgentAdapter.AgentViewHolder> {

    private Context context;
    private ArrayList<Agent> agentList = new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private FirebaseFirestore db;

    private DatabaseReference mDatabase;

    public AgentAdapter(Context context, ArrayList<Agent> agentList) {
        this.context = context;
        this.agentList = agentList;
    }

    @NonNull
    @Override
    public AgentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_agent, viewGroup, false);
        AgentViewHolder viewHolder = new AgentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AgentViewHolder agentViewHolder, int i) {
        Agent agent = agentList.get(i);

        agentViewHolder.agent_name.setText(agent.getName());
        agentViewHolder.agent_username.setText(agent.getUsername());
        agentViewHolder.agent_description.setText(agent.getDescription());
        agentViewHolder.agent_visa.setText(agent.getVisa());
        agentViewHolder.agent_hours.setText(agent.getHours());
        agentViewHolder.agent_distance.setText(String.valueOf(agent.getDistance()));
        agentViewHolder.agent_reviews.setText(String.valueOf(agent.getReviews()));
        agentViewHolder.agent_rating.setText(String.valueOf(agent.getRating()));
    }

    @Override
    public int getItemCount() {
        return agentList.size();
    }

    public class AgentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView agent_name;
        TextView agent_username;
        TextView agent_description;
        TextView agent_visa;
        TextView agent_hours;
        TextView agent_distance;
        TextView agent_reviews;
        TextView agent_rating;
        CardView cardView;

        public AgentViewHolder(@NonNull View itemView) {
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
        }

        @Override
        public void onClick(View v) {

        }
    }
}
