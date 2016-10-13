package com.mw.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.mw.actor.TiledMapActor;
import com.mw.game.MainGame;
import com.mw.logic.Logic;
import com.mw.map.DungeonMap;
import com.mw.map.MapEditor;
import com.mw.map.WorldMap;
import com.mw.model.LogModel;
import com.mw.model.WorldMapModel;
import com.mw.profiles.GameFileHelper;
import com.mw.stage.MapStage;

/**
 * Created by BanditCat on 2016/9/7.
 */
public class WorldMapTable extends Table {
    private FreeTypeFontGenerator generator;
    private LazyBitmapFont bitmapFont;
    private Texture texture;
    private ScrollPane scrollPane;
    private Color[]colors = {Color.LIGHT_GRAY,Color.YELLOW,Color.RED,Color.GREEN};

    private WorldMap worldMap;
    private TiledMapRenderer renderer;
    private OrthographicCamera camera;


    public WorldMapTable() {
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
        WorldMapModel worldMapModel = GameFileHelper.getInstance().getWorldMap(GameFileHelper.DEFAULT_PROFILE);
        if(worldMapModel != null){
            MapEditor mapEditor = new MapEditor();
            mapEditor.setArr(worldMapModel.getArr());
            String s = mapEditor.getArrayString();
            Label label = new Label(s,new Label.LabelStyle(bitmapFont, Color.WHITE));
            table.add(label).expandX().fillX();
        }
//        worldMap = new WorldMap();
//        //获取渲染
//        renderer = new OrthogonalTiledMapRenderer(worldMap,1/16f);
//        OrthographicCamera camera = new OrthographicCamera(800,600);
//        renderer.setView(new OrthographicCamera(800,600));
//        camera.translate(0,0);
        //获取渲染
        TiledMap map = new TmxMapLoader().load("test.tmx");
//        renderer = new OrthogonalTiledMapRenderer(worldMap, 1f / 32f);
//        camera = new OrthographicCamera();
//        camera.setToOrtho(false, (w/h)*10, 10);
//        camera.update();
    }
    private void createActorsForLayer(TiledMapTileLayer tiledLayer) {
        for (int x = 0; x < tiledLayer.getWidth(); x++) {
            for (int y = 0; y < tiledLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = tiledLayer.getCell(x, y);
                TiledMapActor actor;
                if(cell != null){
                    actor = new TiledMapActor(worldMap, tiledLayer, cell,new GridPoint2(x,y));
                    addActor(actor);
                    actor.setZIndex(0);
                    actor.setBounds(x * tiledLayer.getTileWidth(), y * tiledLayer.getTileHeight(), tiledLayer.getTileWidth(),
                            tiledLayer.getTileHeight());
                    if(tiledLayer.getName().equals(DungeonMap.LAYER_SHADOW)){
                        actor.setZIndex(3);
                    }
                    EventListener eventListener = new TiledMapClickListener(actor);
                    actor.addListener(eventListener);
                }
            }
        }
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
        worldMap.dispose();

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
//        camera.update();
//        renderer.setView(camera);
////        renderer.render();
//        renderer.render(new int[]{0,1,2});
    }
}
