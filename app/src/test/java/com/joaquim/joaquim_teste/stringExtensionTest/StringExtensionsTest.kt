package com.joaquim.joaquim_teste.stringExtensionTest

import com.google.common.truth.Truth.assertThat
import com.joaquim.joaquim_teste.data.commom.extensions.addHttpsIfNeeded
import org.junit.Test

class StringExtensionsTest {

    @Test
    fun `when string have an image path with http change it to https`() {
        // Given some image path string
        val imagePath = "http://firebasestorage.googleapis.com/v0/b/meus-atendimentos.appspot.com/o/remote_messages%2Fxablau!.gif?alt=media&token=5201120d-6a68-4478-a9b5-a2d53d58b9b7"
        val eventDetailImage1 = imagePath
        val eventDetailImage2 = "Some text before image path $imagePath"

        // when extension is called
        //assert that http was replaced to https
        assertThat(eventDetailImage1.addHttpsIfNeeded()).startsWith("https://")
        assertThat(eventDetailImage2.addHttpsIfNeeded()).contains("https://")
        assertThat(eventDetailImage1.addHttpsIfNeeded()).doesNotContain("http://")
        assertThat(eventDetailImage2.addHttpsIfNeeded()).doesNotContain("http://")
    }

}