package com.pablichj.incubator.amadeus.demo.viewmodel.factory

import com.macaosoftware.component.navbar.BottomNavigationComponent
import com.macaosoftware.component.navbar.BottomNavigationComponentViewModelFactory
import com.macaosoftware.component.navbar.BottomNavigationStatePresenterDefault
import com.pablichj.incubator.amadeus.demo.di.DiContainer
import com.pablichj.incubator.amadeus.demo.viewmodel.AppBottomNavigationViewModel

class AppBottomNavigationViewModelFactory(
    private val diContainer: DiContainer,
    private val bottomNavigationStatePresenter: BottomNavigationStatePresenterDefault
) : BottomNavigationComponentViewModelFactory<AppBottomNavigationViewModel> {

    override fun create(
        bottomNavigationComponent: BottomNavigationComponent<AppBottomNavigationViewModel>
    ): AppBottomNavigationViewModel {
        return AppBottomNavigationViewModel(
            diContainer = diContainer,
            bottomNavigationComponent = bottomNavigationComponent,
            bottomNavigationStatePresenter = bottomNavigationStatePresenter
        )
    }
}
