package com.multazamgsd.soccermatchmvp.detail

import com.google.gson.Gson
import com.multazamgsd.soccermatchmvp.api.ApiRepository
import com.multazamgsd.soccermatchmvp.api.TheSportDBApi
import com.multazamgsd.soccermatchmvp.model.PreviousResponse
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class DetailPresenter(private val view: DetailView,
                      private val apiRepository: ApiRepository,
                      private val gson: Gson) {

    fun loadMainData(event_id: String) {
        async(UI) {
            val data = bg {
                Gson().fromJson(apiRepository.doRequest(TheSportDBApi.lookupEvent(event_id)),
                        PreviousResponse::class.java
                )
            }
            view.showMainData(data.await().events)
        }
    }
}