package tiled.util;

/**
 * Created by BanditCat on 2016/12/13.
 */

public class EmptyTileImage {
    float u, v;
    float u2, v2;
    int regionWidth, regionHeight;
    public static final int SIZE_WIDTH = 32;
    public static final int SIZE_HEIGHT = 32;
    /** Constructs a region with no texture and no coordinates defined. */

    /** Constructs a region the size of the specified texture. */
    public EmptyTileImage() {
        setRegion(0, 0, SIZE_WIDTH, SIZE_HEIGHT);
    }

    /** @param width The width of the texture region. May be negative to flip the sprite when drawn.
     * @param height The height of the texture region. May be negative to flip the sprite when drawn. */
    public EmptyTileImage(int width, int height) {
        setRegion(0, 0, width, height);
    }

    /** @param width The width of the texture region. May be negative to flip the sprite when drawn.
     * @param height The height of the texture region. May be negative to flip the sprite when drawn. */
    public EmptyTileImage(int x, int y, int width, int height) {
        setRegion(x, y, width, height);
    }

    public EmptyTileImage(float u, float v, float u2, float v2) {
        setRegion(u, v, u2, v2);
    }

    /** Constructs a region with the same texture and coordinates of the specified region. */
    public EmptyTileImage(EmptyTileImage region) {
        setRegion(region);
    }

    /** Constructs a region with the same texture as the specified region and sets the coordinates relative to the specified region.
     * @param width The width of the texture region. May be negative to flip the sprite when drawn.
     * @param height The height of the texture region. May be negative to flip the sprite when drawn. */
    public EmptyTileImage(EmptyTileImage region, int x, int y, int width, int height) {
        setRegion(region, x, y, width, height);
    }

    /** Sets the texture and sets the coordinates to the size of the specified texture. */
    public void setRegion () {
        setRegion(0, 0, SIZE_WIDTH, SIZE_HEIGHT);
    }

    /** @param width The width of the texture region. May be negative to flip the sprite when drawn.
     * @param height The height of the texture region. May be negative to flip the sprite when drawn. */
    public void setRegion (int x, int y, int width, int height) {
        float invTexWidth = 1f / SIZE_WIDTH;
        float invTexHeight = 1f / SIZE_HEIGHT;
        setRegion(x * invTexWidth, y * invTexHeight, (x + width) * invTexWidth, (y + height) * invTexHeight);
        regionWidth = Math.abs(width);
        regionHeight = Math.abs(height);
    }

    public void setRegion (float u, float v, float u2, float v2) {
        int texWidth = SIZE_WIDTH, texHeight = SIZE_HEIGHT;
        regionWidth = Math.round(Math.abs(u2 - u) * texWidth);
        regionHeight = Math.round(Math.abs(v2 - v) * texHeight);

        // For a 1x1 region, adjust UVs toward pixel center to avoid filtering artifacts on AMD GPUs when drawing very stretched.
        if (regionWidth == 1 && regionHeight == 1) {
            float adjustX = 0.25f / texWidth;
            u += adjustX;
            u2 -= adjustX;
            float adjustY = 0.25f / texHeight;
            v += adjustY;
            v2 -= adjustY;
        }

        this.u = u;
        this.v = v;
        this.u2 = u2;
        this.v2 = v2;
    }

    /** Sets the texture and coordinates to the specified region. */
    public void setRegion (EmptyTileImage region) {
        setRegion(region.u, region.v, region.u2, region.v2);
    }

    /** Sets the texture to that of the specified region and sets the coordinates relative to the specified region. */
    public void setRegion (EmptyTileImage region, int x, int y, int width, int height) {
        setRegion(region.getRegionX() + x, region.getRegionY() + y, width, height);
    }

    public float getU () {
        return u;
    }

    public void setU (float u) {
        this.u = u;
        regionWidth = Math.round(Math.abs(u2 - u) * SIZE_WIDTH);
    }

