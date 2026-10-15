package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Word Finder Magic", appName)
  }

  @Test
  fun `test paywall five puzzle limit and unlock`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = com.example.data.PreferencesManager(context)

    // Initially 5 free puzzles remaining
    assertEquals(5, com.example.data.PreferencesManager.MAX_FREE_PUZZLES)
    org.junit.Assert.assertFalse(prefs.isPremiumUnlocked())

    // Increment 5 times
    for (i in 1..5) {
      prefs.incrementPuzzlesPlayed()
    }
    assertEquals(5, prefs.getPuzzlesPlayed())
    assertEquals(0, prefs.getRemainingFreePuzzles())

    // Unlock premium
    prefs.setPremiumUnlocked(true)
    org.junit.Assert.assertTrue(prefs.isPremiumUnlocked())
    assertEquals(Int.MAX_VALUE, prefs.getRemainingFreePuzzles())
  }
}
