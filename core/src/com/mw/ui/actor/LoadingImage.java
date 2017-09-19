package com.mw.ui.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;

public class LoadingImage extends Image {

//	private float stateTime;
	
	public LoadingImage() {
		super();
	}
	public LoadingImage(Drawable drawable, Scaling scaling, int align) {
		super(drawable, scaling, align);
	}
	public LoadingImage(Drawable drawable, Scaling scaling) {
		super(drawable, scaling);
	}
	public LoadingImage(Drawable drawable) {
		super(drawable);
	}
	public LoadingImage(NinePatch patch) {
		super(patch);
	}
	public LoadingImage(Skin skin, String drawableName) {
		super(skin, drawableName);
	}
	public LoadingImage(Texture texture) {
		super(texture);
	}
	public LoadingImage(TextureRegion region) {
		super(region);
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		stateTime += Gdx.graphics.getDeltaTime(); 
		super.draw(batch, parentAlpha);
	}
	public void initLoadingAction(){
		this.setOrigin(getWidth()/2, getHeight()/2);
		this.scaleBy(2);
		//旋转90度
		RotateByAction rotateByAction1 = Actions.rotateBy(90f,0.2f);
		//默认75%透明
		AlphaAction alphaAction0 = Actions.alpha(0.75f, 0f);
		//先透明再旋转
		SequenceAction firstAction = Actions.sequence(alphaAction0,rotateByAction1);
		//停顿
		RotateByAction rotateByAction2 = Actions.rotateBy(0f,0.2f);
		//变暗再复原
		AlphaAction alphaAction1 = Actions.alpha(0.2f, 0.075f);
		AlphaAction alphaAction2 = Actions.alpha(0.75f, 0.075f);
		SequenceAction sequencealphaAction = Actions.sequence(alphaAction1,alphaAction2);
		//同时执行变暗和停顿
		ParallelAction parallelAction = Actions.parallel(sequencealphaAction,rotateByAction2);
		//先旋转再停顿
		SequenceAction sequenceAction = Actions.sequence(firstAction,parallelAction);
		//重复
		RepeatAction repeatAction = Actions.repeat(RepeatAction.FOREVER, sequenceAction);
		this.addAction(repeatAction);
		
	}
	@Override
	public void clear() {
		super.clear();
	}

}
