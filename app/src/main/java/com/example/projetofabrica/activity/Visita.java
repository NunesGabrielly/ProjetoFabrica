package com.example.projetofabrica.activity;

import android.os.Parcel;
import android.os.Parcelable;

public class Visita implements Parcelable {
    private String id;
    private String nome;
    private String data;
    private String hora;

    public Visita(String id, String nome, String data, String hora) {
        this.id = id;
        this.nome = nome;
        this.data = data;
        this.hora = hora;
    }

    protected Visita(Parcel in) {
        id = in.readString();
        nome = in.readString();
        data = in.readString();
        hora = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nome);
        dest.writeString(data);
        dest.writeString(hora);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Visita> CREATOR = new Creator<Visita>() {
        @Override
        public Visita createFromParcel(Parcel in) {
            return new Visita(in);
        }

        @Override
        public Visita[] newArray(int size) {
            return new Visita[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    @Override
    public String toString() {
        return "Visita{" +
                "id=" + id +
                ", nome=" + nome + '\'' +
                ", data='" + data + '\'' +
                ", hora='" + hora + '\'' +
                '}';
    }
}

