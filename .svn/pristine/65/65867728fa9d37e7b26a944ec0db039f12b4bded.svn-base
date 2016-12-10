package edu.utboy.biteit.models.dao;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import edu.utboy.biteit.db.DataBaseHelper;

public abstract class BaseDAO<T> {
	SQLiteDatabase database;
	DataBaseHelper dbHelper;
	String[] columns;

	protected abstract void setAllColumns();

	protected abstract T cursorTo(Cursor cursor);

	public abstract long save(T object);

	public abstract List<Long> save(List<T> objects);

	public abstract void update(T object);

	public abstract boolean delete(T object);

	public abstract boolean delete(List<T> objects);

	public abstract List<T> get();

	public abstract T get(long id);

	public BaseDAO(Context ctx) {
		dbHelper = DataBaseHelper.getDbHelper(ctx);
		setAllColumns();
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

}
