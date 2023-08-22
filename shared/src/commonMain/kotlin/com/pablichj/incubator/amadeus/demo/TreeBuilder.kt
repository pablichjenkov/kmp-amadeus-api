package com.pablichj.incubator.amadeus.demo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import com.pablichj.incubator.amadeus.Database
import com.pablichj.templato.component.core.Component
import com.pablichj.templato.component.core.NavItem
import com.pablichj.templato.component.core.NavigationComponent
import com.pablichj.templato.component.core.NavigationComponentDefaultLifecycleHandler
import com.pablichj.templato.component.core.navbar.NavBarComponent
import com.pablichj.templato.component.core.navbar.NavBarStatePresenter
import com.pablichj.templato.component.core.navbar.NavBarStatePresenterDefault
import com.pablichj.templato.component.core.setNavItems
import com.pablichj.templato.component.platform.CoroutineDispatchers
import kotlinx.coroutines.Dispatchers

object TreeBuilder {

    private var navBarComponent: NavBarComponent<NavBarStatePresenterDefault>? = null

    fun getRootComponent(database: Database): Component {

        navBarComponent?.let { return it }

        return NavBarComponent(
            navBarStatePresenter = NavBarComponent.createDefaultNavBarStatePresenter(
                dispatcher = Dispatchers.Main
            ),
            config = NavBarComponent.DefaultConfig,
            lifecycleHandler = NavigationComponentDefaultLifecycleHandler(),
            dispatchers = CoroutineDispatchers.Defaults,
            content = NavBarComponent.DefaultNavBarComponentView
        ).apply {
            navBarComponent = this
            setNavItems(
                mutableListOf(
                    NavItem(
                        label = "Hotel",
                        component = HotelDemoComponent(database),
                        icon = Icons.Default.Home
                    ),
                    NavItem(
                        label = "Air",
                        component = AirportDemoComponent(database),
                        icon = Icons.Default.Search
                    ),
                ),
                newSelectedIndex = 0
            )
        }
    }

}
