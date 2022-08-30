package com.lit.paytol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RuteAdapter extends RecyclerView.Adapter<RuteAdapter.RuteViewHolder> {

    private ArrayList<Rute> listRute;

    public RuteAdapter(ArrayList<Rute> listRute) {
        this.listRute = listRute;
    }

    @NonNull
    @Override
    public RuteAdapter.RuteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_rute,parent,false);
        return new  RuteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RuteAdapter.RuteViewHolder holder, int position) {
        holder.textGateMasuk.setText(listRute.get(position).getGateMasuk());
        holder.textGateKeluar.setText(listRute.get(position).getGateKeluar());
        holder.textTarifI.setText(listRute.get(position).getTarifI());
        holder.textTarifII.setText(listRute.get(position).getTarifII());
        holder.textTarifIII.setText(listRute.get(position).getTarifIII());
        holder.textTarifIV.setText(listRute.get(position).getTarifIV());
        holder.textTarifV.setText(listRute.get(position).getTarifV());

    }

    @Override
    public int getItemCount() {
        return (listRute != null) ? listRute.size() : 0;
    }

    public class RuteViewHolder extends RecyclerView.ViewHolder{

        private TextView textGateMasuk,textGateKeluar,textTarifI,textTarifII,textTarifIII,textTarifIV,textTarifV;
        public RuteViewHolder (View view){
            super(view);

            textGateMasuk = view.findViewById(R.id.textGateMasuk);
            textGateKeluar = view.findViewById(R.id.textGateKeluar);
            textTarifI = view.findViewById(R.id.textTarifI);
            textTarifII = view.findViewById(R.id.textTarifII);
            textTarifIII = view.findViewById(R.id.textTarifIII);
            textTarifIV = view.findViewById(R.id.textTarifIV);
            textTarifV = view.findViewById(R.id.textTarifV);
        }
    }
}
