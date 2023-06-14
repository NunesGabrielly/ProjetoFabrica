package com.example.projetofabrica.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.projetofabrica.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class FormAgendamento extends AppCompatActivity {
    private EditText editData, editHora;
    private Button btSalvar;
    private AutoCompleteTextView editNome;
    String userID;
    String[] mensagens = {"Preencha todos os campos", "Salvo com sucesso"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_agendamento);
        getSupportActionBar().hide();

        editData = findViewById(R.id.edit_data);
        editHora = findViewById(R.id.edit_hora);
        btSalvar = findViewById(R.id.bt_salvar);

        editNome = (AutoCompleteTextView) findViewById(R.id.edit_nome);
        String[] countries = getResources().getStringArray(R.array.instituicoes_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                countries);
        editNome.setAdapter(adapter);

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = editNome.getText().toString();
                String data = editData.getText().toString();
                String hora = editHora.getText().toString();

                if(nome.isEmpty() || data.isEmpty() || hora.isEmpty()){
                    Snackbar snackbar = Snackbar.make(view, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else {
                    Snackbar snackbar = Snackbar.make(view, mensagens[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    salvarVisita();
                    finish();
                }
                Intent intent =  new Intent(FormAgendamento.this, FormAgenda.class);
                startActivity(intent);
            }
        });
    }

    private void salvarVisita() {
        String nome = editNome.getText().toString();
        String data = editData.getText().toString();
        String hora = editHora.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String,Object> visitas = new HashMap<>();
        visitas.put("nome",nome);
        visitas.put("data",data);
        visitas.put("hora",hora);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        visitas.put("user_id", userID);
        DocumentReference documentReference = db.collection("Visitas").document();
        documentReference.set(visitas).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db","Sucesso ao salvar os dados");
                enviarEmailConfirmacao(nome, data, hora);
            }
            }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db_error","Erro ao salvar os dados" + toString());
                    }
            });
    }
    private void enviarEmailConfirmacao(String nome, String data, String hora) {

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, "Visita " + nome);
        GregorianCalendar calDate = new GregorianCalendar(2023, 6, 14);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                calDate.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                calDate.getTimeInMillis());
        startActivity(intent);
    }

    public void fecharTeclado(View view){
        InputMethodManager fecharTeclado = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        fecharTeclado.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

