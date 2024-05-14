package mff.cuni.szczepaf;

/**
 * Class representing the Rating of a IMedia Entity.
 * Has two fields, ratingValue (from 0 to 1), and ratingCount.
 * Currently, ratingCount is not used in any conditions etc., only the rating value.
 */
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
