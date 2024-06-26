package com.pablichj.incubator.amadeus.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.macaosoftware.component.AndroidComponentRender
import com.macaosoftware.component.bottomnavigation.BottomNavigationComponent
import com.macaosoftware.component.bottomnavigation.BottomNavigationComponentDefaults
import com.pablichj.incubator.amadeus.demo.di.DiContainer
import com.pablichj.incubator.amadeus.demo.viewmodel.factory.AppBottomNavigationViewModelFactory
import com.pablichj.incubator.amadeus.storage.AndroidDriverFactory
import com.pablichj.incubator.amadeus.storage.createDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val database = withContext(Dispatchers.Default) {
                createDatabase(AndroidDriverFactory(this@MainActivity))
            }
            val rootComponent = BottomNavigationComponent(
                viewModelFactory = AppBottomNavigationViewModelFactory(
                    diContainer = DiContainer(database),
                    BottomNavigationComponentDefaults.createBottomNavigationStatePresenter(
                        dispatcher = Dispatchers.Main
                    ),
                    onBackPress = {
                        finish()
                        true
                    }
                ),
                content = BottomNavigationComponentDefaults.BottomNavigationComponentView
            )
            setContent {
                AndroidComponentRender(
                    rootComponent = rootComponent
                )
            }
        }
    }
}
