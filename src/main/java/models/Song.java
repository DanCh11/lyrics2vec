package models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Song {
    private String name;
    private String lyrics;

}
