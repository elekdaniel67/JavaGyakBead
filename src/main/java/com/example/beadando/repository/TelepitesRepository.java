package com.example.beadando.repository;

import com.example.beadando.entity.Telepites;
import com.example.beadando.entity.TelepitesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelepitesRepository extends JpaRepository<Telepites, TelepitesId> {

    @Query("""
            select coalesce(nullif(trim(s.kategoria), ''), 'Kategória nélkül') as kategoria,
                   count(t) as darab
            from Telepites t
            join t.szoftver s
            group by coalesce(nullif(trim(s.kategoria), ''), 'Kategória nélkül')
            order by darab desc, kategoria asc
            """)
    List<KategoriaOsszesites> findTelepitesekKategoriankent();

    interface KategoriaOsszesites {
        String getKategoria();
        long getDarab();
    }
}