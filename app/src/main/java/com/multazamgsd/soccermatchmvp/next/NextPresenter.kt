package com.multazamgsd.soccermatchmvp.next

import com.google.gson.Gson
import com.multazamgsd.soccermatchmvp.api.ApiRepository
import com.multazamgsd.soccermatchmvp.api.TheSportDBApi
import com.multazamgsd.soccermatchmvp.model.NextResponse
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class NextPresenter(private val view: NextView,
                    private val apiRepository: ApiRepository,
                    private val gson: Gson) {

    fun getMatchList() {
        async(UI) {
            val data = bg {
                gson.fromJson(apiRepository.doRequest(TheSportDBApi.eventsNextMatch("4328")),
                        NextResponse::class.java
                )
            }
            view.showMatchList(data.await().events)
        }
    }
}