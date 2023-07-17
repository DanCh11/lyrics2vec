package de.daycu.springLyrics2Vec.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Record {
    private String id;
    private String artistName;
    private String song;
    private String lyrics;
}
