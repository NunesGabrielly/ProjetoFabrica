package com.example.projetofabrica.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetofabrica.R;
import com.example.projetofabrica.activity.EditarVisita;
import com.example.projetofabrica.activity.FormAgenda;
import com.example.projetofabrica.activity.Visita;

import java.util.List;

public class VisitaAdapter extends RecyclerView.Adapter<VisitaAdapter.VisitaViewHolder> {

    private List<Visita> visitas;

    public VisitaAdapter(List<Visita> visitas) {
        this.visitas = visitas;
    }

    @NonNull
    @Override
    public VisitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_visita, parent, false);
        return new VisitaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitaViewHolder holder, int position) {
        Visita visita = visitas.get(position);

        holder.textNomeVisita.setText(visita.getNome());
        holder.textDataVisita.setText(visita.getData());
        holder.textHoraVisita.setText(visita.getHora());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditarVisita.class);
                intent.putExtra("agendaId", visita.getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return visitas.size();
    }

    public void setVisitas(List<Visita> visitas) {
        this.visitas = visitas;
    }



    public static class VisitaViewHolder extends RecyclerView.ViewHolder {
        TextView textNomeVisita;
        TextView textDataVisita;
        TextView textHoraVisita;

        public VisitaViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textNomeVisita = itemView.findViewById(R.id.textNomeVisita);
            this.textDataVisita = itemView.findViewById(R.id.textDataVisita);
            this.textHoraVisita = itemView.findViewById(R.id.textHoraVisita);
        }
    }
}

