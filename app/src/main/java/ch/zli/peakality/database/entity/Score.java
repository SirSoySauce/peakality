package ch.zli.peakality.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Score {

    @PrimaryKey
    @ColumnInfo(name = "score_id")
    public int scoreId;

}
