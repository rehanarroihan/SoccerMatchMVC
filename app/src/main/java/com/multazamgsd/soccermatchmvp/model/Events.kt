package com.multazamgsd.soccermatchmvp.model

import com.google.gson.annotations.SerializedName

data class Events(
        @SerializedName("idEvent")
        var idEvent: String,

        @SerializedName("strEvent")
        var strEvent: String,

        @SerializedName("strDate")
        var strDate: String,

        @SerializedName("strTime")
        var strTime: String,

        @SerializedName("strHomeTeam")
        var strHomeTeam: String,

        @SerializedName("strAwayTeam")
        var strAwayTeam: String,

        @SerializedName("idHomeTeam")
        var idHomeTeam: String,

        @SerializedName("idAwayTeam")
        var idAwayTeam: String,

        @SerializedName("intHomeScore")
        var intHomeScore: String,

        @SerializedName("intAwayScore")
        var intAwayScore: String,

        @SerializedName("strHomeGoalDetails")
        var strHomeGoalDetails: String,

        @SerializedName("strAwayGoalDetails")
        var strAwayGoalDetails: String
)