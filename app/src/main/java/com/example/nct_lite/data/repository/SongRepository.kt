package com.example.nct_lite.data.repository
import androidx.lifecycle.MutableLiveData
import com.example.nct_lite.data.model.Song
import com.example.nct_lite.data.network.ApiService
import com.example.nct_lite.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SongRepository(
    private val apiService: ApiService = RetrofitClient.instance
) {

    fun fetchAllSongs(liveData: MutableLiveData<List<Song>>) {
        apiService.getAllSongs().enqueue(object : Callback<List<Song>> {
            override fun onResponse(call: Call<List<Song>>, response: Response<List<Song>>) {
                if (response.isSuccessful) liveData.postValue(response.body())
                else liveData.postValue(emptyList())
            }

            override fun onFailure(call: Call<List<Song>>, t: Throwable) {
                liveData.postValue(emptyList())
            }
        })
    }

    fun searchSongs(keyword: String, liveData: MutableLiveData<List<Song>>) {
        apiService.searchSongs(keyword).enqueue(object : Callback<List<Song>> {
            override fun onResponse(call: Call<List<Song>>, response: Response<List<Song>>) {
                if (response.isSuccessful) liveData.postValue(response.body())
            }

            override fun onFailure(call: Call<List<Song>>, t: Throwable) {
                liveData.postValue(emptyList())
            }
        })
    }

    suspend fun fetchSong(id: String): Song? {
        return try {
            apiService.getSong(id)
        } catch (throwable: Throwable) {
            null
        }
    }
}
