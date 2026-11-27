package com.example.beadando.entity;

import java.io.Serializable;
import java.util.Objects;

public class TelepitesId implements Serializable {

    private Integer gep;
    private Integer szoftver;

    public TelepitesId() {}

    public TelepitesId(Integer gep, Integer szoftver) {
        this.gep = gep;
        this.szoftver = szoftver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TelepitesId that)) return false;
        return Objects.equals(gep, that.gep) && Objects.equals(szoftver, that.szoftver);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gep, szoftver);
    }
}