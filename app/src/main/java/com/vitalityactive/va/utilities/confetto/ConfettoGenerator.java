package com.vitalityactive.va.utilities.confetto;

import java.util.Random;

interface ConfettoGenerator {
    /**
     * Generate a random confetto to animate.
     *
     * @param random a {@link Random} that can be used to generate random confetto.
     * @return the randomly generated confetto.
     */
    Confetto generateConfetto(Random random);
}
