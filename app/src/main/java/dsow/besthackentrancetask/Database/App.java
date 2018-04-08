package dsow.besthackentrancetask.Database;

import android.app.Application;
import android.arch.persistence.room.Room;
//It's Singleton's pattern implement;
public class App extends Application {
    public static App mInstance;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //creating our database
        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .allowMainThreadQueries()
                .build();
    }

    public static App getInstance() {
        return mInstance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
