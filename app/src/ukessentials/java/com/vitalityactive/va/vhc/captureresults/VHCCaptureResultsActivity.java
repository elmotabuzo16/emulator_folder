package com.vitalityactive.va.vhc.captureresults;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;

public class VHCCaptureResultsActivity extends BaseVHCCaptureResultsActivity {

    @Override
    public void showIncompleteBMIInformationAlert() {
        AlertDialogFragment alert =
                AlertDialogFragment.create(INCOMPLETE_BMI_ALERT,
                        getString(R.string.capture_results_missing_points_alert_title_1171),
                        getString(R.string.capture_results_missing_points_alert_message_1172),
                        getString(R.string.cancel_button_title_24),
                        null,
                        getString(R.string.continue_button_title_87));
        alert.show(getSupportFragmentManager(), INCOMPLETE_BMI_ALERT);
    }
}
