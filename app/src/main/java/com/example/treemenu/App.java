package com.example.treemenu;

import android.app.Application;

import com.example.treemenu.dao.DaoMaster;
import com.example.treemenu.dao.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * @author yaxi
 * @date 2018/8/30
 */
public class App extends Application {
    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = false;

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "notes-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

}
