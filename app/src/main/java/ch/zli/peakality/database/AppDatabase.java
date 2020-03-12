package ch.zli.peakality.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ch.zli.peakality.database.dao.ScoreDAO;
import ch.zli.peakality.database.entity.Score;

@Database(entities = {Score.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ScoreDAO userDao();
}
