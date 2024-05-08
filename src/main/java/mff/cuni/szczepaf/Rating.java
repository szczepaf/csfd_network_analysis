package mff.cuni.szczepaf;

public class Rating implements IMediaEntity {
    private float ratingValue;
    private int ratingCount;


    public Rating(float ratingValue, int ratingCount) {
        this.ratingValue = ratingValue;
        this.ratingCount = ratingCount;
    }

    public float getRatingValue() {
        return ratingValue;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    // These functions will be useful for deciding whether a movie has a good enough rating.
    public boolean isLessThan(float f) {
        return this.ratingValue < f;
    }

    public boolean isEqualTo(float f) {
        return this.ratingValue == f;
    }

}
