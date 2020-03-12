package ch.zli.peakality.domain.bo;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScoreBO implements Serializable {

    private double longitude;

    private double latitude;

    private double altitude;

    private double temperature;

    private String weather;

    private int weatherId;

    private double windSpeed;

    private float airPressure;

    private Date date;

}
