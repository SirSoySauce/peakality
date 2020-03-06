package ch.zli.peakality.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ch.zli.peakality.database.entity.Score;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface ScoreDAO {

    @Query("SELECT * FROM score")
    Single<List<Score>> getAllScores();

    @Insert
    Completable insertAll(Score... scores);


}
