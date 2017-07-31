package com.mw.avatar.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Timer;
import com.mw.avatar.model.PixelAnimModel;
import com.mw.avatar.model.PixelIndexModel;
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

    private void upDateAvatar() {
        fill(as.all, new Color(0, 0, 0, 0));
        updatePixmap();
        texture.draw(pixmap, 0, 0);
    }
    private void updatePixmap(){
        if(as.list.size <=0){
            return;
        }
        Array<PixelIndexModel> arr = as.list.get(0).getList().get(index).getList();
        for (int i = 0; i < arr.size; i++) {
            int x = arr.get(i).getX();
            int y = arr.get(i).getY();
            int index = arr.get(i).getIndex();
            pixmap.setColor(as.getColor(index));
            pixmap.drawPixel(x, y);
        }
        index++;
        if (index >= as.list.get(0).getList().size) {
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

    public static final int LOOP = -2;//循环
    private float frameDuration = 0.1f;//帧间隔
    private Timer timer = new Timer();
    private Timer.Task timerTask = new Timer.Task() {
        @Override
        public void run() {
            upDateAvatar();
        }
    };

    private int getAnimCount() {
        int count = as.list.get(0).getList().size;
        Gdx.app.log("count:", "" + count);
        return count;
    }

    public void play(float delaySeconds, float intervalSeconds, int repeatCount) {
        synchronized (timerTask) {
            if (!timerTask.isScheduled()) {
                index = 0;
                timer.scheduleTask(timerTask, delaySeconds, intervalSeconds, repeatCount);
            }
        }
    }

    public void play() {
        play(0, frameDuration, getAnimCount());
    }

    public void playLoop() {
        play(0, frameDuration, LOOP);
    }
    public void stop(){
        timer.clear();
        index = 0;
        upDateAvatar();
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

        public Array<PixelAnimModel> list = new Array<PixelAnimModel>();
        private Map<Integer, Integer> colors = new HashMap<Integer, Integer>();

        public AvatarSettings() {
            init();
        }

        public void init() {
            addAnimFile("anim/base_anim_face.json");
            //给每一个点设置颜色
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
        public void addAnimFile(String internalFile){
            FileHandle fileHandle = Gdx.files.internal(internalFile);
            String text = fileHandle.readString();
            Json json = new Json();
            PixelAnimModel model = json.fromJson(PixelAnimModel.class, text);
            list.add(model);
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
