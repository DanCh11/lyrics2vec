package models;

import lombok.Data;

import java.util.List;

@Data
public class Record {
    private String ArtistName;
    private List<Album> albums;
}
