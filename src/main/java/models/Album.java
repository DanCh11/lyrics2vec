package models;

import lombok.Data;

import java.util.List;

@Data
public class Album {
    private String name;
    private List<Song> songs;
}
