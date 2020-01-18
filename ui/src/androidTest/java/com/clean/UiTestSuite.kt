package com.clean

import com.clean.homefragmenttest.HomeFragmentListTest
import com.clean.mainactivitytest.MainActivityTest
import com.clean.mapfragmenttest.MapFragmentViewTest
import com.clean.roomtest.RoomDatabaseTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    RoomDatabaseTest::class, MainActivityTest::class,
    HomeFragmentListTest::class, MapFragmentViewTest::class
)
class UiTestSuite