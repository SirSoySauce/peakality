package ch.zli.peakality.domain.bo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScoreBO implements Serializable {

    public double longitude;

    public double latitude;

    public double altitude;

    public double temperature;

    public String weather;

    public double windSpeed;

    public float airPressure;

}