    public float getV () {
        return v;
    }

    public void setV (float v) {
        this.v = v;
        regionHeight = Math.round(Math.abs(v2 - v) * SIZE_HEIGHT);
    }

    public float getU2 () {
        return u2;
    }

    public void setU2 (float u2) {
        this.u2 = u2;
        regionWidth = Math.round(Math.abs(u2 - u) * SIZE_WIDTH);
    }

    public float getV2 () {
        return v2;
    }

    public void setV2 (float v2) {
        this.v2 = v2;
        regionHeight = Math.round(Math.abs(v2 - v) * SIZE_HEIGHT);
    }

    public int getRegionX () {
        return Math.round(u * SIZE_WIDTH);
    }

    public void setRegionX (int x) {
        setU(x / (float)SIZE_WIDTH);
    }

    public int getRegionY () {
        return Math.round(v * SIZE_HEIGHT);
    }

    public void setRegionY (int y) {
        setV(y / (float)SIZE_HEIGHT);
    }

    /** Returns the region's width. */
    public int getRegionWidth () {
        return regionWidth;
    }

    public void setRegionWidth (int width) {
        if (isFlipX()) {
            setU(u2 + width / (float)SIZE_WIDTH);
        } else {
            setU2(u + width / (float)SIZE_WIDTH);
        }
    }

    /** Returns the region's height. */
    public int getRegionHeight () {
        return regionHeight;
    }

    public void setRegionHeight (int height) {
        if (isFlipY()) {
            setV(v2 + height / (float)SIZE_HEIGHT);
        } else {
            setV2(v + height / (float)SIZE_HEIGHT);
        }
    }

    public void flip (boolean x, boolean y) {
        if (x) {
            float temp = u;
            u = u2;
            u2 = temp;
        }
        if (y) {
            float temp = v;
            v = v2;
            v2 = temp;
        }
    }

    public boolean isFlipX () {
        return u > u2;
    }

    public boolean isFlipY () {
        return v > v2;
    }

    /** Offsets the region relative to the current region. Generally the region's size should be the entire size of the texture in
     * the direction(s) it is scrolled.
     * @param xAmount The percentage to offset horizontally.
     * @param yAmount The percentage to offset vertically. This is done in texture space, so up is negative. */
    public void scroll (float xAmount, float yAmount) {
        if (xAmount != 0) {
            float width = (u2 - u) * SIZE_WIDTH;
            u = (u + xAmount) % 1;
            u2 = u + width / SIZE_WIDTH;
        }
        if (yAmount != 0) {
            float height = (v2 - v) * SIZE_HEIGHT;
            v = (v + yAmount) % 1;
            v2 = v + height / SIZE_HEIGHT;
        }
    }

    /** Helper function to create tiles out of this TextureRegion starting from the top left corner going to the right and ending at
     * the bottom right corner. Only complete tiles will be returned so if the region's width or height are not a multiple of the
     * tile width and height not all of the region will be used. This will not work on texture regions returned form a TextureAtlas
     * that either have whitespace removed or where flipped before the region is split.
     *
     * @param tileWidth a tile's width in pixels
     * @param tileHeight a tile's height in pixels
     * @return a 2D array of TextureRegions indexed by [row][column]. */
    public EmptyTileImage[][] split (int tileWidth, int tileHeight) {
        int x = getRegionX();
        int y = getRegionY();
        int width = regionWidth;
        int height = regionHeight;

        int rows = height / tileHeight;
        int cols = width / tileWidth;

        int startX = x;
        EmptyTileImage[][] tiles = new EmptyTileImage[rows][cols];
        for (int row = 0; row < rows; row++, y += tileHeight) {
            x = startX;
            for (int col = 0; col < cols; col++, x += tileWidth) {
                tiles[row][col] = new EmptyTileImage( x, y, tileWidth, tileHeight);
            }
        }

        return tiles;
    }
}
