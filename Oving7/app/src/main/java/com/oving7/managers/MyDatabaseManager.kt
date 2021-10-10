package com.oving7.managers

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity

class MyDatabaseManager(private val activity: AppCompatActivity) :
    SQLiteOpenHelper(activity, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "FilmDatabase"
        const val DATABASE_VERSION = 1

        const val ID = "_id"

        const val TABLE_DIRECTOR = "DIRECTOR"
        const val DIRECTOR_NAME = "name"

        const val TABLE_FILM = "FILM"
        const val FILM_TITLE = "title"

        const val TABLE_ACTOR = "ACTOR"
        const val ACTOR_NAME = "name"

        const val TABLE_DIRECTOR_FILM = "DIRECTOR_FILM"
        const val TABLE_FILM_ACTOR = "FILM_ACTOR"

        const val DIRECTOR_ID = "director_id"
        const val FILM_ID = "film_id"
        const val ACTOR_ID = "actor_id"

        val JOIN_DIRECTOR_FILM = arrayOf(
            "$TABLE_DIRECTOR.$ID=$TABLE_DIRECTOR_FILM.$DIRECTOR_ID",
            "$TABLE_FILM.$ID=$TABLE_DIRECTOR_FILM.$FILM_ID"
        )

        val JOIN_FILM_ACTOR = arrayOf(
            "$TABLE_FILM.$ID=$TABLE_FILM_ACTOR.$FILM_ID",
            "$TABLE_ACTOR.$ID=$TABLE_FILM_ACTOR.$ACTOR_ID"
        )
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """create table $TABLE_DIRECTOR (
						$ID integer primary key autoincrement, 
						$DIRECTOR_NAME text unique not null
						);"""
        )
        db.execSQL(
            """create table $TABLE_FILM (
						$ID integer primary key autoincrement, 
						$FILM_TITLE text unique not null
						);"""
        )
        db.execSQL(
            """create table $TABLE_ACTOR (
						$ID integer primary key autoincrement, 
						$ACTOR_NAME text unique not null
						);"""
        )
        db.execSQL(
            """create table $TABLE_DIRECTOR_FILM (
						$ID integer primary key autoincrement,
                        $DIRECTOR_ID numeric,
						$FILM_ID numeric,
						FOREIGN KEY($DIRECTOR_ID) REFERENCES $TABLE_DIRECTOR($ID), 
						FOREIGN KEY($FILM_ID) REFERENCES $TABLE_FILM($ID)
						);"""
        )
        db.execSQL(
            """create table $TABLE_FILM_ACTOR (
						$ID integer primary key autoincrement,
						$FILM_ID numeric,
                        $ACTOR_ID numeric,
						FOREIGN KEY($FILM_ID) REFERENCES $TABLE_FILM($ID),
                        FOREIGN KEY($ACTOR_ID) REFERENCES $TABLE_ACTOR($ID)
						);"""
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, arg1: Int, arg2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DIRECTOR")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FILM")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACTOR")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DIRECTOR_FILM")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FILM_ACTOR")
        onCreate(db)
    }

    fun clear() {
        writableDatabase.use { onUpgrade(it, 0, 0) }
    }

    fun insert(director: String,
               film: String,
               actors: Array<String>) {

        writableDatabase.use { database ->
            val directorId = insertValueIfNotExists(database, TABLE_DIRECTOR, DIRECTOR_NAME, director)
            val filmId = insertValueIfNotExists(database, TABLE_FILM, FILM_TITLE, film)
            val actorIDs = mutableListOf<Long>()
            for (actor in actors) {
                actorIDs.add(insertValueIfNotExists(database, TABLE_ACTOR, ACTOR_NAME, actor))
            }
            linkDirectorFilm(database, directorId, filmId)
            linkFilmActors(database, filmId, actorIDs)
        }
    }

    private fun insertValueIfNotExists(database: SQLiteDatabase,
                                       table: String,
                                       field: String,
                                       value: String): Long {
        // Query for the value
        query(database, table, arrayOf(ID, field), "$field='$value'").use { cursor ->
            // insert if value doesn't exist
            return if (cursor.count != 0) {
                cursor.moveToFirst()
                cursor.getLong(0) //id of found value
            } else {
                insertValue(database, table, field, value)
            }
        }
    }

    private fun insertValue(database: SQLiteDatabase,
                            table: String,
                            field: String,
                            value: String): Long {

        val values = ContentValues()
        values.put(field, value.trim())
        return database.insert(table, null, values)
    }

    private fun linkDirectorFilm(database: SQLiteDatabase,
                                  directorId: Long,
                                  filmId: Long) {

        val values = ContentValues()
        values.put(DIRECTOR_ID, directorId)
        values.put(FILM_ID, filmId)
        database.insert(TABLE_DIRECTOR_FILM, null, values)
    }

    private fun linkFilmActors(database: SQLiteDatabase,
                               filmId: Long,
                               actorIDs: MutableList<Long>) {

        val values = ContentValues()
        values.put(FILM_ID, filmId)
        for (actorID in actorIDs)
            values.put(FILM_ID, actorID)
        database.insert(TABLE_DIRECTOR_FILM, null, values)
    }

    fun performQuery(table: String,
                     columns: Array<String>,
                     selection: String? = null): ArrayList<String> {

        assert(columns.isNotEmpty())
        readableDatabase.use { database ->
            query(database, table, columns, selection).use { cursor ->
                return readFromCursor(cursor, columns.size)
            }
        }
    }

    fun performRawQuery(select: Array<String>,
                        from: Array<String>,
                        join: Array<String>,
                        where: String? = null): ArrayList<String> {

        val query = StringBuilder("SELECT ")
        for ((i, field) in select.withIndex()) {
            query.append(field)
            if (i != select.lastIndex) query.append(", ")
        }

        query.append(" FROM ")
        for ((i, table) in from.withIndex()) {
            query.append(table)
            if (i != from.lastIndex) query.append(", ")
        }

        query.append(" WHERE ")
        for ((i, link) in join.withIndex()) {
            query.append(link)
            if (i != join.lastIndex) query.append(" and ")
        }

        if (where != null) query.append(" and $where")

        readableDatabase.use { db ->
            db.rawQuery("$query;", null).use { cursor ->
                return readFromCursor(cursor, select.size)
            }
        }
    }

    private fun readFromCursor(cursor: Cursor, numberOfColumns: Int): ArrayList<String> {
        val result = ArrayList<String>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val item = StringBuilder("")
            for (i in 0 until numberOfColumns) {
                item.append("${cursor.getString(i)} ")
            }
            result.add("$item")
            cursor.moveToNext()
        }
        return result
    }

    private fun query(database: SQLiteDatabase,
                      table: String,
                      columns: Array<String>,
                      selection: String?): Cursor {

        return database.query(table, columns, selection,
            null, null, null, null, null)
    }
}