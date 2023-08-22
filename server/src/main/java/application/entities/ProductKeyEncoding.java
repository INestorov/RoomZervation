package application.entities;

import com.weilerhaus.productKeys.beans.ProductKeyEncodingData;

public class ProductKeyEncoding implements ProductKeyEncodingData {

    /* PRIVATE VARIABLES */
    /**
     * This will store the 1st byte.
     */
    private final byte a1;
    /**
     * This will store the 2nd byte.
     */
    private final byte b1;
    /**
     * This will store the 3rd byte.
     */
    private final byte c1;

    /* CONSTRUCTORS */

    /**
     * This will create a new instance of a ProductKeyEncoding.
     *
     * @param a1 The 1st byte.
     * @param b1 1The 2nd byte.
     * @param c1 The 3rd byte.
     */
    public ProductKeyEncoding(final byte a1, final byte b1, final byte c1) {
        this.a1 = a1;
        this.b1 = b1;
        this.c1 = c1;
    }

    /* PUBLIC METHODS */

    /**
     * This will get the 1st byte.
     *
     * @return The 1st byte.
     */
    public byte getA() {
        return this.a1;
    }

    /**
     * This will get the 2nd byte.
     *
     * @return The 2nd byte.
     */
    public byte getB() {
        return this.b1;
    }

    /**
     * This will get the 3rd byte.
     *
     * @return The 3rd byte.
     */
    public byte getC() {
        return this.c1;
    }

}



