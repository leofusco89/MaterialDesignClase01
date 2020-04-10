package com.example.username.myapplication;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class LibroManager {

    private static LibroManager instance;
    Dao<Libro, Integer> dao;

    public static LibroManager getInstance(Context context) {
        if (instance == null) {
            instance = new LibroManager(context);
        }
        return instance;
    }

    private LibroManager(Context context) {
        OrmLiteSqliteOpenHelper helper = OpenHelperManager.getHelper(context, DBHelper.class);
        try {
            dao = helper.getDao(Libro.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Libro> getLibros() throws SQLException {
        return dao.queryForAll();
    }

    public void agregarLibro(Libro libro) throws SQLException {
        dao.create(libro);
    }

}
