package co.edcall.app.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;

import co.edcall.app.Model.Course;
import co.edcall.app.R;

public class FirestoreCourseAdapter extends FirestoreRecyclerAdapter<Course, FirestoreCourseAdapter.CourseHolder> {

    private OnItemClickListener listener;

    public FirestoreCourseAdapter(@NonNull FirestoreRecyclerOptions<Course> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CourseHolder holder, int position, @NonNull Course course) {
        holder.courseRecyclerName.setText(course.getName() + " - " + course.getCourseCode());
        holder.courseRecyclerDuration.setText(course.getDuration());

    }

    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_profile_courses, viewGroup, false);
        return new CourseHolder(view);
    }

    class CourseHolder extends RecyclerView.ViewHolder {
        TextView courseRecyclerName;
        TextView courseRecyclerDuration;

        public CourseHolder(@NonNull View itemView) {
            super(itemView);

            courseRecyclerName = itemView.findViewById(R.id.courseRecyclerName);
            courseRecyclerDuration = itemView.findViewById(R.id.courseRecyclerDuration);

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
