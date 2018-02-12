package com.vitalityactive.va.snv.partners;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.snv.partners.dto.ProductFeatureGroupDto;

/**
 * Created by kerry.e.lawagan on 12/10/2017.
 */

public class SnvParticipatingPartnersDetailActivity extends BaseActivity {
    public static final String NAME_KEY = "SNV_PARTICIPATING_PARTNERS_ACTIVITY_NAME_KEY";
    public static final String DESC_KEY = "SNV_PARTICIPATING_PARTNERS_ACTIVITY_DESC_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snv_activity_participating_partners_details);

        Bundle extras = getIntent().getExtras();

        setUpActionBarWithTitle(extras.getString(NAME_KEY)).setDisplayHomeAsUpEnabled(true);

        TextView description = findViewById(R.id.snvPartnerDetailsDescription);

        String desc = extras.getString(DESC_KEY).replace("\\n", System.getProperty("line.separator"));
        description.setText(desc);

        if (SnvParticipatingPartnersImageHelper.drawable != null) {
            ImageView image = findViewById(R.id.snvPartnerDetailsImageView);
            image.setImageDrawable(SnvParticipatingPartnersImageHelper.drawable);
        }
    }
}
