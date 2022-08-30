package com.lit.paytol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InformasiAdapter extends RecyclerView.Adapter<InformasiAdapter.InformasiViewHolder> {
    private ArrayList<Informasi> informasiArrayList;

    public InformasiAdapter(ArrayList<Informasi> informasiArrayList) {
        this.informasiArrayList = informasiArrayList;
    }

    @NonNull
    @Override
    public InformasiAdapter.InformasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_informasi,parent,false);
        return new InformasiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InformasiAdapter.InformasiViewHolder holder, int position) {
        holder.textInformasi.setText(informasiArrayList.get(position).getNamaInformasi());
        holder.textKeterangan.setText(informasiArrayList.get(position).getKeterangan());
    }

    @Override
    public int getItemCount() {
        return (informasiArrayList != null) ? informasiArrayList.size() :0;
    }

    public class InformasiViewHolder extends RecyclerView.ViewHolder{
        private TextView textInformasi,textKeterangan;
        public InformasiViewHolder (View view){
            super(view);
            textInformasi = view.findViewById(R.id.textInformasi);
            textKeterangan = view.findViewById(R.id.textKeterangan);
        }
    }
}
