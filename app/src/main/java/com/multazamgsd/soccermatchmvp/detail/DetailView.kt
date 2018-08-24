package com.multazamgsd.soccermatchmvp.detail

import com.multazamgsd.soccermatchmvp.model.Events

interface DetailView {
    fun showMainData(data: List<Events>)
}