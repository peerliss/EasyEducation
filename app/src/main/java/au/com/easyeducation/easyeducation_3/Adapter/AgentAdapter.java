package au.com.easyeducation.easyeducation_3.Adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import au.com.easyeducation.easyeducation_3.Activities.MainActivity;
import au.com.easyeducation.easyeducation_3.Model.Agents;
import au.com.easyeducation.easyeducation_3.R;

public class AgentAdapter extends RecyclerView.Adapter<AgentAdapter.AgentViewHolder> {

    private Context context;
    private List<Agents> agentsList;

    public AgentAdapter(Context context, List<Agents> agentsList) {
        this.context = context;
        this.agentsList = agentsList;
    }

    @NonNull
    @Override
    public AgentAdapter.AgentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_recycler, null);
        return new AgentViewHolder(view);
//      return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AgentAdapter.AgentViewHolder agentViewHolder, int i) {
        Agents agents = agentsList.get(i);

        agentViewHolder.agent_name.setText(agents.getAgent_name());
        agentViewHolder.agent_username.setText(agents.getAgent_username());
        agentViewHolder.agent_description.setText(agents.getAgent_description());
        agentViewHolder.agent_visa.setText(agents.getAgent_visa());
        agentViewHolder.agent_hours.setText(agents.getAgent_hours());
        agentViewHolder.agent_distance.setText(String.valueOf(agents.getAgent_distance()));
        agentViewHolder.agent_reviews.setText(String.valueOf(agents.getAgent_reviews()));
        agentViewHolder.agent_rating.setText(String.valueOf(agents.getAgent_rating()));
    }

    @Override
    public int getItemCount() {
        return agentsList.size();
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
        }

        @Override
        public void onClick(View v) {

        }
    }
}
