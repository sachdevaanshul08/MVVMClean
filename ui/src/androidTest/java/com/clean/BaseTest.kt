package com.clean

import android.content.Context
import com.clean.utils.APICallsSuccessDispatcher
import com.clean.utils.ReaderUtil
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before

abstract class BaseTest {

    lateinit var mockServer: MockWebServer

    @Before
    open fun setUp() {
        this.configureMockServer()
    }

    @After
    open fun tearDown() {
        this.stopMockServer()
    }

    // MOCK SERVER
    abstract fun isMockServerEnabled(): Boolean


    open fun configureMockServer() {
        if (isMockServerEnabled()) {
            mockServer = MockWebServer()
            mockServer.start(8080)
        }
    }


    open fun stopMockServer() {
        if (isMockServerEnabled()) {
            mockServer.shutdown()
        }
    }

    open fun mockSuccessHttpResponse(ctx: Context, fileName: String, responseCode: Int) {
        val response = ReaderUtil.asset(ctx, fileName)
        mockServer.enqueue(
            MockResponse()
                .setResponseCode(responseCode)
                .setBody(response)
        )
    }


}