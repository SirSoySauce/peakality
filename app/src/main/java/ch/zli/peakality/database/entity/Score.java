package ch.zli.peakality.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Score {

    @PrimaryKey(autoGenerate = true)
    private int scoreId;

    private double longitude;

    private double latitude;

    private double altitude;

    private String cityName;

    private double temperature;

    private String weather;

    private int weatherId;

    private double windSpeed;

    private float airPressure;

    private Date date;

}
