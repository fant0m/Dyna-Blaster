package sk.uniza.fri.fant0m.bomberman;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Animation class.
 * @author fant0m
 */
public class Animation {
    /**
     * ArrayList of frames.
     */
    private ArrayList<AFrame> frames;
    /**
     * Current frame.
     */
    private int currentFrame;
    /**
     * Current time.
     */
    private long time;
    /**
     * Total time of animation.
     */
    private long totalTime;
    /**
     * Is moving.
     */
    private boolean moving;
    /**
     * Display time of dying enemy animation.
     */
    public static final int A_DYING_ENEMY = 400;
    /**
     * Display time of bomb animation.
     */
    public static final int A_BOMB = 319;

    /**
     * Animation constructor.
     */
    public Animation() {
        frames = new ArrayList<>();
        currentFrame = 0;
        time = 0;
        totalTime = 0;
        moving = false;
    }

    /**
     * Add time since last update + control if we dont have to change frame.
     * @param addTime time
     */
    public final void update(final int addTime) {
        time += addTime;

        if (time >= totalTime) {
            time = time % totalTime;
            currentFrame = 0;
        }

        if (time > getFrame(currentFrame).getDelay()) {
            currentFrame++;
        }
    }

    /**
     * Frame getter.
     * @param index frame index
     * @return frame
     */
    public final AFrame getFrame(final int index) {
        return frames.get(index);
    }

    /**
     * Add a frame into animation.
     * @param image image
     * @param delay frame display time(ms)
     */
    public final void addFrame(final BufferedImage image, final long delay) {
        frames.add(new AFrame(image, totalTime + delay));
        totalTime += delay;
    }

    /**
     * Get current frame as image.
     * @return image
     */
    public final BufferedImage getCurrentImage() {
        if (!moving) {
            return frames.get(0).getImage();
        }

        return frames.get(currentFrame).getImage();
    }

    /**
     * Get current frame id.
     * @return id
     */
    public final int getCurrentFrame() {
        return currentFrame;
    }

    /**
     * Turn animation on/off.
     * @param moving moving
     */
    public final void setMoving(final boolean moving) {
        this.moving = moving;
        if (!this.moving) {
            time = 0;
            currentFrame = 0;
        }
    }

    /**
     * Frame class.
     * @author fant0m
     */
    public static class AFrame {
        /**
         * Frame image.
         */
        private BufferedImage image;
        /**
         * Display time.
         */
        private long delay;

        /**
         * Frame constructor.
         * @param image image
         * @param delay delay
         */
        public AFrame(final BufferedImage image, final long delay) {
            this.image = image;
            this.delay = delay;
        }

        /**
         * Image getter.
         * @return image
         */
        public final BufferedImage getImage() {
            return image;
        }

        /**
         * Display time getter.
         * @return delay
         */
        public final long getDelay() {
            return delay;
        }
    }
}
