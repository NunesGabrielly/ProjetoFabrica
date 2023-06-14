package com.example.projetofabrica.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projetofabrica.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class EditarVisita extends AppCompatActivity {

    private EditText editDataVisita, editHoraVisita, editNomeVisita;
    private Button btnSalvarEdicao, btnCancel;
    private Visita visita;
    String userID;
    String[] mensagens = {"Preencha todos os campos", "Salvo com sucesso"};

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_visita);
        getSupportActionBar().hide();

        editNomeVisita = findViewById(R.id.edit_nome);
        editDataVisita = findViewById(R.id.edit_data);
        editHoraVisita = findViewById(R.id.edit_hora);
        btnSalvarEdicao = findViewById(R.id.btnSalvarEdicao);
        btnCancel = findViewById(R.id.btnCancel);

        Bundle bundle = getIntent().getExtras();
        Task<DocumentSnapshot> snapshot = FirebaseFirestore.getInstance().collection("Visitas").document(bundle.getString("agendaId")).get();
        snapshot.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                visita = new Visita(task.getResult().getId(), task.getResult().getString("nome"), task.getResult().getString("data"), task.getResult().getString("hora"));
                if (visita != null) {
                    editNomeVisita.setText(visita.getNome());
                    editDataVisita.setText(visita.getData());
                    editHoraVisita.setText(visita.getHora());
                }
            }
        });
        btnSalvarEdicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = editNomeVisita.getText().toString();
                String data = editDataVisita.getText().toString();
                String hora = editHoraVisita.getText().toString();

                if (nome.isEmpty() || data.isEmpty() || hora.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    salvarEdicao();
                    finish();
                }
                Intent intent = new Intent(EditarVisita.this, FormAgenda.class);
                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("Visitas").document(bundle.getString("agendaId")).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditarVisita.this, "Visita cancelada com sucesso", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditarVisita.this, "Falha ao cancelar visita", Toast.LENGTH_SHORT).show();
                            }
                        });

                Intent intent = new Intent(EditarVisita.this, FormAgenda.class);
                startActivity(intent);
            }
        });
    }

    private void salvarEdicao() {
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        VisitaDTO newVisita = new VisitaDTO(userID, editNomeVisita.getText().toString(), editDataVisita.getText().toString(), editHoraVisita.getText().toString());
        String nome = editNomeVisita.toString();
        String data = editDataVisita.toString();
        String hora = editHoraVisita.toString();

        FirebaseFirestore.getInstance().collection("Visitas").document(visita.getId()).set(newVisita).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EditarVisita.this, "Edição salva com sucesso", Toast.LENGTH_SHORT).show();
                enviarEmailConfirmacao(nome, data, hora);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditarVisita.this, "Falha ao salvar edição", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enviarEmailConfirmacao(String nome, String data, String hora) {

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, "Visita " + nome);
        GregorianCalendar calDate = new GregorianCalendar(2023, 6, 14);
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY,
                calDate.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                calDate.getTimeInMillis());
        startActivity(intent);
    }

    public void fecharTeclado(View view) {
        InputMethodManager fecharTeclado = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        fecharTeclado.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
