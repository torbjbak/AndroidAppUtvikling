package com.oving7.service

import android.content.Context
import android.util.Log
import com.oving7.managers.MyDatabaseManager
import org.json.JSONArray

class Database(context: Context, input: JSONArray) : MyDatabaseManager(context) {

	init {
		try {
			this.clear()

			for(i in 0 until input.length()) {
				val obj = input.getJSONObject(i)

				val actorsJSON = obj.getJSONArray("actors")
				val actors = arrayListOf<String>()

				for(j in 0 until actorsJSON.length())
					actors.add(actorsJSON.getString(j))

				this.insert(
					obj.getString("director") ,
					obj.getString("film"),
					actors
				)
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	val allDirectors: ArrayList<String>
		get() = performQuery(TABLE_DIRECTOR, arrayOf(ID, DIRECTOR_NAME))

	val allFilms: ArrayList<String>
		get() = performQuery(TABLE_FILM, arrayOf(ID, FILM_TITLE))

    val allActors: ArrayList<String>
        get() = performQuery(TABLE_ACTOR, arrayOf(ID, ACTOR_NAME))


	val allDirectorsAndFilms: ArrayList<String>
		get() {
			val select = arrayOf("$TABLE_DIRECTOR.$DIRECTOR_NAME", "$TABLE_FILM.$FILM_TITLE")
			val from = arrayOf(TABLE_DIRECTOR, TABLE_FILM, TABLE_DIRECTOR_FILM)
			val join = JOIN_DIRECTOR_FILM

			return performRawQuery(select, from, join)
		}

	fun getFilmsByDirector(director: String): ArrayList<String> {
		val select = arrayOf("$TABLE_FILM.$FILM_TITLE")
		val from = arrayOf(TABLE_DIRECTOR, TABLE_FILM, TABLE_DIRECTOR_FILM)
		val join = JOIN_DIRECTOR_FILM
		val where = "$TABLE_DIRECTOR.$DIRECTOR_NAME='$director'"

		return performRawQuery(select, from, join, where)
	}

	fun getActorsByFilm(film: String): ArrayList<String> {
		val select = arrayOf("$TABLE_ACTOR.$ACTOR_NAME")
		val from = arrayOf(TABLE_FILM, TABLE_ACTOR, TABLE_FILM_ACTOR)
		val join = JOIN_FILM_ACTOR
		val where = "$TABLE_FILM.$FILM_TITLE='$film'"

		return performRawQuery(select, from, join, where)
	}
}
