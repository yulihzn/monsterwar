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
package tiled.io;

import com.badlogic.gdx.graphics.Color;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import tiled.awt.Rectangle;
import tiled.core.AnimatedTile;

import tiled.core.Map;
import tiled.core.MapLayer;
import tiled.core.MapObject;
import tiled.core.ObjectGroup;
import tiled.core.Sprite;
import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.core.TileSet;
import tiled.io.xml.XMLWriter;
import tiled.util.Base64;

/**
 * A writer for Tiled's TMX map format.
 *
 * @author Thorbjørn Lindeijer
 * @author Adam Turk
 * @author Mike Thomas
 * @version 0.17
 */
public class TMXMapWriter {

    private static final int LAST_BYTE = 0x000000FF;

    private static final boolean ENCODE_LAYER_DATA = true;
    private static final boolean COMPRESS_LAYER_DATA = ENCODE_LAYER_DATA;

    private HashMap<TileSet, Integer> firstGidPerTileset;

    public static class Settings {

        @Deprecated
        public static final String LAYER_COMPRESSION_METHOD_GZIP = "gzip";
        public static final String LAYER_COMPRESSION_METHOD_ZLIB = "zlib";

        public String layerCompressionMethod = LAYER_COMPRESSION_METHOD_ZLIB;
    }
    public Settings settings = new Settings();

    /**
     * Saves a map to an XML file.
     *
     * @param map a {@link tiled.core.Map} object.
     * @param filename the filename of the map file
     * @throws IOException if any.
     */
    public void writeMap(Map map, String filename) throws IOException {
//        String s = filename.substring(0,filename.lastIndexOf("/")+1);
//        File dir = new File(s);
//        dir.mkdirs();
//        String n = filename.substring(filename.lastIndexOf("/")+1,filename.length());
//        File file = new File(dir,n);
//        file.createNewFile();
        OutputStream os = new FileOutputStream(filename);

        if (filename.endsWith(".tmx.gz")) {
            os = new GZIPOutputStream(os);
        }

        Writer writer = new OutputStreamWriter(os, Charset.forName("UTF-8"));
        XMLWriter xmlWriter = new XMLWriter(writer);

        xmlWriter.startDocument();
        writeMap(map, xmlWriter, filename);
        xmlWriter.endDocument();

        writer.flush();

        if (os instanceof GZIPOutputStream) {
            ((GZIPOutputStream) os).finish();
        }
    }

    /**
     * Saves a tileset to an XML file.
     *
     * @param set a {@link tiled.core.TileSet} object.
     * @param filename the filename of the tileset file
     * @throws IOException if any.
     */
    public void writeTileset(TileSet set, String filename) throws IOException {
        OutputStream os = new FileOutputStream(filename);
        Writer writer = new OutputStreamWriter(os, Charset.forName("UTF-8"));
        XMLWriter xmlWriter = new XMLWriter(writer);

        xmlWriter.startDocument();
        writeTileset(set, xmlWriter, filename);
        xmlWriter.endDocument();

        writer.flush();
    }

    /**
     * <p>writeMap.</p>
     *
     * @param map a {@link tiled.core.Map} object.
     * @param out a {@link OutputStream} object.
     * @throws Exception if any.
     */
    public void writeMap(Map map, OutputStream out) throws Exception {
        Writer writer = new OutputStreamWriter(out, Charset.forName("UTF-8"));
        XMLWriter xmlWriter = new XMLWriter(writer);

        xmlWriter.startDocument();
        writeMap(map, xmlWriter, "/.");
        xmlWriter.endDocument();

        writer.flush();
    }

    /**
     * <p>writeTileset.</p>
     *
     * @param set a {@link tiled.core.TileSet} object.
     * @param out a {@link OutputStream} object.
     * @throws Exception if any.
     */
    public void writeTileset(TileSet set, OutputStream out) throws Exception {
        Writer writer = new OutputStreamWriter(out, Charset.forName("UTF-8"));
        XMLWriter xmlWriter = new XMLWriter(writer);

        xmlWriter.startDocument();
        writeTileset(set, xmlWriter, "/.");
        xmlWriter.endDocument();

        writer.flush();
    }

