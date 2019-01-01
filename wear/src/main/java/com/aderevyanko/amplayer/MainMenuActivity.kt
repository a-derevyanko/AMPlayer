package com.aderevyanko.amplayer

import android.os.Bundle
import android.support.wearable.activity.WearableActivity

class MainMenuActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        // Enables Always-on
        setAmbientEnabled()
    }
}
