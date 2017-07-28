package com.mw.avatar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.mw.avatar.model.PixelAnimModel;
import com.mw.avatar.model.PixelIndexModel;
import com.mw.avatar.model.PixelInfoModel;
import com.mw.ui.FloatRectPixmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by BanditCat on 2016/9/21.
 * 读取json
 */
public class Avatar {
    private int width = 32;
    private int height = 32;
    private AvatarSettings as;
    private FloatRectPixmap pixmap;
    private Texture texture;
    private int index = 0;

    public Avatar() {
        as = new AvatarSettings();
        pixmap = new FloatRectPixmap(width, height, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        texture = new Texture(width, height, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        upDateAvatar();
    }

    public void upDateAvatar() {
        fill(as.all, new Color(0, 0, 0, 0));
        pixmap.setColor(as.color_skin);
        Array<PixelIndexModel> arr = as.model.getList().get(index).getList();
        for (int i = 0; i < arr.size; i++) {
            int x = arr.get(i).getX();
            int y = arr.get(i).getY();
            int index = arr.get(i).getIndex();
            pixmap.setColor(as.getColor(index));
            pixmap.drawPixel(x, y);
        }
        texture.draw(pixmap, 0, 0);
        index++;
        if (index >= as.model.getList().size) {
            index = 0;
        }
    }

    public void createRegions() {
    }

    public void newAvatar() {
        as.init();
    }

    public void dispose() {
        pixmap.dispose();
        texture.dispose();
    }

    public Texture getTexture() {
        return texture;
    }

    public FloatRectPixmap getPixmap() {
        return pixmap;
    }


    private void fill(Rectangle rectangle, Color color) {
        pixmap.setColor(color);
        pixmap.fillRectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    private void fill(Rectangle rectangle) {
        pixmap.fillRectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    private void pixel(int x, int y) {
        pixmap.drawPixel(x, y);
    }

    private void pixel(int x, int y, Color color) {
        pixmap.drawPixel(x, y, color.toIntBits());
    }

    private class AvatarSettings {

        public int color_skin = 0xffffccff;
        public int color_eyes = 0x000000ff;
        public int color_lips = 0xff6666ff;
        public int color_armr = 0xffffccff;
        public int color_arml = 0xffffccff;
        public int color_legl = 0xffffccff;
        public int color_legr = 0xffffccff;
        public int color_body = 0xffffccff;

        public Rectangle all = new Rectangle(0, 0, width, height);


        public PixelAnimModel model;
        private Map<Integer, Integer> colors = new HashMap<Integer, Integer>();

        public AvatarSettings() {
            init();
        }

        public void init() {
            FileHandle fileHandle = Gdx.files.internal("anim/base_anim_face.json");
            String text = fileHandle.readString();
            Json json = new Json();
            model = json.fromJson(PixelAnimModel.class, text);
            for (int i = 1; i < 71; i++) {
                //head
                if (i >= 1 && i <= 25) {
                    colors.put(i, color_skin);
                }
                //right arm
                if (i >= 26 && i <= 30) {
                    colors.put(i, color_armr);
                }
                //left arm
                if (i >= 36 && i <= 40) {
                    colors.put(i, color_arml);
                }
                //body
                if (i >= 31 && i <= 35) {
                    colors.put(i, color_body);
                }
                if (i >= 41 && i <= 60) {
                    colors.put(i, color_body);
                }
                if (i >= 61) {
                    //right leg
                    if (i % 2 == 1) {
                        colors.put(i, color_legr);
                    }
                    //left leg
                    else {
                        colors.put(i, color_legl);
                    }
                }
                //eyes
                if (i == 12 || i == 14) {
                    colors.put(i, color_eyes);
                }
                //mouth
                if (i == 23) {
                    colors.put(i, color_lips);
                }
            }
        }

        public int getColor(int index) {
            Integer i = colors.get(index);
            if (i == null) {
                return color_skin;
            }
            return i;
        }

    }
}
