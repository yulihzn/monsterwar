package tiled.util;

import com.badlogic.gdx.files.FileHandle;

/**
 * Created by BanditCat on 2016/12/13.
 */

public class TileTexture {
    private int width;
    private int height;
    public static final String[]packNames = {"world","floor","block","decorate","shadow"};
    public TileTexture() {
    }
    //TODO
    public TileTexture (String name) {
        //根据名字设置宽高
        for (int i = 0; i < packNames.length; i++) {
            if(name.equals(TileTexture.packNames[i])){
                switch (i){
                    case 0:width=256;height=288;break;
                    case 1:width=128;height=992;break;
                    case 2:width=1024;height=384;break;
                    case 3:width=128;height=32;break;
                    case 4:width=256;height=32;break;
                }
                break;
            }
        }
    }
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
