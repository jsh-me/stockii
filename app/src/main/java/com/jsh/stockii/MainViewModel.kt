package com.jsh.stockii

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.stream.Collectors

class MainViewModel: ViewModel() {
    private val TAG: String? = MainViewModel::class.java.simpleName
    var itemLiveData = MutableLiveData<List<Store>>()

    private var retrofitService = Retrofit.Builder()
    .baseUrl(Consts.baseUrl)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()
    .create(MaskService::class.java)
    .fetchStoreInfo()



    fun fetchStoreInfo(){
        retrofitService.clone().enqueue(object: Callback<StoreInfo>{
            override fun onFailure(call: Call<StoreInfo>, t: Throwable) {
                Log.e(TAG, t.localizedMessage!!)
                itemLiveData.value = Collections.emptyList()
            }

            override fun onResponse(call: Call<StoreInfo>, response: Response<StoreInfo>) {
                val items = response.body()?.stores
                Log.i(TAG, "refresh")

                itemLiveData.value = items?.let {
                    it.stream()
                        .filter { it.remain_stat != null }
                        .collect(Collectors.toList())
                }
            }
        })
    }
}