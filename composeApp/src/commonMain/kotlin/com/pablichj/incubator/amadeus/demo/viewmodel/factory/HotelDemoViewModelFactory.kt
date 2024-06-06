package com.pablichj.incubator.amadeus.demo.viewmodel.factory

import com.macaosoftware.component.viewmodel.ComponentViewModelFactory
import com.macaosoftware.component.viewmodel.StateComponent
import com.pablichj.incubator.amadeus.demo.di.DiContainer
import com.pablichj.incubator.amadeus.demo.viewmodel.HotelDemoViewModel

class HotelDemoViewModelFactory(
    private val diContainer: DiContainer
) : ComponentViewModelFactory<HotelDemoViewModel> {
    override fun create(
        component: StateComponent<HotelDemoViewModel>
    ): HotelDemoViewModel {
        return HotelDemoViewModel(diContainer.database)
    }
}
