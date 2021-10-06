package com.example.myplayerapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myplayerapp.dto.Album
import com.example.myplayerapp.dto.Track
import com.example.myplayerapp.model.FeedModel
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.concurrent.thread

@HiltViewModel
class MainViewModel @Inject constructor()
    : ViewModel() {

    private val client = OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build()

    companion object {
        const val BASE_URL =
            "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"
    }

    val data: MutableLiveData<FeedModel> by lazy {
        MutableLiveData<FeedModel>()
    }

    init {
        loadTracks()
    }

    private fun loadTracks() {
        thread {
            val request: Request = Request.Builder()
                .url("${BASE_URL}album.json")
                .build()

            client.newCall(request)
                .execute()
                .use { response ->
                    if (!response.isSuccessful) {
                        Log.e("error", "shit...")
                        parseJsonData("")
                    }
                    else
                        parseJsonData(response.body!!.string())
                }
        }
    }

    private fun parseJsonData(rawData: String){
        if (rawData.isEmpty())
        {
            val emptyAlbum = Album(-1, "", "", "", "", "", emptyList())
            data.postValue(FeedModel(emptyAlbum, true))
            return
        }

        val jsonObject = JSONTokener(rawData).nextValue() as JSONObject
        val id = jsonObject.getLong("id")
        val title = jsonObject.getString("title")
        val subtitle = jsonObject.getString("subtitle")
        val artist = jsonObject.getString("artist")
        val published = jsonObject.getString("published")
        val genre = jsonObject.getString("genre")
        val tracksJson = jsonObject.getString("tracks")
        val tracksListJson = JSONTokener(tracksJson).nextValue() as JSONArray
        val tracksList = mutableListOf<Track>()
        for (i in 0 until tracksListJson.length()){
            val id = tracksListJson.getJSONObject(i).get("id").toString()
            val file = tracksListJson.getJSONObject(i).get("file").toString()
            tracksList.add(Track(id, file))
        }
        val album = Album(id, title, subtitle, artist, published, genre, tracksList)

        data.postValue(FeedModel(album, false))
    }
}