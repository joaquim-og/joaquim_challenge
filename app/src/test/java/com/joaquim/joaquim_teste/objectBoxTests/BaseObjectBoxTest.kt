package com.joaquim.joaquim_teste.objectBoxTests

import com.joaquim.joaquim_teste.data.model.MyObjectBox
import io.objectbox.BoxStore
import io.objectbox.DebugFlags
import org.junit.After
import org.junit.Before
import java.io.File

abstract class BaseObjectBoxTest {

    companion object {
        val TEST_DIRECTORY = File("objectboxtest/test-db")
    }

    var store: BoxStore? = null

    @Before
    fun starting() {
        BoxStore.deleteAllFiles(TEST_DIRECTORY)
        store = MyObjectBox.builder().directory(TEST_DIRECTORY)
            .debugFlags(DebugFlags.LOG_QUERIES or DebugFlags.LOG_QUERY_PARAMETERS).buildDefault()

    }

    @After
    fun tearDown() {
        BoxStore.clearDefaultStore()
        if (store != null) {
            store?.close()
            store = null
        }
        BoxStore.deleteAllFiles(TEST_DIRECTORY)

    }

}