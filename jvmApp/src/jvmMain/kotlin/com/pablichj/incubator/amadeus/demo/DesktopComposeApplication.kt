package com.pablichj.incubator.amadeus.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import com.macaosoftware.component.DesktopComponentRender
import com.macaosoftware.component.bottomnavigation.BottomNavigationComponent
import com.macaosoftware.component.bottomnavigation.BottomNavigationComponentDefaults
import com.macaosoftware.component.util.LocalBackPressedDispatcher
import com.macaosoftware.plugin.BackPressDispatcherPlugin
import com.macaosoftware.plugin.DefaultBackPressDispatcherPlugin
import com.pablichj.incubator.amadeus.Database
import com.pablichj.incubator.amadeus.demo.di.DiContainer
import com.pablichj.incubator.amadeus.demo.viewmodel.factory.AppBottomNavigationViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlin.system.exitProcess

@Composable
fun WindowScope.ComposeDesktopApplication(
    windowState: WindowState,
    database: Database
) {
    val backPressDispatcher: BackPressDispatcherPlugin = remember {
        DefaultBackPressDispatcherPlugin()
    }
    val rootComponent = remember(windowState) {
        BottomNavigationComponent(
            viewModelFactory = AppBottomNavigationViewModelFactory(
                diContainer = DiContainer(database),
                BottomNavigationComponentDefaults.createBottomNavigationStatePresenter(
                    dispatcher = Dispatchers.Main
                ),
                onBackPress = { exitProcess(0) }
            ),
            content = BottomNavigationComponentDefaults.BottomNavigationComponentView
        )
    }
    MaterialTheme {
        Column {
            WindowDraggableArea {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color.LightGray)
                ) {
                    Row {
                        Spacer(modifier = Modifier.size(20.dp))
                        Box(
                            modifier = Modifier
                                .padding(top = 14.dp, end = 8.dp)
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                                .clickable {
                                    exitProcess(0)
                                }
                        )
                        Box(
                            modifier = Modifier
                                .padding(top = 14.dp, end = 8.dp)
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(Color.Yellow)
                                .clickable {
                                    windowState.isMinimized = true
                                }
                        )
                        Box(
                            modifier = Modifier
                                .padding(top = 14.dp)
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(Color.Green)
                                .clickable {
                                    // Add code to maximize the window
                                }
                        )
                    }
                    Row(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        Box(
                            modifier = Modifier.clickable {
                                backPressDispatcher.dispatchBackPressed()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "back"
                            )
                        }
                        Spacer(modifier = Modifier.size(20.dp))
                    }

                }
            }
            CompositionLocalProvider(
                value = LocalBackPressedDispatcher provides backPressDispatcher
            ) {
                DesktopComponentRender(
                    rootComponent = rootComponent,
                    windowState = windowState
                )
            }
        }
    }
}
