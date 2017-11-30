package spider.app.sportsfete;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import spider.app.sportsfete.API.Event;
import spider.app.sportsfete.API.Standing;
import spider.app.sportsfete.FireBaseServices.Comment;

/**
 * Created by dhananjay on 26/1/17.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "SportsFeteDB";
    private static final int DATABASE_VERSION = 1;
    private Dao<Event,Long> eventsDao;
    private Dao<Standing,Long> standingsDao;
    private ConnectionSource connectionSource;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Event.class);
            TableUtils.createTable(connectionSource, Standing.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Event.class,false);
            TableUtils.dropTable(connectionSource, Standing.class,false);
            onCreate(database,connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Event, Long> getEventsDao() throws SQLException {
        if(eventsDao == null) {
            eventsDao = getDao(Event.class);
        }
        return eventsDao;
    }

    public Dao<Standing, Long> getStandingsDao() throws SQLException {
        if(standingsDao == null) {
            standingsDao = getDao(Standing.class);
        }
        return standingsDao;
    }

    public ConnectionSource getConnectionSource(){
        if (connectionSource == null) {
            connectionSource = super.getConnectionSource();
        }
        return connectionSource;
    }
}
