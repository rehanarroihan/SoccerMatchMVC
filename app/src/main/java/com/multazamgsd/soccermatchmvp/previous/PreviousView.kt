package com.multazamgsd.soccermatchmvp.previous

import com.multazamgsd.soccermatchmvp.model.Events

interface PreviousView {
    fun showMatchList(data: List<Events>)
}