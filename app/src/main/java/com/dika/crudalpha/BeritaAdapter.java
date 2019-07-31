package com.dika.crudalpha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.text.Html;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dika.crudalpha.database.Berita;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.BeritaViewHolder> {
    private Context context;
    private List<Berita> listBerita;

    public void notifyDataSetChanged(int position) {

    }

    public class BeritaViewHolder extends RecyclerView.ViewHolder{
        public TextView judul;
        public TextView timestamp;

        public BeritaViewHolder(@NonNull View view){
            super(view);
            judul = view.findViewById( R.id.judul );
            timestamp = view.findViewById( R.id.timestamp );
        }
    }

    public BeritaAdapter(Context context, List<Berita> listBerita) {
        this.context = context;
        this.listBerita = listBerita;
    }

    @NonNull
    @Override
    public BeritaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from( parent.getContext())
                .inflate( R.layout.berita_list_item, parent, false );
        return new BeritaViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder(@NonNull BeritaViewHolder holder, int position){
        Berita berita = listBerita.get( position );
        holder.judul.setText( berita.getJudul());
        holder.timestamp.setText( formatDate(berita.getTimestamp()));
    }

    @Override
    public int getItemCount(){
        return listBerita.size();
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
            Date date = fmt.parse( dateStr );

            SimpleDateFormat fmtOut = new SimpleDateFormat( "MM d" );
            return fmtOut.format( date );
        } catch (ParseException e) {

        }

        return "";
    }
}
