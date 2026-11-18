package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.EmergencyAlertHelper
import com.example.data.UserLocation
import com.example.data.entity.VerifiedHelplines
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun readStringFromContext() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Raksha Kavach", appName)
    }

    @Test
    fun verifyHelplineCatalogHasCoreNumbers() {
        val numbers = VerifiedHelplines.list.map { it.number }
        assertTrue("Must include national 112", numbers.contains("112"))
        assertTrue("Must include Women Helpline 181", numbers.contains("181"))
        assertTrue("Must include Women Police 1091", numbers.contains("1091"))
        assertTrue("Must include Cybercrime 1930", numbers.contains("1930"))
        assertTrue("Must include Childline 1098", numbers.contains("1098"))
        assertTrue("Must include Ambulance 108", numbers.contains("108"))
        assertTrue("Must include NCW 14490", numbers.contains("14490"))
    }

    @Test
    fun verifyEmergencyMessageBuilder() {
        val testLoc = UserLocation(22.5726, 88.3639, 10f)
        val message = EmergencyAlertHelper.buildEmergencyMessage(
            customPrefix = "I need urgent assistance",
            location = testLoc,
            evidenceAttachedNote = "Near Park Street"
        )
        assertTrue(message.contains("EMERGENCY SOS"))
        assertTrue(message.contains("I need urgent assistance"))
        assertTrue(message.contains("22.5726"))
        assertTrue(message.contains("maps.google.com"))
        assertTrue(message.contains("Near Park Street"))
    }
}
