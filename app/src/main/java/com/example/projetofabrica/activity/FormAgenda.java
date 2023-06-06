package com.example.projetofabrica.activity;

import static android.os.Build.VERSION_CODES.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetofabrica.adapter.VisitaAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FormAgenda extends AppCompatActivity {
    private Button btAgendar, btSair;
    private RecyclerView recyclerView;
    private VisitaAdapter visitaAdapter;
    private List<Visita> visitasList;
    private TextView nomeUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.projetofabrica.R.layout.activity_form_agenda);
        getSupportActionBar().hide();

        btAgendar = findViewById(com.example.projetofabrica.R.id.bt_agendar);
        btSair = findViewById(com.example.projetofabrica.R.id.bt_sair);
        nomeUser = findViewById(com.example.projetofabrica.R.id.textNomeUser);
        recyclerView = findViewById(com.example.projetofabrica.R.id.recycler);

        visitasList = new ArrayList<>();
        visitaAdapter= new VisitaAdapter(visitasList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(visitaAdapter);

        btAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormAgenda.this, FormAgendamento.class);
                startActivity(intent);
            }
        });
        btSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(FormAgenda.this, FormLogin.class);
                startActivity(intent);
                finish();
            }
        });

        carregarVisitasAgendadas();
    }

    private void carregarVisitasAgendadas() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            Task<QuerySnapshot> qsnapshot = FirebaseFirestore.getInstance().collection("Visitas").whereEqualTo("user_id", userId).get();
            qsnapshot.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for(DocumentSnapshot doc : qsnapshot.getResult().getDocuments()) {
                       Visita visita = new Visita(doc.getId(), doc.getString("nome"), doc.getString("data"), doc.getString("hora"));
                       visitasList.add(visita);
                    }
                    visitaAdapter.notifyDataSetChanged();
                }
            });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null){
                    nomeUser.setText(documentSnapshot.getString("nome"));
                }
            }
        });
    }
    public void fecharTeclado(View view){
        InputMethodManager fecharTeclado = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        fecharTeclado.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
