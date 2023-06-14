package com.example.projetofabrica.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class DateTextWatcher implements TextWatcher {
    private EditText editText;

    public DateTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String input = s.toString();
        String formattedDate = "";

        // Remove qualquer caractere não numérico
        input = input.replaceAll("[^\\d]", "");

        // Formata a string da data (exemplo: 31122021 -> 31/12/2021)
        if (input.length() >= 2) {
            formattedDate += input.substring(0, 2) + "/";
        }
        if (input.length() >= 4) {
            formattedDate += input.substring(2, 4) + "/";
        }
        if (input.length() >= 8) {
            formattedDate += input.substring(4, 8);
        }

        // Define o texto formatado no EditText
        editText.removeTextChangedListener(this);
        editText.setText(formattedDate);
        editText.setSelection(formattedDate.length());
        editText.addTextChangedListener(this);
    }
}

