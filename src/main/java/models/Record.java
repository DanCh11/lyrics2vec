package models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Record {
    private String artistName;
    private String song;
    private String lyrics;
}
