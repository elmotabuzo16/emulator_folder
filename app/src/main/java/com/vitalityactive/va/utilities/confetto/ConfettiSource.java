package com.vitalityactive.va.utilities.confetto;

public class ConfettiSource {
    private final int x0, y0, x1, y1;

    /**
     * Specifies a line source from which all confetti will emit from.
     *
     * @param x0 x-coordinate of the first point relative to the {@link ConfettiView}'s parent.
     * @param y0 y-coordinate of the first point relative to the {@link ConfettiView}'s parent.
     * @param x1 x-coordinate of the second point relative to the {@link ConfettiView}'s parent.
     * @param y1 y-coordinate of the second point relative to the {@link ConfettiView}'s parent.
     */
    ConfettiSource(int x0, int y0, int x1, int y1) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }

    float getInitialX(float random) {
        return x0 + (x1 - x0) * random;
    }

    float getInitialY(float random) {
        return y0 + (y1 - y0) * random;
    }
}
