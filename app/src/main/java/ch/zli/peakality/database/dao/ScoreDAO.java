package ch.zli.peakality.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import ch.zli.peakality.database.entity.Score;

@Dao
public interface ScoreDAO {

    @Query("SELECT * FROM score")
    List<Score> getAllScores();

}
