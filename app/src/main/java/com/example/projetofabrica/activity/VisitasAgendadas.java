package com.example.projetofabrica.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetofabrica.R;
import com.example.projetofabrica.adapter.VisitaAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VisitasAgendadas extends AppCompatActivity{

    private RecyclerView recyclerViewVisitas;
    private VisitaAdapter visitaAdapter;

    private DatabaseReference visitasRef;
    private ValueEventListener visitasListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitas_agendadas);
        getSupportActionBar().hide();

        recyclerViewVisitas = findViewById(R.id.recyclerViewVisitas);
        recyclerViewVisitas.setLayoutManager(new LinearLayoutManager(this));

        visitasRef = FirebaseDatabase.getInstance().getReference().child("visitas");
        visitaAdapter = new VisitaAdapter(new ArrayList<>());
        recyclerViewVisitas.setAdapter(visitaAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        carregarVisitasAgendadasDoFirebase();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (visitasListener != null) {
            visitasRef.removeEventListener(visitasListener);
        }
    }

    private void carregarVisitasAgendadasDoFirebase() {
        visitasListener = visitasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Visita> visitas = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Visita visita = snapshot.getValue(Visita.class);
                    visitas.add(visita);
                }
                visitaAdapter.setVisitas(visitas);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(VisitasAgendadas.this, "Falha ao carregar visitas agendadas", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
