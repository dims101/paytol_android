package com.lit.paytol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.RiwayatViewHolder> {
    private ArrayList<Riwayat> listRiwayat;

    public RiwayatAdapter(ArrayList<Riwayat> listRiwayat) {
        this.listRiwayat = listRiwayat;
    }

    @NonNull
    @Override
    public RiwayatAdapter.RiwayatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_riwayat,parent,false);
        return new RiwayatViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatAdapter.RiwayatViewHolder holder, int position) {
        holder.textTarif.setText(listRiwayat.get(position).getTarif());
        holder.textRute.setText(listRiwayat.get(position).getRute());
        holder.textTanggal.setText(listRiwayat.get(position).getTanggal());
        holder.textWaktu.setText(listRiwayat.get(position).getWaktu());

    }

    @Override
    public int getItemCount() {
        return (listRiwayat != null) ? listRiwayat.size():0;
    }

    public class RiwayatViewHolder extends RecyclerView.ViewHolder{
        private TextView textTarif,textRute,textTanggal,textWaktu;


        public RiwayatViewHolder (View view){
            super(view);
            textTanggal = view.findViewById(R.id.textTanggal);
            textTarif = view.findViewById(R.id.textTarif);
            textRute = view.findViewById(R.id.textRute);
            textWaktu = view.findViewById(R.id.textWaktu);

        }
    }
}
