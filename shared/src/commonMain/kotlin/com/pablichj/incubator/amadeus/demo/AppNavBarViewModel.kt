package com.pablichj.incubator.amadeus.demo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import com.macaosoftware.component.core.NavItem
import com.macaosoftware.component.core.setNavItems
import com.macaosoftware.component.navbar.NavBarComponent
import com.macaosoftware.component.navbar.NavBarComponentViewModel
import com.macaosoftware.component.navbar.NavBarStatePresenterDefault
import com.pablichj.incubator.amadeus.Database

class AppNavBarViewModel(
    private val database: Database
) : NavBarComponentViewModel<NavBarStatePresenterDefault>() {

    private lateinit var navBarComponent: NavBarComponent<NavBarStatePresenterDefault>

    override fun onCreate(navBarComponent: NavBarComponent<NavBarStatePresenterDefault>) {
        this.navBarComponent = navBarComponent
        navBarComponent.setNavItems(
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
            selectedIndex = 0
        )
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun onDestroy() {
    }
}
