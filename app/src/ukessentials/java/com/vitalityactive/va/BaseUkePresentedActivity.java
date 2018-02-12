package com.vitalityactive.va;

import javax.inject.Inject;

public abstract class BaseUkePresentedActivity<UI, P extends Presenter<UI>> extends BasePresentedActivity<UI, P> {
    @Inject
    protected UkeNavigationCoordinator ukeNavigationCoordinator;
}
