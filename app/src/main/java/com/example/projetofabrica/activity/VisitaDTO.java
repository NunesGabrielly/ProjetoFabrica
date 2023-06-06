package com.example.projetofabrica.activity;

public class VisitaDTO {

    private String nome;
    private String data;
    private String hora;
    private String user_id;

    public VisitaDTO(String user_id, String nome, String data, String hora) {
        this.nome = nome;
        this.data = data;
        this.hora = hora;
        this.user_id = user_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
