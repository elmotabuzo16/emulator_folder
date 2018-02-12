package com.vitalityactive.va.settings;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;

import javax.inject.Inject;

public class PasswordChangedActivity extends BaseActivity {

    private ImageView imageView;
    private Button doneButton;

    @Inject
    AppConfigRepository appConfigRepository;

    private int globalTintColor, globalTintDarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_done);

        navigationCoordinator.getPNSCaptureDependencyInjector().inject(this);

        globalTintColor = Color.parseColor(appConfigRepository.getGlobalTintColorHex());
        globalTintDarker = Color.parseColor(appConfigRepository.getGlobalTintDarkerColorHex());

        Drawable mIcon;

        mIcon = ContextCompat.getDrawable(this, R.drawable.password_changed_56);
        imageView = findViewById(R.id.change_password_icon);
        imageView.setImageDrawable(mIcon);
        imageView.setColorFilter(globalTintColor);

        doneButton = findViewById(R.id.done_button);
        doneButton.setBackgroundColor(globalTintColor);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setStatusBarColor(globalTintDarker);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
