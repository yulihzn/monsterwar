package com.mw.avatar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mw.ui.FloatRectPixmap;

/**
 * Created by BanditCat on 2016/9/21.
 */
public class Avatar {
    private int width = 32;
    private int height = 32;
    private AvatarSettings as;
    private FloatRectPixmap pixmap;
    private Texture texture;
    private int direct = 0;
    private int[]indexs={0,1,2,3,4,5,6};
    private TextureRegion[] textureRegions = new TextureRegion[14];

    /**
     * 方案一：固定八个方向设置坐标点
     * 方案二：2个面生成7x7，转向每个像素都按列划分，总共2x7个图案
     */

    public Avatar() {
        as = new AvatarSettings();
        pixmap = new FloatRectPixmap(width, height, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        texture = new Texture(width, height, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        upDateAvatar();
    }

    public void upDateAvatar() {
        fill(as.all,new Color(0,0,0,0));
        drawHead(direct);
        drawBody(direct);
        drawLeg(direct);
        drawArm(direct);
        drawHair(direct);
        drawEyes(direct);
        texture.draw(pixmap, 0, 0);
        for (int i = 0; i < indexs.length; i++) {
            indexs[i] += 1;
            if(indexs[i]>=14){
                indexs[i]-=14;
            }
        }
    }
    public  void createRegions(){
    }
    public void newAvatar(){
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

    private void drawArm(int direct) {
        pixmap.setColor(as.color_skin);
        fill(as.arm_left);
        fill(as.arm_right);
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

    private void drawLeg(int direct) {
        pixmap.setColor(as.color_skin);
        fill(as.leg_left);
        fill(as.leg_right);
    }

    private void drawBody(int direct) {//5x5
        pixmap.setColor(as.color_skin);
        fill(as.body);
        for (int i = 1; i < indexs.length-1; i++) {
            pixmap.setColor(Color.GREEN);
            if(indexs[i]>6){
                pixmap.setColor(Color.OLIVE);
            }
            Rectangle r = as.bodys.get(indexs[i]);
            fill(new Rectangle(as.hair.x+i,r.y,r.width,r.height));
        }
    }

    private void drawHead(int direct) {//5x5
        pixmap.setColor(as.color_skin);
        fill(as.head);
    }

    private void drawEyes(int direct) {
        pixmap.setColor(Color.BROWN);
        for (int i = 1; i < indexs.length-1; i++) {
            Rectangle r = as.eyes.get(indexs[i]);
            fill(new Rectangle(as.hair.x+i,r.y,r.width,r.height));
        }
    }

    private void drawHair(int direct) {
        pixmap.setColor(as.color_gold);
        for (int i = 0; i < indexs.length; i++) {
            Rectangle r = as.hairs.get(indexs[i]);
            fill(new Rectangle(as.hair.x+i,r.y,r.width,r.height));
        }
    }

    class AvatarSettings {
        public Array<Rectangle> hairs = new Array<Rectangle>();
        public Array<Rectangle> eyes = new Array<Rectangle>();
        public Array<Rectangle> bodys = new Array<Rectangle>();
        public Color color_black = new Color(0, 0, 0, 1f);
        public Color color_gold = new Color(254, 242, 0, 1f);
        public Color color_skin = new Color(255, 232, 216, 1f);
        public Color color_gray = new Color(126, 126, 126, 1f);
        public Color color_red = new Color(255, 0, 0, 1f);
        public Rectangle head = new Rectangle(13, 9, 5, 5);
        public Rectangle body = new Rectangle(head.x, head.y + 5, 5, 5);
        public Rectangle leg_left = new Rectangle(14, 19, 1, 5);
        public Rectangle leg_right = new Rectangle(16, 19, 1, 5);
        public Rectangle arm_left = new Rectangle(12, 14, 1, 5);
        public Rectangle arm_right = new Rectangle(18, 14, 1, 5);
        public Rectangle eye = new Rectangle(head.x + 1, head.y + 2, 1, 1);
        public Rectangle hair = new Rectangle(head.x-1, head.y - 2, 7, 7);//正面7x7
        public Rectangle all = new Rectangle(0,0,32,32);

        public AvatarSettings() {
            init();
        }
        public void init(){
            initHair();
            initEyes();
            initBody();
        }
        private void initHair(){
            hairs.clear();
            for (int i = 0; i < 14; i++) {
                int offsetY = MathUtils.random(2);
                int length = MathUtils.random(7-offsetY);
                if(i>6){
                    length = MathUtils.random(4,7-offsetY);
                }
                hairs.add(new Rectangle(hair.x,(int)hair.y+offsetY,1,length));
            }
        }
        private void initEyes(){
            eyes.clear();
            for (int i = 0; i < 14; i++) {
                if(i==2){
                    eyes.add(new Rectangle(eye.x,eye.y,1,1));
                }else if(i==4){
                    eyes.add(new Rectangle(eye.x+2,eye.y,1,1));
                }else{
                    eyes.add(new Rectangle(0,0,0,0));
                }
            }
        }
        private void initBody(){
            bodys.clear();
            for (int i = 0; i < 14; i++) {
                if(i!=0||i!=6||i!=13){
                    int r = MathUtils.random(2);
                    bodys.add(new Rectangle(body.x,body.y+r,1,5-r));
                }else{
                    bodys.add(new Rectangle(0,0,0,0));
                }
            }
        }
    }
}