    private void writeMap(Map map, XMLWriter w, String wp) throws IOException {
        w.writeDocType("map", null, "http://mapeditor.org/dtd/1.0/map.dtd");
        w.startElement("map");

        w.writeAttribute("version", "1.0");

        switch (map.getOrientation()) {
            case Map.ORIENTATION_ORTHOGONAL:
                w.writeAttribute("orientation", "orthogonal");
                break;
            case Map.ORIENTATION_ISOMETRIC:
                w.writeAttribute("orientation", "isometric");
                break;
            case Map.ORIENTATION_HEXAGONAL:
                w.writeAttribute("orientation", "hexagonal");
                break;
            case Map.ORIENTATION_SHIFTED:
                w.writeAttribute("orientation", "shifted");
                break;
        }

        w.writeAttribute("width", map.getWidth());
        w.writeAttribute("height", map.getHeight());
        w.writeAttribute("tilewidth", map.getTileWidth());
        w.writeAttribute("tileheight", map.getTileHeight());

        writeProperties(map.getProperties(), w);

        firstGidPerTileset = new HashMap<TileSet, Integer>();
        int firstgid = 1;
        for (TileSet tileset : map.getTileSets()) {
            setFirstGidForTileset(tileset, firstgid);
            writeTilesetReference(tileset, w, wp);
            firstgid += tileset.getMaxTileId() + 1;
        }

        for (MapLayer layer : map) {
            writeMapLayer(layer, w, wp);
        }
        firstGidPerTileset = null;

        w.endElement();
    }

    private static void writeProperties(Properties props, XMLWriter w) throws
            IOException {
        if (!props.isEmpty()) {
            final Set<Object> propertyKeys = new TreeSet<Object>();
            propertyKeys.addAll(props.keySet());
            w.startElement("properties");
            for (Object propertyKey : propertyKeys) {
                final String key = (String) propertyKey;
                final String property = props.getProperty(key);
                w.startElement("property");
                w.writeAttribute("name", key);
                if (property.indexOf('\n') == -1) {
                    if ("true".equals(property) || "false".equals(property)) {
                        w.writeAttribute("type", "bool");
                    }
                    w.writeAttribute("value", property);
                } else {
                    // Save multiline values as character data
                    w.writeCDATA(property);
                }
                w.endElement();
            }
            w.endElement();
        }
    }

    /**
     * Writes a reference to an external tileset into a XML document. In the
     * case where the tileset is not stored in an external file, writes the
     * contents of the tileset instead.
     *
     * @param set the tileset to write a reference to
     * @param w the XML writer to write to
     * @param wp the working directory of the map
     * @throws IOException
     */
    private void writeTilesetReference(TileSet set, XMLWriter w, String wp)
            throws IOException {

        String source = set.getSource();

        if (source == null) {
            writeTileset(set, w, wp);
        } else {
            w.startElement("tileset");
            w.writeAttribute("firstgid", getFirstGidForTileset(set));
            w.writeAttribute("source", getRelativePath(wp, source));
            if (set.getBaseDir() != null) {
                w.writeAttribute("basedir", set.getBaseDir());
            }
            w.endElement();
        }
    }

