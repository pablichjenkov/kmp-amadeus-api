package com.pablichj.incubator.amadeus.demo.viewmodel.factory

import com.macaosoftware.component.viewmodel.ComponentViewModelFactory
import com.macaosoftware.component.viewmodel.StateComponent
import com.pablichj.incubator.amadeus.demo.di.DiContainer
import com.pablichj.incubator.amadeus.demo.viewmodel.AirportDemoViewModel

class AirportDemoViewModelFactory(
    private val diContainer: DiContainer
) : ComponentViewModelFactory<AirportDemoViewModel> {
    override fun create(
        component: StateComponent<AirportDemoViewModel>
    ): AirportDemoViewModel {
        return AirportDemoViewModel(diContainer.database)
    }
}