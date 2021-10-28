package com.oving5

import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import java.net.*

const val ENCODING = "UTF-8"
//const val ENCODING = "ISO-8859-1"

class HttpWrapper(private val URL: String) {
	private val cookieManager = CookieManager(null, CookiePolicy.ACCEPT_ALL)

	init {
		CookieHandler.setDefault(cookieManager)
	}

	private fun openConnection(url: String): URLConnection {
		val connection = URL(url).openConnection() as HttpURLConnection
		connection.setRequestProperty("Accept-Charset", ENCODING)
		return connection
	}


	/**
	 * Sends HTTP GET request and returns body of response from server as String
	 * */
	fun get(parameterList: Map<String, String>): String {
		val connection = openConnection(URL + encodeParameters(parameterList))
		Log.i("Get Cookie", cookieManager.cookieStore.cookies.toString())
		connection.inputStream.use { response ->
			return readResponseBody(response, getCharSet(connection))
		}
	}

	/**
	 * Remove invalid characters and symbols from url parameters and format them properly
	 */
	private fun encodeParameters(parameterList: Map<String, String>): String {
		var parameterString = "?"
		for ((key, value) in parameterList) {
			try {
				parameterString += URLEncoder.encode(key, ENCODING)
				parameterString += "="
				parameterString += URLEncoder.encode(value, ENCODING)
				parameterString += "&"
			} catch (e: UnsupportedEncodingException) {
				Log.e("encodeParameters()", e.toString())
			}
		}
		return parameterString
	}

	/**
	 *  Read the entire body of the response from the inputStream
	 */
	private fun readResponseBody(inputStream: InputStream, charset: String?): String {
		var body = ""
		try {
			BufferedReader(InputStreamReader(inputStream, charset)).use { bufferedReader ->
				var line = bufferedReader.readLine()
				while (line != null) {
					body += "$line\n"
					line = bufferedReader.readLine()
				}
			}
		} catch (e: Exception) {
			Log.e("readResponseBody()", e.toString())
			body += "******* Problem reading from server *******\n$e"
		}
		Log.i("readResponseBody()", body)
		return body
	}

	/**
	 * Check to see if the connection uses a different charset/encoding that we do
	 */
	private fun getCharSet(connection: URLConnection): String? {
		var charset: String? = ENCODING
		val contentType = connection.contentType
		val contentInfo = contentType.replace(" ", "").split(";").toTypedArray()
		for (param in contentInfo) {
			if (param.startsWith("charset=")) charset = param.split("=").toTypedArray()[1]
		}
		//Log.i("getCharSet()", "contentType = $contentType")
		//Log.i("getCharSet()", "Encoding/charset = $charset")
		return charset
	}
}
