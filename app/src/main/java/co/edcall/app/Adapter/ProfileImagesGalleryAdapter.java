package co.edcall.app.Adapter;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import co.edcall.app.Model.Institution;
import co.edcall.app.R;

public class ProfileImagesGalleryAdapter extends FirestoreRecyclerAdapter<Institution, ProfileImagesGalleryAdapter.ImageHolder> {

    private OnItemClickListener listener;

    private StorageReference imagePhotoRef;
    private FirebaseStorage firebaseStorage;
    private Context context;

    public ProfileImagesGalleryAdapter(@NonNull FirestoreRecyclerOptions<Institution> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ImageHolder holder, int position, @NonNull Institution institution) {
        firebaseStorage = FirebaseStorage.getInstance();
        imagePhotoRef = firebaseStorage.getReference("institutions/" + institution.getId()
                + "/gallery" + "/" + String.valueOf(position) + ".png");

//        imagePhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Glide.with(context).load(uri).into(holder.gallery_imageView);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Failure loading gallery images", Toast.LENGTH_LONG).show();
//            }
//        });

        Glide.with(context).load(imagePhotoRef).into(holder.gallery_imageView);
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_gallery, viewGroup, false);
        return new ImageHolder(view);
    }

    class ImageHolder extends RecyclerView.ViewHolder {
        ImageView gallery_imageView;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);

            gallery_imageView = itemView.findViewById(R.id.image_gallery_imageview);

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
