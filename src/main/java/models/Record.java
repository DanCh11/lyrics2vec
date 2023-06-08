package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

import java.util.List;

@Data
@AllArgsConstructor
public class Record {
    @Field("id")
    private String id;
    @Field("artistName")
    private String artistName;
    @Field("songName")
    private String song;
    @Field("lyrics")
    private String lyrics;
}
