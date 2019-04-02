package au.com.easyeducation.easyeducation_3.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import au.com.easyeducation.easyeducation_3.Model.Institution;
import au.com.easyeducation.easyeducation_3.R;

public class FirestoreInstitutionAdapter extends FirestoreRecyclerAdapter<Institution, FirestoreInstitutionAdapter.InstitutionHolder> {

    private OnItemClickListener listener;

    private StorageReference profileImagePhotoRef;
    private FirebaseStorage firebaseStorage;
    private Context context;

    public FirestoreInstitutionAdapter(@NonNull FirestoreRecyclerOptions<Institution> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final InstitutionHolder holder, int position, @NonNull Institution institution) {
        holder.institution_name.setText(institution.getName());
        holder.institution_username.setText(institution.getUsername());
        holder.institution_description.setText(institution.getDescription());
        holder.institution_visa.setText(institution.getVisa());
        holder.institution_hours.setText(institution.getHours());
        holder.institution_distance.setText(String.valueOf(institution.getDistance()));
        holder.institution_reviews.setText(String.valueOf(institution.getReviews()));
        holder.institution_rating.setText(String.valueOf(institution.getRating()));

        firebaseStorage = FirebaseStorage.getInstance();
        profileImagePhotoRef = firebaseStorage.getReference("institutions/" + institution.getId() + "/profile_image.png");

        profileImagePhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.institution_imageView);
            }
        });
    }

    @NonNull
    @Override
    public InstitutionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_institution, viewGroup, false);
        return new InstitutionHolder(view);
    }

    class InstitutionHolder extends RecyclerView.ViewHolder {
        TextView institution_name;
        TextView institution_username;
        TextView institution_description;
        TextView institution_visa;
        TextView institution_hours;
        TextView institution_distance;
        TextView institution_reviews;
        TextView institution_rating;
        CardView cardView;
        ImageView institution_imageView;

        public InstitutionHolder(@NonNull View itemView) {
            super(itemView);

            institution_name = itemView.findViewById(R.id.institution_name);
            institution_username = itemView.findViewById(R.id.institution_username);
            institution_description = itemView.findViewById(R.id.institution_description);
            institution_visa = itemView.findViewById(R.id.institution_visa);
            institution_hours = itemView.findViewById(R.id.institution_hours);
            institution_distance = itemView.findViewById(R.id.institution_distance);
            institution_reviews = itemView.findViewById(R.id.institution_reviews);
            institution_rating = itemView.findViewById(R.id.institution_rating);
            cardView = itemView.findViewById(R.id.fragment_institution);
            institution_imageView = itemView.findViewById(R.id.institution_profile_imageview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position).getReference());
                    }
                }
            });

            context = itemView.getContext();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentReference documentReference);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}