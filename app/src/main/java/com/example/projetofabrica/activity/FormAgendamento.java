package com.example.projetofabrica.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.projetofabrica.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FormAgendamento extends AppCompatActivity {
    private EditText editData, editHora;
    private Button btSalvar;
    private AutoCompleteTextView editNome;
    String userID;

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
                salvarVisita();
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
            }
            })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db_error","Erro ao salvar os dados" + toString());
                    }
                });
    }

    public void fecharTeclado(View view){
        InputMethodManager fecharTeclado = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        fecharTeclado.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

