package com.multazamgsd.soccermatchmvp.previous

import com.google.gson.Gson
import com.multazamgsd.soccermatchmvp.CoroutineContextProvider
import com.multazamgsd.soccermatchmvp.api.ApiRepository
import com.multazamgsd.soccermatchmvp.api.TheSportDBApi
import com.multazamgsd.soccermatchmvp.model.PreviousResponse
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class PreviousPresenter(private val view: PreviousView,
                        private val apiRepository: ApiRepository,
                        private val gson: Gson,
                        private val context: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getMatchList(league: String?) {


        async(context.main) {
            val data = bg {
                gson.fromJson(apiRepository.doRequest(TheSportDBApi.eventsPastLeague(league)),
                        PreviousResponse::class.java)
            }
            view.showMatchList(data.await().events)
        }
    }
}