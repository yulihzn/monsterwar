package com.mw.avatar;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
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
        drawHead();
        drawBody();
        drawLeg();
        drawArm();
        drawEyes();
        drawHair();
        texture.draw(pixmap, 0, 0);
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

    private void drawArm() {
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

    private void drawLeg() {
        pixmap.setColor(as.color_skin);
        fill(as.leg_left);
        fill(as.leg_right);
    }

    private void drawBody() {//5x5
        pixmap.setColor(as.color_skin);
        fill(as.body);
    }

    private void drawHead() {//5x5
        pixmap.setColor(as.color_skin);
        fill(as.head);
    }

    private void drawEyes() {
        pixmap.setColor(as.color_black);
        fill(as.eye_left);
        fill(as.eye_right);
    }

    private void drawHair() {
        pixmap.setColor(as.color_red);
        as.hair_left.height = MathUtils.random(7);
        as.hair_right.height = MathUtils.random(7);
        as.hair_center.width = MathUtils.random(5);
        as.hair_center.height = MathUtils.random(4);
        fill(as.hair_left);
        fill(as.hair_right);
        fill(as.hair_center);


    }

    class AvatarSettings {
        public Color color_black = new Color(0, 0, 0, 1f);
        public Color color_gold = new Color(254, 242, 0, 1f);
        public Color color_skin = new Color(255, 232, 216, 1f);
        public Color color_gray = new Color(126, 126, 126, 1f);
        public Color color_red = new Color(255, 0, 0, 1f);
        public Rectangle body = new Rectangle(13, 14, 5, 5);
        public Rectangle head = new Rectangle(13, 9, 5, 5);
        public Rectangle leg_left = new Rectangle(14, 19, 1, 5);
        public Rectangle leg_right = new Rectangle(16, 19, 1, 5);
        public Rectangle arm_left = new Rectangle(12, 14, 1, 5);
        public Rectangle arm_right = new Rectangle(18, 14, 1, 5);
        public Rectangle eye_left = new Rectangle(head.x + 1, head.y + 2, 1, 1);
        public Rectangle eye_right = new Rectangle(head.x + 3, head.y + 2, 1, 1);
        public Rectangle hair_left = new Rectangle(head.x - 1, head.y - 2, 1, 7);
        public Rectangle hair_right = new Rectangle(head.x+5, head.y - 2, 1, 7);
        public Rectangle hair_center = new Rectangle(head.x, head.y - 2, 5, 4);
        public Rectangle all = new Rectangle(0,0,32,32);
    }
}
