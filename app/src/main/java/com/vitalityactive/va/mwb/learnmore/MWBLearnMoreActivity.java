package com.vitalityactive.va.mwb.learnmore;

import com.vitalityactive.va.menu.MenuItem;
import java.util.List;

/**
 * Created by christian.j.p.capin on 2/6/2018.
 */

public class MWBLearnMoreActivity extends BaseMWBLearnMoreActivity {
    @Override
    protected void marketUpdateUi(List<MenuItem> configuredMenuItems) {
        sortAlphabetically(configuredMenuItems);
    }
}