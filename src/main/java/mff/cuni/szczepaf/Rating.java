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


}
