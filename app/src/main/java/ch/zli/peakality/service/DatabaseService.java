package ch.zli.peakality.service;

import java.util.List;

import ch.zli.peakality.database.AppDatabase;
import ch.zli.peakality.database.entity.Score;
import io.reactivex.Completable;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DatabaseService {

    private final AppDatabase db;

    public Completable writeScoreToDatabase(Score... scores) {
        return db.userDao()
                .insertAll(scores);
    }

    public Single<List<Score>> getAllScoresFromDatabase() {
        return db.userDao()
                .getAllScores();
    }

    public Single<Score> getScoreFromDatabase(int scoreId) {
        return db.userDao()
                .getScoreById(scoreId);
    }
}
