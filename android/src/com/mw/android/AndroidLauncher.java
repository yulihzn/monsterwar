package com.mw.android;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mw.game.MainGame;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		try {
			AssetCopyer assetCopyer = new AssetCopyer(this);
			assetCopyer.copy();
		} catch (IOException e) {
			e.printStackTrace();
		}
		MainGame mainGame = new MainGame();
		MainGame.androidDir = getApplicationContext().getExternalFilesDir("")+"/";
		initialize(mainGame, config);
	}
	private String[]packNames = {"world","floor","block","decorate","shadow"};
	private void copyBigDataToSD(String strOutFileName) throws IOException
	{
		InputStream myInput;
		OutputStream myOutput = new FileOutputStream(strOutFileName);
		myInput = this.getAssets().open("tiles.zip");
		byte[] buffer = new byte[1024];
		int length = myInput.read(buffer);
		while(length > 0)
		{
			myOutput.write(buffer, 0, length);
			length = myInput.read(buffer);
		}

		myOutput.flush();
		myInput.close();
		myOutput.close();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void finish() {
		super.finish();
		System.exit(0);//强制退出
	}
}
