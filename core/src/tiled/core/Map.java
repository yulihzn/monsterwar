/*-
 * #%L
 * This file is part of libtiled-java.
 * %%
 * Copyright (C) 2004 - 2016 Thorbjørn Lindeijer <thorbjorn@lindeijer.nl>
 * Copyright (C) 2004 - 2016 Adam Turk <aturk@biggeruniverse.com>
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package tiled.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import tiled.awt.Rectangle;

/**
 * The Map class is the focal point of the <code>tiled.core</code> package.
 *
 * @author Thorbjørn Lindeijer
 * @author Adam Turk
 * @author Mike Thomas
 * @version 0.17
 */
public class Map implements Iterable<MapLayer> {

    /**
     * Constant <code>ORIENTATION_ORTHOGONAL=1</code>
     */
    public static final int ORIENTATION_ORTHOGONAL = 1;
    /**
     * Constant <code>ORIENTATION_ISOMETRIC=2</code>
     */
    public static final int ORIENTATION_ISOMETRIC = 2;
    /**
     * Constant <code>ORIENTATION_HEXAGONAL=4</code>
     *
     * @since 0.11
     */
    public static final int ORIENTATION_HEXAGONAL = 4;
    /**
     * Shifted (used for iso and hex).
     */
    public static final int ORIENTATION_SHIFTED = 5;

    private List<MapLayer> layers;
    private List<TileSet> tileSets;

    private int tileWidth, tileHeight;
    private int orientation = ORIENTATION_ORTHOGONAL;
    private Properties properties;
    private String filename;
    protected Rectangle bounds;          // in tiles

    /**
     * <p>
     * Constructor for Map.</p>
     *
     * @param width the map width in tiles.
     * @param height the map height in tiles.
     */
    public Map(int width, int height) {
        layers = new ArrayList<MapLayer>();
        bounds = new Rectangle(width, height);
        properties = new Properties();
        tileSets = new ArrayList<TileSet>();
    }

    /**
     * Returns the total number of layers.
     *
     * @return the size of the layer vector
     */
    public int getLayerCount() {
        return layers.size();
    }

    /**
     * Changes the bounds of this plane to include all layers completely.
     */
    public void fitBoundsToLayers() {
        int width = 0;
        int height = 0;

        Rectangle layerBounds = new Rectangle();

        for (int i = 0; i < layers.size(); i++) {
            getLayer(i).getBounds(layerBounds);
            if (width < layerBounds.width) {
                width = layerBounds.width;
            }
            if (height < layerBounds.height) {
                height = layerBounds.height;
            }
        }

        bounds.width = width;
        bounds.height = height;
    }

    /**
     * Returns a <code>Rectangle</code> representing the maximum bounds in
     * tiles.
     *
     * @return a new rectangle containing the maximum bounds of this plane
     */
    public Rectangle getBounds() {
        return new Rectangle(bounds);
    }

    /**
     * <p>addLayer.</p>
     *
     * @param layer a {@link MapLayer} object.
     * @return a {@link MapLayer} object.
     */
    public MapLayer addLayer(MapLayer layer) {
        layer.setMap(this);
        layers.add(layer);
        return layer;
    }

    /**
     * <p>setLayer.</p>
     *
     * @param index a int.
     * @param layer a {@link MapLayer} object.
     */
    public void setLayer(int index, MapLayer layer) {
        layer.setMap(this);
        layers.set(index, layer);
    }

    /**
     * <p>insertLayer.</p>
     *
     * @param index a int.
     * @param layer a {@link MapLayer} object.
     */
    public void insertLayer(int index, MapLayer layer) {
        layer.setMap(this);
        layers.add(index, layer);
    }

    /**
     * Removes the layer at the specified index. Layers above this layer will
     * move down to fill the gap.
     *
     * @param index the index of the layer to be removed
     * @return the layer that was removed from the list
     */
    public MapLayer removeLayer(int index) {
        return layers.remove(index);
    }

    /**
     * Removes all layers from the plane.
     */
    public void removeAllLayers() {
        layers.clear();
    }

    /**
     * Returns the layer list.
     *
     * @return List the layer list
     */
    public List<MapLayer> getLayers() {
        return layers;
    }

    /**
     * Sets the layer list to the given java.util.List.
     *
     * @param layers the new set of layers
     */
    public void setLayers(List<MapLayer> layers) {
        this.layers = layers;
    }

    /**
     * Returns the layer at the specified vector index.
     *
     * @param i the index of the layer to return
     * @return the layer at the specified index, or null if the index is out of
     * bounds
     */
    public MapLayer getLayer(int i) {
        try {
            return layers.get(i);
        } catch (ArrayIndexOutOfBoundsException e) {
            // todo: we should log this
        }
        return null;
    }

    /**
     * Resizes this plane. The (dx, dy) pair determines where the original plane
     * should be positioned on the new area. Only layers that exactly match the
     * bounds of the map are resized, any other layers are moved by the given
     * shift.
     *
     * @see MapLayer#resize
     * @param width The new width of the map.
     * @param height The new height of the map.
     * @param dx The shift in x direction in tiles.
     * @param dy The shift in y direction in tiles.
     */
    public void resize(int width, int height, int dx, int dy) {
        for (MapLayer layer : this) {
            if (layer.bounds.equals(bounds)) {
                layer.resize(width, height, dx, dy);
            } else {
                layer.setOffset(layer.bounds.x + dx, layer.bounds.y + dy);
            }
        }

        bounds.width = width;
        bounds.height = height;
    }

