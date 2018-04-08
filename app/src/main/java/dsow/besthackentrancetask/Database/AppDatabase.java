package dsow.besthackentrancetask.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import dsow.besthackentrancetask.Member;

@Database(entities = {Member.class}, version = 1) //creating our Member table by Entity(Member class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MemberDao memberDao();
}