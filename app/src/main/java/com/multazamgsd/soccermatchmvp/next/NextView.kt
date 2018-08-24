package com.multazamgsd.soccermatchmvp.next

import com.multazamgsd.soccermatchmvp.model.Events

interface NextView {
    fun showMatchList(data: List<Events>)
}