package com.gz.family.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gz.family.model.ChatMessage;
import com.gz.family.model.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by host on 2016/2/8.
 */
public class DBhelper extends OrmLiteSqliteOpenHelper {
    // 数据库名称
    public static final String DATABASE_NAME = "family.db";
    // 数据库version
    private static final int DATABASE_VERSION = 1;

    private static DBhelper instance;

    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void init(Context context) {
        instance = new DBhelper(context);
    }

    public static DBhelper getInstance() {
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
        try {
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, UserStore.UserMap.class);
            TableUtils.createTable(connectionSource, ChatMessage.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource arg1, int arg2,
                          int arg3) {
        try {
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, UserStore.UserMap.class, true);
            TableUtils.dropTable(connectionSource, ChatMessage.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public <T> void add(T t) {
        try {
            Class<T> clazz = (Class<T>) Class.forName(t.getClass().getName());
            getDao(clazz).create(t);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public <T> void remove(T t) {
        try {
            Class<T> clazz = (Class<T>) Class.forName(t.getClass().getName());
            getDao(clazz).delete(t);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public <T> List<T> getAll(Class<T> clazz) {
        try {
            return getDao(clazz).queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T getById (Class<T> clazz, String id) {
        try {
            Dao<T,String> dao = getDao(clazz);
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clearDb(Class clazz) {
        try {
            TableUtils.clearTable(connectionSource, clazz);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
