package com.multazamgsd.soccermatchmvp.detail

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.multazamgsd.soccermatchmvp.R
import com.multazamgsd.soccermatchmvp.R.id.action_fav
import com.multazamgsd.soccermatchmvp.R.id.action_unfav
import com.multazamgsd.soccermatchmvp.api.ApiRepository
import com.multazamgsd.soccermatchmvp.api.TheSportDBApi
import com.multazamgsd.soccermatchmvp.db.Favorite
import com.multazamgsd.soccermatchmvp.db.database
import com.multazamgsd.soccermatchmvp.model.Events
import com.multazamgsd.soccermatchmvp.model.TeamDetailResponse
import com.multazamgsd.soccermatchmvp.model.Teams
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class DetailActivity : AppCompatActivity(), DetailView {
    private var TAG: String = "DetailActivity"
    private lateinit var snackbar: Snackbar
    private lateinit var event: List<Events>
    private lateinit var presenter: DetailPresenter

    private var homeBadge: String = ""
    private var awayBadge: String = ""
    private var homeGoal: String = ""
    private var awayGoal: String = ""
    private var event_id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        title = "Match Detail"

        val request = ApiRepository()
        val gson = Gson()
        presenter = DetailPresenter(this, request, gson)

        event_id = intent.extras.get("event_id").toString()
        presenter.loadMainData(event_id)
    }

    override fun showMainData(data: List<Events>) {
        event = data
        Log.d(TAG, data.toString())
        tvClub.text = data[0].strEvent
        if (data[0].intHomeScore === null || data[0].intHomeScore === "null") {
            tvScore.text = ""
        } else {
            tvScore.text = "${data[0].intHomeScore} : ${data[0].intAwayScore}"
        }
        tvDate.text = data[0].strDate
        if (data[0].strHomeGoalDetails !== null) {
            data[0].strHomeGoalDetails.split(";").toTypedArray().forEach {
                homeGoal += it + "\n"
            }
        } else {
            homeGoal = ""
        }
        if (data[0].strAwayGoalDetails !== null) {
            data[0].strAwayGoalDetails.split(";").toTypedArray().forEach {
                awayGoal += it + "\n"
            }
        } else {
            awayGoal = ""
        }

        tvHomeGoal.text = homeGoal
        tvAwayGoal.text = awayGoal
        loadTeamBadge()
    }

    fun loadTeamBadge() {
        val teamID = arrayOf(event[0].idHomeTeam, event[0].idAwayTeam)
        val stringTeamBadge = arrayOf(homeBadge, awayBadge)
        val teamBadge = arrayOf(ivHomeTeam, ivAwayTeam)
        for (i in 0 until teamID.size) {
            async(UI) {
                val data = bg {
                    Gson().fromJson(ApiRepository().doRequest(TheSportDBApi.teamDetail(teamID[i])),
                            TeamDetailResponse::class.java
                    )
                }
                val teams: List<Teams> = data.await().teams
                Glide.with(this@DetailActivity).load(teams[0].strTeamBadge).into(teamBadge[i])
                stringTeamBadge[i] = teams[0].strTeamBadge
            }
        }
    }

    private fun isFavorite(): Boolean {
        var count = 0
        database.use {
            select("TABLE_FAVORITE").whereArgs("(EVENT_ID = ${event_id})")
                    .exec {
                        count = this.count
                    }
        }
        return count > 0
    }

    private fun addToFavorite() {
        try {
            database.use {
                insert(Favorite.TABLE_FAVORITE,
                        Favorite.EVENT_ID to event_id,
                        Favorite.EVENT_DTE to event[0].strDate,
                        Favorite.TEAM_HOME_NAME to event[0].strHomeTeam,
                        Favorite.TEAM_AWAY_NAME to event[0].strAwayTeam,
                        Favorite.EVENT_SCORE to "${event[0].intHomeScore} : ${event[0].intAwayScore}")
            }
            snackbar = Snackbar.make(llDetail, "Added to Favorite", Snackbar.LENGTH_LONG)
            snackbar.show()
            invalidateOptionsMenu()
        } catch (e: SQLiteConstraintException) {
            snackbar = Snackbar.make(llDetail, e.localizedMessage, Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    }

    private fun deleteFromFavorite() {
        database.use {
            try {
                delete("TABLE_FAVORITE", "EVENT_ID = ${event_id}")
                snackbar = Snackbar.make(llDetail, "Deleted from Favorite", Snackbar.LENGTH_LONG)
                snackbar.show()
                invalidateOptionsMenu()
            } catch (e: SQLiteConstraintException) {
                snackbar = Snackbar.make(llDetail, e.localizedMessage, Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            action_fav -> {
                addToFavorite()
                true
            }
            action_unfav -> {
                deleteFromFavorite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_activity, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        Log.d(TAG, "onPrepareOptionsMenu")
        if (isFavorite()) {
            menu?.findItem(R.id.action_fav)?.isVisible = false
            menu?.findItem(R.id.action_unfav)?.isVisible = true
            Log.d(TAG, "onPrepareOptionsMenu: isFavorite")
        } else {
            menu?.findItem(R.id.action_fav)?.isVisible = true
            menu?.findItem(R.id.action_unfav)?.isVisible = false
            Log.d(TAG, "onPrepareOptionsMenu: not fav")
        }
        return super.onPrepareOptionsMenu(menu)
    }
}
