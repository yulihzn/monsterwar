package com.mw.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.mw.actor.TiledMapActor;
import com.mw.game.MainGame;
import com.mw.map.MapEditor;
import com.mw.map.TmxWorldMap;
import com.mw.stage.CharacterStage;

/**
 * Created by BanditCat on 2016/9/7.
 */
public class WorldMapTable extends Table {
    private FreeTypeFontGenerator generator;
    private LazyBitmapFont bitmapFont;
    private Texture texture;
    private ScrollPane scrollPane;
    private Color[]colors = {Color.LIGHT_GRAY,Color.YELLOW,Color.RED,Color.GREEN};

    private TiledMapRenderer renderer;
    private OrthographicCamera camera;



    public WorldMapTable(OrthographicCamera camera) {
        this.camera = camera;
        camera.position.set(128, 128, 0);
        init();
    }

    private void init() {
        int w = MainGame.worldWidth;
        int h = MainGame.worldHeight;
        generator = new FreeTypeFontGenerator(Gdx.files.internal("data/font.ttf"));
        bitmapFont = new LazyBitmapFont(generator,8);

        Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        texture = new Texture(w, h, Pixmap.Format.RGBA8888);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        pixmap.setColor(new Color(0,0,0,1f));
        pixmap.fillRectangle(0,0,w,h);
        texture.draw(pixmap, 0, 0);
        TextureRegion textureRegion = new TextureRegion(texture,w,h);
        setBackground(new TextureRegionDrawable(textureRegion));
        setPosition(0,0);
        setWidth(w);
        setHeight(h);
        Table table = new Table();
        scrollPane = new ScrollPane(table);
        table.pad(5).defaults().expandX().space(5);
        add(scrollPane).expand().fill().colspan(5);
        row().space(5).padBottom(5);
//        MapEditor mapEditor = new MapEditor();
//        mapEditor.Create();
//        WorldMapModel worldMapModel = GameFileHelper.getInstance().getWorldMap(GameFileHelper.DEFAULT_PROFILE);
//        if(worldMapModel != null){
//            MapEditor mapEditor = new MapEditor();
//            mapEditor.setArr(worldMapModel.getArr());
//            String s = mapEditor.getArrayString();
//            Label label = new Label(s,new Label.LabelStyle(bitmapFont, Color.WHITE));
//            table.add(label).expandX().fillX();
//        }
//        worldMap = new WorldMap();
//        //获取渲染
//        renderer = new OrthogonalTiledMapRenderer(worldMap,1/16f);
//        OrthographicCamera camera = new OrthographicCamera(800,600);
//        renderer.setView(new OrthographicCamera(800,600));
//        camera.translate(0,0);
        //获取渲染
        final TmxWorldMap tmxWorldMap = new TmxWorldMap(MapEditor.WIDTH,MapEditor.HEIGHT);
        TiledMap map = tmxWorldMap.getTileMapReload();
        map.getLayers().get(TmxWorldMap.LAYER_SHADOW).setVisible(false);
//        tmxWorldMap.saveTiledMap();
        renderer = new OrthogonalTiledMapRenderer(map, 1f/32f);
        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                tmxWorldMap.changeTile(TmxWorldMap.LAYER_SHADOW,21,0,0);
                Gdx.app.log("worldMapTable","x="+x+",y="+y);
            }
        });

    }
    public class TiledMapClickListener extends ClickListener {

        private TiledMapActor actor;

        public TiledMapClickListener(TiledMapActor actor) {
            this.actor = actor;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            System.out.println(actor.getX()+","+actor.getY() +"value = "+actor.getCell().getTile().getId()+ " has been clicked.");
        }
    }
    public void dispose(){
        texture.dispose();
        bitmapFont.dispose();
        generator.dispose();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setProjectionMatrix(camera.combined);
        super.draw(batch, parentAlpha);
        renderer.setView(camera);
        renderer.render();
//        renderer.render(new int[]{0,1,2});
    }
    public static final long roundSecond = 10000000;
    private long roundTime = TimeUtils.nanoTime();
    @Override
    public void act(float delta) {
        super.act(delta);

        if (TimeUtils.nanoTime() - roundTime >= roundSecond) {
            roundTime = TimeUtils.nanoTime();
            if(Gdx.input.isKeyPressed(Input.Keys.UP)){
                camera.translate(0,1);
            }else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                camera.translate(0,-1);
            }else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                camera.translate(-1,0);
            }else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                camera.translate(1,0);
            }else if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                camera.position.set(128,128,0);
            }
        }
        camera.zoom = MathUtils.clamp(camera.zoom,0.1f, 18f);
        camera.position.x = MathUtils.clamp(camera.position.x,0,256);
        camera.position.y = MathUtils.clamp(camera.position.y,0,256);
        float w = MainGame.worldWidth;
        float h = MainGame.worldHeight;
        camera.viewportWidth = CharacterStage.camSize;
        camera.viewportHeight = CharacterStage.camSize*(h/w);
        camera.update();
    }

}
