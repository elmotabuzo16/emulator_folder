package com.vitalityactive.va.utilities;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.VitalityActiveApplication;
import com.vitalityactive.va.uicomponents.FlexibleDividerItemDecoration;

import java.text.DecimalFormat;

public class ViewUtilities {
    private static final String TAG = "ViewUtilities";
    private static DecimalFormat twoDigitFormat = new DecimalFormat("0.##");
    private static DecimalFormat oneDigitFormat = new DecimalFormat("0.#");

    public static int pxFromDp(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, Resources.getSystem().getDisplayMetrics());
    }

    public static Drawable tintDrawable(Drawable drawable, ColorStateList colorStateList) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(drawable.mutate(), colorStateList);
        return drawable;
    }

    public static Drawable tintDrawable(Drawable drawable, @ColorInt int colorInt) {
        drawable = DrawableCompat.wrap(drawable).mutate();
        DrawableCompat.setTint(drawable, colorInt);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    public static void scrollToTop(final RecyclerView recyclerView) {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollToPosition(0);
            }
        });
    }

    public static void scrollToTop(final ScrollView scrollView) {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });
    }

    public static void scrollToTop(final NestedScrollView scrollView) {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });
    }

    public static void addDividers(Context context, RecyclerView recyclerView) {
        addDividers(context, recyclerView, 0);
    }

    public static void addDividers(Context context, RecyclerView recyclerView, int leftMarginPx) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null) {
            throw new NullPointerException("layoutManager is not set on adapter");
        }
        if (!(layoutManager instanceof LinearLayoutManager)) {
            throw new IllegalArgumentException("layoutManager is not a LinearLayoutManager");
        }
        if (VitalityActiveApplication.isDevDebugBuild() && alreadyHaveAnItemDecorator(recyclerView)) {
            Log.w(TAG, "alreadyHaveAnItemDecorator: this decorator might be darker than expected");
        }
        FlexibleDividerItemDecoration decoration = new FlexibleDividerItemDecoration.Builder(context)
                .setOrientation(((LinearLayoutManager) layoutManager).getOrientation())
                .setDrawLastDivider(false)
                .setLeftMarginPx(leftMarginPx)
                .build();
        recyclerView.addItemDecoration(decoration);
        try {
            recyclerView.setTag(recyclerView.getId(), decoration);
        } catch (Exception ignored) {
        }
    }

    public static boolean alreadyHaveAnItemDecorator(RecyclerView recyclerView) {
        try {
            return recyclerView.getTag(recyclerView.getId()) != null;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static void hideKeyboard(@NonNull Context context, @NonNull View view) {
        hideKeyboardWithoutRequestingFocus(context, view);
        view.requestFocus();
    }

    public static boolean isPointOutsideViewHitRect(int touchX, int touchY, View view) {
        Rect hitRect = new Rect();
        view.getHitRect(hitRect);
        return !hitRect.contains(touchX, touchY);
    }

    public static void hideKeyboardWithoutRequestingFocus(@NonNull Context context, @NonNull View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void swapVisibility(View visible, View invisible) {
        visible.setVisibility(View.VISIBLE);
        invisible.setVisibility(View.GONE);
    }

    public static void moveButtonLeftByAmountOfPadding(Button manageInSettingsButton) {
        int buttonPaddingStart = manageInSettingsButton.getPaddingStart();

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) manageInSettingsButton.getLayoutParams();
        params.setMarginStart(params.getMarginStart() - buttonPaddingStart);

        manageInSettingsButton.setLayoutParams(params);
    }

    public static void removeBottomMargin(TextView description) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) description.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
    }

    @ColorInt
    public static int getColorPrimaryFromTheme(View view) {
        return getColorPrimaryFromTheme(view.getContext());
    }

    @ColorInt
    public static int getColorPrimaryFromTheme(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    public static void setTextOfView(View textView, String text) {
        ((TextView) textView).setText(text);
    }

    public static void setResourceOfView(View icon, int resourceId) {
        ((ImageView) icon).setImageResource(resourceId);
    }

    public static void setSpinnerColor(View view, @ColorInt int spinnerColor) {
        view.getBackground().setColorFilter(spinnerColor, PorterDuff.Mode.SRC_ATOP);
    }

    @NonNull
    public static String removeTrailingZeros(String value) {
        int number = 0;
        try {
            if (!TextUtils.isEmpty(value)) {
                number = Double.valueOf(value).intValue();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "NumberFormatException";
        }
        return Integer.toString(number);
    }

    public static String roundToTwoDecimalsRemoveTrailingZeros(String value) {
        double number = 0;
        try {
            if (!TextUtils.isEmpty(value)) {
                number = Double.parseDouble(value);
            }
        } catch (NumberFormatException ex) {
            return value;
        }

        return twoDigitFormat.format(number);
    }

    public static String roundToNoDecimal(float value) {
        int number = 0;
        try {
            number = (int) value;
        } catch (Exception e) {
            return String.valueOf(value);
        }
        return String.valueOf(number);
    }


    public static int getDrawableWidthDp(Context context, @DrawableRes int drawableId) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), drawableId, o);
        int w = bmp.getWidth();
        return w;
    }

    public static void setViewVisible(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public static void setViewGone(View view) {
        view.setVisibility(View.GONE);
    }

    public static void setTextAndMakeVisibleIfPopulated(TextView view, String text) {
        if (!TextUtilities.isNullOrWhitespace(text)) {
            setTextOfView(view, text);
            setViewVisible(view);
        } else {
            setViewGone(view);
        }
    }

    public static void setHTMLTextAndMakeVisibleIfPopulated(TextView view, CharSequence text) {
        if (!TextUtilities.isNullOrWhitespace(text)) {
            view.setText(text);
            view.setMovementMethod(LinkMovementMethod.getInstance());
            setViewVisible(view);
        } else {
            setViewGone(view);
        }
    }

    public static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }
}
