package com.vitalityactive.va.utilities.confetto;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

import com.vitalityactive.va.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommonConfetti {
    private static final Paint PAINT = new Paint();
    private static int defaultConfettiSize;
    private static int defaultVelocitySlow;
    private static int defaultVelocityNormal;

    static {
        PAINT.setStyle(Paint.Style.FILL);
    }

    private ConfettiManager confettiManager;

    private CommonConfetti(ViewGroup container) {
        ensureStaticResources(container);
    }

    /**
     * @param container the container view group to host the confetti animation.
     * @param colors    the set of colors to colorize the confetti bitmaps.
     * @return the created common confetti object.
     */
    public static CommonConfetti fallingConfetti(ViewGroup container, int[] colors) {
        final CommonConfetti commonConfetti = new CommonConfetti(container);
        final ConfettiSource confettiSource = new ConfettiSource(0, -defaultConfettiSize,
                container.getWidth(), -defaultConfettiSize);
        commonConfetti.configureRainingConfetti(container, confettiSource, colors);
        return commonConfetti;
    }

    private static Bitmap createConfettoBitmap(int color, int size, int heightToWidthRatio) {
        final Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        PAINT.setColor(color);

        int middle = size / 2;
        int offsetFromMiddle = size / heightToWidthRatio / 2;

        final Path path = new Path();
        path.moveTo(0, middle - offsetFromMiddle);
        path.lineTo(size, middle - offsetFromMiddle);
        path.lineTo(size, middle + offsetFromMiddle);
        path.lineTo(0, middle + offsetFromMiddle);
        path.close();

        canvas.drawPath(path, PAINT);
        return bitmap;
    }

    private static void ensureStaticResources(ViewGroup container) {
        if (defaultConfettiSize == 0) {
            final Resources res = container.getResources();
            defaultConfettiSize = res.getDimensionPixelSize(R.dimen.default_confetti_size);
            defaultVelocitySlow = res.getDimensionPixelOffset(R.dimen.default_velocity_slow);
            defaultVelocityNormal = res.getDimensionPixelOffset(R.dimen.default_velocity_normal);
        }
    }

    public static int[] getLevelColor(int key, Context context) {

        List<Integer> colorList = new ArrayList<>();

        switch (key) {
            case 1:
                colorList.add(R.color.bronzeDark);
                colorList.add(R.color.bronzeMedium);
                break;
            case 2:
                colorList.add(R.color.SilverDark);
                colorList.add(R.color.SilverMedium);
                colorList.add(R.color.SilverLight);
                break;
            case 3:
                colorList.add(R.color.goldDark);
                colorList.add(R.color.goldMedium);
                colorList.add(R.color.goldLight);
                break;
            case 4:
                colorList.add(R.color.platinumDark);
                colorList.add(R.color.platinumMedium);
                colorList.add(R.color.platinumLight);
                break;
            case 5:
                colorList.add(R.color.blueDark);
                colorList.add(R.color.blueLight);
        }

        int[] colors = new int[colorList.size()];
        for (int i = 0; i < colorList.size(); i++) {
            colors[i] = ContextCompat.getColor(context, colorList.get(i));
        }

        return colors;
    }

    public ConfettiManager generate() {
        return confettiManager.setNumInitialCount(0)
                .setEmissionDuration(ConfettiManager.INFINITE_DURATION)
                .setEmissionRate(20)
                .animate();
    }

    private ConfettoGenerator getDefaultGenerator(int[] colors) {
        final List<Bitmap> bitmaps = generateConfettiBitmaps(colors, defaultConfettiSize);
        final int numBitmaps = bitmaps.size();
        return new ConfettoGenerator() {
            @Override
            public Confetto generateConfetto(Random random) {
                return new Confetto(bitmaps.get(random.nextInt(numBitmaps)));
            }
        };
    }

    private List<Bitmap> generateConfettiBitmaps(int[] colors, int size) {
        final List<Bitmap> bitmaps = new ArrayList<>();
        for (int color : colors) {
            for (int denominator = 1; denominator <= 3; denominator++) {
                for (int ratio = 2; ratio <= 3; ratio++) {
                    bitmaps.add(createConfettoBitmap(color, size / denominator, ratio));
                }
            }
        }
        return bitmaps;
    }

    private void configureRainingConfetti(ViewGroup container, ConfettiSource confettiSource,
                                          int[] colors) {
        final Context context = container.getContext();
        final ConfettoGenerator generator = getDefaultGenerator(colors);

        confettiManager = new ConfettiManager(context, generator, confettiSource, container)
                .setVelocityX(0, defaultVelocitySlow)
                .setVelocityY(defaultVelocityNormal, defaultVelocitySlow)
                .setInitialRotation(180, 180);
//                .setRotationalAcceleration(360, 180)
//                .setTargetRotationalVelocity(360);
    }
}
