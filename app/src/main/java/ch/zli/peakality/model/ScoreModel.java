package ch.zli.peakality.model;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScoreModel {
    private int id;
    private Date date;
    private String city;
    private int score;
}
