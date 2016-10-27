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

import java.util.LinkedList;
import java.util.Iterator;
import java.util.List;

import tiled.awt.Rectangle;
import tiled.awt.Rectangle2D;
import tiled.awt.Ellipse2D;
import tiled.awt.Shape;

/**
 * A layer containing {@link MapObject map objects}.
 *
 * @author Thorbjørn Lindeijer
 * @author Adam Turk
 * @version 0.17
 */
public class ObjectGroup extends MapLayer implements Iterable<MapObject> {

    private List<MapObject> objects = new LinkedList<MapObject>();
    private String draworder;

    /**
     * Default constructor.
     */
    public ObjectGroup() {
    }

    /**
     * <p>Constructor for ObjectGroup.</p>
     *
     * @param map the map this object group is part of
     */
    public ObjectGroup(Map map) {
        super(map);
    }

    /**
     * Creates an object group that is part of the given map and has the given
     * origin.
     *
     * @param map the map this object group is part of
     * @param origX the x origin of this layer
     * @param origY the y origin of this layer
     */
    public ObjectGroup(Map map, int origX, int origY) {
        super(map);
        setBounds(new Rectangle(origX, origY, 0, 0));
    }

    /**
     * Creates an object group with a given area. The size of area is
     * irrelevant, just its origin.
     *
     * @param area the area of the object group
     */
    public ObjectGroup(Rectangle area) {
        super(area);
    }

    /**
     * gets the draw order (topdown, index)
     *
     * @return draw order
     */
    public String getDraworder() {
        return draworder;
    }

    /**
     * sets the draw order
     *
     * @param draworder "topdown" or "index"
     */
    public void setDraworder(String draworder) {
        this.draworder = draworder;
    }

    /**
     * sets the height
     *
     * @param height height
     */
    public void setHeight(int height) {
        super.getBounds().height = height;
    }

    /**
     * sets the weight
     *
     * @param width width
     */
    public void setWidth(int width) {
        super.getBounds().width = width;
    }

    /** {@inheritDoc} */
    @Override
    public void rotate(int angle) {
        // TODO: Implement rotating an object group
    }

    /** {@inheritDoc} */
    @Override
    public void mirror(int dir) {
        // TODO: Implement mirroring an object group
    }

    /** {@inheritDoc} */
    @Override
    public void mergeOnto(MapLayer other) {
        // TODO: Implement merging with another object group
    }

//    /** {@inheritDoc} */
//    @Override
//    public void maskedMergeOnto(MapLayer other, Area mask) {
//        // TODO: Figure out what object group should do with this method
//    }

    /** {@inheritDoc} */
    @Override
    public void copyFrom(MapLayer other) {
        // TODO: Implement copying from another object group (same as merging)
    }

//    /** {@inheritDoc} */
//    @Override
//    public void maskedCopyFrom(MapLayer other, Area mask) {
//        // TODO: Figure out what object group should do with this method
//    }

    /** {@inheritDoc} */
    @Override
    public void copyTo(MapLayer other) {
        // TODO: Implement copying to another object group (same as merging)
    }

    /** {@inheritDoc} */
    @Override
    public void resize(int width, int height, int dx, int dy) {
        // TODO: Translate contained objects by the change of origin
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty() {
        return objects.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public Object clone() throws CloneNotSupportedException {
        ObjectGroup clone = (ObjectGroup) super.clone();
        clone.objects = new LinkedList<MapObject>();
        for (MapObject object : objects) {
            final MapObject objectClone = (MapObject) object.clone();
            clone.objects.add(objectClone);
            objectClone.setObjectGroup(clone);
        }
        return clone;
    }

    /** {@inheritDoc} */
    @Override
    @Deprecated
    public MapLayer createDiff(MapLayer ml) {
        return null;
    }

    /**
     * <p>addObject.</p>
     *
     * @param o a {@link MapObject} object.
     */
    public void addObject(MapObject o) {
        objects.add(o);
        o.setObjectGroup(this);
    }

    /**
     * <p>removeObject.</p>
     *
     * @param o a {@link MapObject} object.
     */
    public void removeObject(MapObject o) {
        objects.remove(o);
        o.setObjectGroup(null);
    }

    /**
     * <p>Getter for the field <code>objects</code>.</p>
     *
     * @return a {@link Iterator} object.
     */
    public Iterator<MapObject> getObjects() {
        return objects.iterator();
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<MapObject> iterator() {
        return objects.iterator();
    }

    /**
     * <p>getObjectAt.</p>
     *
     * @param x a double.
     * @param y a double.
     * @return a {@link MapObject} object.
     */
    public MapObject getObjectAt(double x, double y) {
        for (MapObject obj : objects) {
            // Attempt to get an object bordering the point that has no width
            if (obj.getWidth() == 0 && obj.getX() + bounds.x == x) {
                return obj;
            }

            // Attempt to get an object bordering the point that has no height
            if (obj.getHeight() == 0 && obj.getY() + bounds.y == y) {
                return obj;
            }

            Rectangle2D.Double rect = new Rectangle2D.Double(obj.getX() + bounds.x * myMap.getTileWidth(),
                    obj.getY() + bounds.y * myMap.getTileHeight(),
                    obj.getWidth(), obj.getHeight());
            if (rect.contains(x, y)) {
                return obj;
            }
        }
        return null;
    }

    // This method will work at any zoom level, provided you provide the correct zoom factor. It also adds a one pixel buffer (that doesn't change with zoom).
    /**
     * <p>getObjectNear.</p>
     *
     * @param x a int.
     * @param y a int.
     * @param zoom a double.
     * @return a {@link MapObject} object.
     */
    public MapObject getObjectNear(int x, int y, double zoom) {
        Rectangle2D mouse = new Rectangle2D.Double(x - zoom - 1, y - zoom - 1, 2 * zoom + 1, 2 * zoom + 1);
        Shape shape;

        for (MapObject obj : objects) {
            if (obj.getWidth() == 0 && obj.getHeight() == 0) {
                shape = new Ellipse2D.Double(obj.getX() * zoom, obj.getY() * zoom, 10 * zoom, 10 * zoom);
            } else {
                shape = new Rectangle2D.Double(obj.getX() + bounds.x * myMap.getTileWidth(),
                        obj.getY() + bounds.y * myMap.getTileHeight(),
                        obj.getWidth() > 0 ? obj.getWidth() : zoom,
                        obj.getHeight() > 0 ? obj.getHeight() : zoom);
            }

            if (shape.intersects(mouse)) {
                return obj;
            }
        }

        return null;
    }

}
