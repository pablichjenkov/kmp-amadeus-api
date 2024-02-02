package com.pablichj.incubator.amadeus.demo.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import com.macaosoftware.component.core.NavItem
import com.macaosoftware.component.core.setNavItems
import com.macaosoftware.component.bottomnavigation.BottomNavigationComponent
import com.macaosoftware.component.bottomnavigation.BottomNavigationComponentViewModel
import com.macaosoftware.component.bottomnavigation.BottomNavigationStatePresenterDefault
import com.macaosoftware.component.viewmodel.StateComponent
import com.pablichj.incubator.amadeus.demo.di.DiContainer
import com.pablichj.incubator.amadeus.demo.view.AirportDemoComponentView
import com.pablichj.incubator.amadeus.demo.view.HotelDemoComponentView
import com.pablichj.incubator.amadeus.demo.viewmodel.factory.AirportDemoViewModelFactory
import com.pablichj.incubator.amadeus.demo.viewmodel.factory.HotelDemoViewModelFactory

class AppBottomNavigationViewModel(
    private val diContainer: DiContainer,
    bottomNavigationComponent: BottomNavigationComponent<AppBottomNavigationViewModel>,
    override val bottomNavigationStatePresenter: BottomNavigationStatePresenterDefault,
) : BottomNavigationComponentViewModel(bottomNavigationComponent) {

    override fun onAttach() {

        bottomNavigationComponent.setNavItems(
            mutableListOf(
                NavItem(
                    label = "Hotel",
                    component = StateComponent<HotelDemoViewModel>(
                        viewModelFactory = HotelDemoViewModelFactory(diContainer),
                        content = HotelDemoComponentView
                    ),
                    icon = Icons.Default.Home
                ),
                NavItem(
                    label = "Air",
                    component = StateComponent<AirportDemoViewModel>(
                        viewModelFactory = AirportDemoViewModelFactory(diContainer),
                        content = AirportDemoComponentView
                    ),
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

    override fun onDetach() {
    }
}
