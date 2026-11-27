package com.example.beadando.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "telepites")
@IdClass(TelepitesId.class)
public class Telepites {

    @Id
    @ManyToOne
    @JoinColumn(name = "gepid")
    private Gep gep;

    @Id
    @ManyToOne
    @JoinColumn(name = "szoftverid")
    private Szoftver szoftver;

    private String verzio;
    private String datum;

    public Gep getGep() { return gep; }
    public void setGep(Gep gep) { this.gep = gep; }

    public Szoftver getSzoftver() { return szoftver; }
    public void setSzoftver(Szoftver szoftver) { this.szoftver = szoftver; }

    public String getVerzio() { return verzio; }
    public void setVerzio(String verzio) { this.verzio = verzio; }

    public String getDatum() { return datum; }
    public void setDatum(String datum) { this.datum = datum; }
}