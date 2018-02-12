package com.vitalityactive.va;

import javax.inject.Inject;

public abstract class BaseUkePresentedFragment<UI, P extends Presenter<UI>> extends BasePresentedFragment<UI, P> {
    @Inject
    protected UkeNavigationCoordinator ukeNavigationCoordinator;
}
