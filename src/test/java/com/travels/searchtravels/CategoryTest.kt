package com.travels.searchtravels


import android.graphics.Bitmap
import android.os.Looper.getMainLooper
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.api.services.vision.v1.model.LatLng
import com.travels.searchtravels.api.OnVisionApiListener
import com.travels.searchtravels.api.VisionApi
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf


@RunWith(AndroidJUnit4::class)
class CategoryTest {
    private val bitmap = Bitmap.createBitmap(1,1, Bitmap.Config.ALPHA_8)

    @Test
    fun testMountain() {
        testCategory("mountain")
    }

    @Test
    fun testBeach() {
        testCategory("beach")
    }

    @Test
    fun testSea() {
        testCategory("sea")
    }

    @Test
    fun testOcean() {
        testCategory("ocean")
    }

    @Test
    fun testSnow() {
        testCategory("snow")
    }

    private fun testCategory(testCategory: String) {
        var errorPlaceCalled = false
        VisionApi.setGoogleResponseCreator(GoogleResponseCreatorMocks.getMockForCategory(testCategory))
        VisionApi.findLocation(bitmap, "", object : OnVisionApiListener {
            override fun onSuccess(latLng: LatLng?) {
                fail("onSuccessCalled")
            }

            override fun onErrorPlace(category: String?) {
                assertEquals("Categories didn't match", testCategory, category)
                errorPlaceCalled = true
            }

            override fun onError() {
                fail("onErrorCalled")
            }
        })

        try {
            Thread.sleep(1000)
        } catch (ignore: Exception) {
        }
        shadowOf(getMainLooper()).idle()

        assertTrue("onErrorPlace wasn't called", errorPlaceCalled)
    }
}