    private void writeTileset(TileSet set, XMLWriter w, String wp)
            throws IOException {

        String tileBitmapFile = set.getTilebmpFile();
        String name = set.getName();

        w.startElement("tileset");
        w.writeAttribute("firstgid", getFirstGidForTileset(set));

        if (name != null) {
            w.writeAttribute("name", name);
        }

        if (tileBitmapFile != null) {
            w.writeAttribute("tilewidth", set.getTileWidth());
            w.writeAttribute("tileheight", set.getTileHeight());

            final int tileSpacing = set.getTileSpacing();
            final int tileMargin = set.getTileMargin();
            if (tileSpacing != 0) {
                w.writeAttribute("spacing", tileSpacing);
            }
            if (tileMargin != 0) {
                w.writeAttribute("margin", tileMargin);
            }
        }

        if (set.getBaseDir() != null) {
            w.writeAttribute("basedir", set.getBaseDir());
        }

        if (tileBitmapFile != null) {
            w.startElement("image");
            w.writeAttribute("source", getRelativePath(wp, tileBitmapFile));

            Color trans = set.getTransparentColor();
            if (trans != null) {
                w.writeAttribute("trans", Integer.toHexString(
                        trans.toIntBits()).substring(2));
            }
            w.endElement();

            // Write tile properties when necessary.
            for (Tile tile : set) {
                // todo: move the null check back into the iterator?
                if (tile != null && !tile.getProperties().isEmpty()) {
                    w.startElement("tile");
                    w.writeAttribute("id", tile.getId());
                    writeProperties(tile.getProperties(), w);
                    w.endElement();
                }
            }
        } else {
                        // Check to see if there is a need to write tile elements
            boolean needWrite = false;

            // As long as one has properties, they all need to be written.
            // TODO: This shouldn't be necessary
            for (Tile tile : set) {
                if (!tile.getProperties().isEmpty() ||
                        tile.getSource() != null) {
                    needWrite = true;
                    break;
                }
            }

            if (needWrite) {
                w.writeAttribute("tilewidth", set.getTileWidth());
                w.writeAttribute("tileheight", set.getTileHeight());
                w.writeAttribute("tilecount", set.size());
                w.writeAttribute("columns", set.getTilesPerRow());

                for (Tile tile : set) {
                    // todo: move this check back into the iterator?
                    if (tile != null) {
                        writeTile(tile, w, wp);
                    }
                }
            }
        }
        w.endElement();
    }

    private static void writeObjectGroup(ObjectGroup o, XMLWriter w, String wp)
            throws IOException {
        Iterator<MapObject> itr = o.getObjects();
        while (itr.hasNext()) {
            writeMapObject(itr.next(), w, wp);
        }
    }

