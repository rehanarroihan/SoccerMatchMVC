package com.multazamgsd.soccermatchmvp

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HaTest {
    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun onCreate() {
        onView(ViewMatchers.withId(R.id.recyclerViewPrev)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.recyclerViewPrev)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(11))
        onView(ViewMatchers.withId(R.id.recyclerViewPrev)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(11, ViewActions.click()))
    }
}