    /**
     * Determines whether the point (x,y) falls within the plane.
     *
     * @param x a int.
     * @param y a int.
     * @return <code>true</code> if the point is within the plane,
     * <code>false</code> otherwise
     */
    public boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < bounds.width && y < bounds.height;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<MapLayer> iterator() {
        return layers.iterator();
    }

    /**
     * Adds a Tileset to this Map. If the set is already attached to this map,
     * <code>addTileset</code> simply returns.
     *
     * @param tileset a tileset to add
     */
    public void addTileset(TileSet tileset) {
        if (tileset == null || tileSets.indexOf(tileset) > -1) {
            return;
        }

        Tile t = tileset.getTile(0);

        if (t != null) {
            int tw = t.getWidth();
            int th = t.getHeight();
            if (tw != tileWidth) {
                if (tileWidth == 0) {
                    tileWidth = tw;
                    tileHeight = th;
                }
            }
        }

        tileSets.add(tileset);
    }

    /**
     * Removes a {@link TileSet} from the map, and removes any tiles
     * in the set from the map layers.
     *
     * @param tileset TileSet to remove
     */
    public void removeTileset(TileSet tileset) {
        // Sanity check
        final int tilesetIndex = tileSets.indexOf(tileset);
        if (tilesetIndex == -1) {
            return;
        }

        // Go through the map and remove any instances of the tiles in the set
        for (Tile tile : tileset) {
            for (MapLayer ml : this) {
                if (ml instanceof TileLayer) {
                    ((TileLayer) ml).removeTile(tile);
                }
            }
        }

        tileSets.remove(tileset);
    }

    /**
     * <p>Getter for the field <code>properties</code>.</p>
     *
     * @return the map properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * <p>Setter for the field <code>properties</code>.</p>
     *
     * @param prop a {@link Properties} object.
     */
    public void setProperties(Properties prop) {
        properties = prop;
    }

    /**
     * <p>Setter for the field <code>filename</code>.</p>
     *
     * @param filename a {@link String} object.
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Sets a new tile width.
     *
     * @param width the new tile width
     */
    public void setTileWidth(int width) {
        tileWidth = width;
    }

    /**
     * Sets a new tile height.
     *
     * @param height the new tile height
     */
    public void setTileHeight(int height) {
        tileHeight = height;
    }

    /**
     * <p>Setter for the field <code>orientation</code>.</p>
     *
     * @param orientation a int.
     */
    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    /**
     * <p>Getter for the field <code>filename</code>.</p>
     *
     * @return a {@link String} object.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Returns a list with the currently loaded tileSets.
     *
     * @return List
     */
    public List<TileSet> getTileSets() {
        return tileSets;
    }

    /**
     * Returns width of map in tiles.
     *
     * @return int
     */
    public int getWidth() {
        return bounds.width;
    }

    /**
     * Returns height of map in tiles.
     *
     * @return int
     */
    public int getHeight() {
        return bounds.height;
    }

    /**
     * Returns default tile width for this map.
     *
     * @return the default tile width
     */
    public int getTileWidth() {
        return tileWidth;
    }

    /**
     * Returns default tile height for this map.
     *
     * @return the default tile height
     */
    public int getTileHeight() {
        return tileHeight;
    }

    /**
     * Returns whether the given tile coordinates fall within the map
     * boundaries.
     *
     * @param x The tile-space x-coordinate
     * @param y The tile-space y-coordinate
     * @return <code>true</code> if the point is within the map boundaries,
     * <code>false</code> otherwise
     */
    public boolean contains(int x, int y) {
        return x >= 0 && y >= 0 && x < bounds.width && y < bounds.height;
    }

    /**
     * Returns the maximum tile height. This is the height of the highest tile
     * in all tileSets or the tile height used by this map if it's smaller.
     *
     * @return int The maximum tile height
     */
    public int getTileHeightMax() {
        int maxHeight = tileHeight;

        for (TileSet tileset : tileSets) {
            int height = tileset.getTileHeight();
            if (height > maxHeight) {
                maxHeight = height;
            }
        }

        return maxHeight;
    }

    /**
     * Swaps the tile sets at the given indices.
     *
     * @param index0 a int.
     * @param index1 a int.
     */
    public void swapTileSets(int index0, int index1) {
        if (index0 == index1) {
            return;
        }
        TileSet set = tileSets.get(index0);
        tileSets.set(index0, tileSets.get(index1));
        tileSets.set(index1, set);
    }

    /**
     * Returns the orientation of this map. Orientation will be one of
     * {@link Map#ORIENTATION_ISOMETRIC},
     * {@link Map#ORIENTATION_ORTHOGONAL},
     * {@link Map#ORIENTATION_HEXAGONAL} and
     * {@link Map#ORIENTATION_SHIFTED}.
     *
     * @return The orientation from the enumerated set
     */
    public int getOrientation() {
        return orientation;
    }

    /**
     * {@inheritDoc}
     *
     * Returns string describing the map. The form is <code>Map[width x height
     * x layers][tileWidth x tileHeight]</code>, for example <code>
     * Map[64x64x2][24x24]</code>.
     */
    @Override
    public String toString() {
        return "Map[" + bounds.width + "x" + bounds.height + "x"
                + getLayerCount() + "][" + tileWidth + "x"
                + tileHeight + "]";
    }
}