    /**
     * Writes this layer to an XMLWriter. This should be done <b>after</b> the
     * first global ids for the tilesets are determined, in order for the right
     * gids to be written to the layer data.
     */
    private void writeMapLayer(MapLayer l, XMLWriter w, String wp) throws IOException {
        Rectangle bounds = l.getBounds();

        if (l instanceof ObjectGroup) {
            w.startElement("objectgroup");
        } else {
            w.startElement("layer");
        }

        w.writeAttribute("name", l.getName());
        if (bounds.width != 0) {
            w.writeAttribute("width", bounds.width);
        }
        if (bounds.height != 0) {
            w.writeAttribute("height", bounds.height);
        }
        if (bounds.x != 0) {
            w.writeAttribute("x", bounds.x);
        }
        if (bounds.y != 0) {
            w.writeAttribute("y", bounds.y);
        }

        if (!l.isVisible()) {
            w.writeAttribute("visible", "0");
        }
        if (l.getOpacity() < 1.0f) {
            w.writeAttribute("opacity", l.getOpacity());
        }

        writeProperties(l.getProperties(), w);

        if (l instanceof ObjectGroup) {
            writeObjectGroup((ObjectGroup) l, w, wp);
        } else if (l instanceof TileLayer) {
            final TileLayer tl = (TileLayer) l;
            w.startElement("data");
            if (ENCODE_LAYER_DATA) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                OutputStream out;

                w.writeAttribute("encoding", "base64");

                DeflaterOutputStream dos;
                if (COMPRESS_LAYER_DATA) {
                    if (Settings.LAYER_COMPRESSION_METHOD_ZLIB.equalsIgnoreCase(settings.layerCompressionMethod)) {
                        dos = new DeflaterOutputStream(baos);
                    } else if (Settings.LAYER_COMPRESSION_METHOD_GZIP.equalsIgnoreCase(settings.layerCompressionMethod)) {
                        dos = new GZIPOutputStream(baos);
                    } else {
                        throw new IOException("Unrecognized compression method \"" + settings.layerCompressionMethod + "\" for map layer " + l.getName());
                    }
                    out = dos;
                    w.writeAttribute("compression", settings.layerCompressionMethod);
                } else {
                    out = baos;
                }

                for (int y = 0; y < l.getHeight(); y++) {
                    for (int x = 0; x < l.getWidth(); x++) {
                        Tile tile = tl.getTileAt(x + bounds.x,
                                y + bounds.y);
                        int gid = 0;

                        if (tile != null) {
                            gid = getGid(tile);
                        }

                        out.write(gid & LAST_BYTE);
                        out.write(gid >> 8 & LAST_BYTE);
                        out.write(gid >> 16 & LAST_BYTE);
                        out.write(gid >> 24 & LAST_BYTE);
                    }
                }

                if (COMPRESS_LAYER_DATA && dos != null) {
                    dos.finish();
                }

                w.writeCDATA(Base64.encodeToString(baos.toByteArray(), false));
            } else {
                for (int y = 0; y < l.getHeight(); y++) {
                    for (int x = 0; x < l.getWidth(); x++) {
                        Tile tile = tl.getTileAt(x + bounds.x, y + bounds.y);
                        int gid = 0;

                        if (tile != null) {
                            gid = getGid(tile);
                        }

                        w.startElement("tile");
                        w.writeAttribute("gid", gid);
                        w.endElement();
                    }
                }
            }
            w.endElement();

            boolean tilePropertiesElementStarted = false;

            for (int y = 0; y < l.getHeight(); y++) {
                for (int x = 0; x < l.getWidth(); x++) {
                    Properties tip = tl.getTileInstancePropertiesAt(x, y);

                    if (tip != null && !tip.isEmpty()) {
                        if (!tilePropertiesElementStarted) {
                            w.startElement("tileproperties");
                            tilePropertiesElementStarted = true;
                        }
                        w.startElement("tile");

                        w.writeAttribute("x", x);
                        w.writeAttribute("y", y);

                        writeProperties(tip, w);

                        w.endElement();
                    }
                }
            }

            if (tilePropertiesElementStarted) {
                w.endElement();
            }
        }
        w.endElement();
    }

    /**
     * Used to write tile elements for tilesets not based on a tileset image.
     *
     * @param tile the tile instance that should be written
     * @param w the writer to write to
     * @throws IOException when an io error occurs
     */
    private void writeTile(Tile tile, XMLWriter w, String wp) throws IOException {
        w.startElement("tile");
        w.writeAttribute("id", tile.getId());

        if (!tile.getProperties().isEmpty()) {
            writeProperties(tile.getProperties(), w);
        }

        if (tile.getSource() != null) {
            writeImage(tile, w, wp);
        }

        if (tile instanceof AnimatedTile) {
            writeAnimation(((AnimatedTile) tile).getSprite(), w);
        }

        w.endElement();
    }

    private void writeImage(Tile t, XMLWriter w, String wp) throws IOException {
        w.startElement("image");
        w.writeAttribute("width", t.getWidth());
        w.writeAttribute("height", t.getHeight());
        w.writeAttribute("source", getRelativePath(wp, t.getSource()));
        w.endElement();
    }

    private void writeAnimation(Sprite s, XMLWriter w) throws IOException {
        w.startElement("animation");
        for (int k = 0; k < s.getTotalKeys(); k++) {
            Sprite.KeyFrame key = s.getKey(k);
            w.startElement("keyframe");
            w.writeAttribute("name", key.getName());
            for (int it = 0; it < key.getTotalFrames(); it++) {
                Tile stile = key.getFrame(it);
                w.startElement("tile");
                w.writeAttribute("gid", getGid(stile));
                w.endElement();
            }
            w.endElement();
        }
        w.endElement();
    }

    private static void writeMapObject(MapObject mapObject, XMLWriter w, String wp)
            throws IOException {
        w.startElement("object");
        w.writeAttribute("name", mapObject.getName());

        if (mapObject.getType().length() != 0) {
            w.writeAttribute("type", mapObject.getType());
        }

        w.writeAttribute("x", mapObject.getX());
        w.writeAttribute("y", mapObject.getY());

        if (mapObject.getWidth() != 0) {
            w.writeAttribute("width", mapObject.getWidth());
        }
        if (mapObject.getHeight() != 0) {
            w.writeAttribute("height", mapObject.getHeight());
        }

        writeProperties(mapObject.getProperties(), w);

        if (mapObject.getImageSource().length() > 0) {
            w.startElement("image");
            w.writeAttribute("source",
                    getRelativePath(wp, mapObject.getImageSource()));
            w.endElement();
        }

        w.endElement();
    }

    /**
     * Returns the relative path from one file to the other. The function
     * expects absolute paths, relative paths will be converted to absolute
     * using the working directory.
     *
     * @param from the path of the origin file
     * @param to the path of the destination file
     * @return the relative path from origin to destination
     */
    public static String getRelativePath(String from, String to) {
        if (!(new File(to)).isAbsolute()) {
            return to;
        }

        // Make the two paths absolute and unique
        try {
            from = new File(from).getCanonicalPath();
            to = new File(to).getCanonicalPath();
        } catch (IOException e) {
            // todo: log this
        }

        File fromFile = new File(from);
        File toFile = new File(to);
        List<String> fromParents = new ArrayList<String >();
        List<String> toParents = new ArrayList<String>();

        // Iterate to find both parent lists
        while (fromFile != null) {
            fromParents.add(0, fromFile.getName());
            fromFile = fromFile.getParentFile();
        }
        while (toFile != null) {
            toParents.add(0, toFile.getName());
            toFile = toFile.getParentFile();
        }

        // Iterate while parents are the same
        int shared = 0;
        int maxShared = Math.min(fromParents.size(), toParents.size());
        for (shared = 0; shared < maxShared; shared++) {
            String fromParent = fromParents.get(shared);
            String toParent = toParents.get(shared);
            if (!fromParent.equals(toParent)) {
                break;
            }
        }

        // Append .. for each remaining parent in fromParents
        StringBuilder relPathBuf = new StringBuilder();
        for (int i = shared; i < fromParents.size() - 1; i++) {
            relPathBuf.append("..").append(File.separator);
        }

        // Add the remaining part in toParents
        for (int i = shared; i < toParents.size() - 1; i++) {
            relPathBuf.append(toParents.get(i)).append(File.separator);
        }
        relPathBuf.append(new File(to).getName());
        String relPath = relPathBuf.toString();

        // Turn around the slashes when path is relative
        try {
            String absPath = new File(relPath).getCanonicalPath();

            if (!absPath.equals(relPath)) {
                // Path is not absolute, turn slashes around
                // Assumes: \ does not occur in file names
                relPath = relPath.replace('\\', '/');
            }
        } catch (IOException e) {
        }

        return relPath;
    }

    /**
     * <p>accept.</p>
     *
     * @param pathName a {@link File} object.
     * @return a boolean.
     */
    public boolean accept(File pathName) {
        try {
            String path = pathName.getCanonicalPath();
            if (path.endsWith(".tmx") || path.endsWith(".tsx") || path.endsWith(".tmx.gz")) {
                return true;
            }
        } catch (IOException e) {
        }
        return false;
    }

    /**
     * Returns the global tile id of the given tile.
     *
     * @return global tile id of the given tile
     */
    private int getGid(Tile tile) {
        TileSet tileset = tile.getTileSet();
        if (tileset != null) {
            return tile.getId() + getFirstGidForTileset(tileset);
        }
        return tile.getId();
    }

    private void setFirstGidForTileset(TileSet tileset, int firstGid) {
        firstGidPerTileset.put(tileset, firstGid);
    }

    private int getFirstGidForTileset(TileSet tileset) {
        if (firstGidPerTileset == null) return 1;
        return firstGidPerTileset.get(tileset);
    }
}
