package de.daycu.springLyrics2Vec.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@Entity
@ToString
@Table(name = "RECORD")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private UUID id;
    @Column(name = "ARTIST_NAME")
    private String artistName;
    @Column(name = "SONG_NAME")
    private String songName;
    @Column(name = "LYRICS")
    private String lyrics;

    public Record(String artistName, String songName, String lyrics) {
        this.artistName = artistName;
        this.songName = songName;
        this.lyrics = lyrics;
    }


}
