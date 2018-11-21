package tn.esprit.baladity.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import tn.esprit.baladity.R;
import tn.esprit.baladity.entities.Evenement;


public class EvenementAdapter extends RecyclerView.Adapter<EvenementAdapter.ViewHolder> {
    Context context;
    List<Evenement> lstevenements;
    ImageLoader imageLoader;

    public EvenementAdapter( Context context, List<Evenement> getDataAdapter) {
        super();
        this.lstevenements = getDataAdapter;

        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_evenement, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Evenement getDataAdapter1 =  lstevenements.get(position);
        imageLoader= ImageAdapter.getInstance(context).getImageLoader();

        imageLoader.get(getDataAdapter1.getImage(),
                ImageLoader.getImageListener(
                        holder.VollyImageView,//Server Image
                        R.mipmap.ic_launcher,//Before loading server image the default showing image.
                        android.R.drawable.ic_dialog_alert //Error image if requested image dose not found on server.
                )
        );
        holder.eventTitre.setText(getDataAdapter1.getTitre());
        holder.eventDescription.setText(getDataAdapter1.getDescription());
        holder.VollyImageView.setImageUrl(getDataAdapter1.getImage(), imageLoader);
    }

    @Override
    public int getItemCount() {

        return lstevenements.size();
    }





    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView eventTitre;
        public TextView eventDescription;
        public NetworkImageView VollyImageView ;

        public ViewHolder(View itemView) {

            super(itemView);

            eventTitre = (TextView) itemView.findViewById(R.id.txt_titre) ;
            eventDescription = (TextView) itemView.findViewById(R.id.txt_description) ;
            VollyImageView =(NetworkImageView)itemView.findViewById(R.id.img_evt);
        }
    }

}
