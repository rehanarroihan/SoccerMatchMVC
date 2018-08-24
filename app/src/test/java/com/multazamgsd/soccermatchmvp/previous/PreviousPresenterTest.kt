package com.multazamgsd.soccermatchmvp.previous

import com.google.gson.Gson
import com.multazamgsd.soccermatchmvp.TestContextProvider
import com.multazamgsd.soccermatchmvp.api.ApiRepository
import com.multazamgsd.soccermatchmvp.api.TheSportDBApi
import com.multazamgsd.soccermatchmvp.model.Events
import com.multazamgsd.soccermatchmvp.model.PreviousResponse
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class PreviousPresenterTest {

    @Test
    fun getMatchList() {
        val teams: MutableList<Events> = mutableListOf()
        val response = PreviousResponse(teams)
        val league = "English Premiere League"

        `when`(gson.fromJson(apiRepository
                .doRequest(TheSportDBApi.eventsPastLeague(league)),
                PreviousResponse::class.java
        )).thenReturn(response)

        presenter.getMatchList(league)

        verify(view).showMatchList(teams)
    }

    @Mock
    private lateinit var view: PreviousView

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var apiRepository: ApiRepository

    @Mock
    private lateinit var presenter: PreviousPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = PreviousPresenter(view, apiRepository, gson, TestContextProvider())
    }
}