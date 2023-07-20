package de.daycu.springLyrics2Vec.repositories;

import de.daycu.springLyrics2Vec.models.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecordRepository extends JpaRepository<Record, UUID> {
    List<String> findByArtistName(String artistName);
    Optional<String> findByAlbumName(String albumName);
}
