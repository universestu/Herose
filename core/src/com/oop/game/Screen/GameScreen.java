package com.oop.game.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.oop.game.Herose;
import com.oop.game.Scenes.Hud;
import com.oop.game.Sprite.Hero;

/**
 * Created by brentaureli on 8/14/15.
 */
public class GameScreen implements Screen{
    private final TextureRegion HeroStand;
    //Reference to our Game, used to set Screens
    private Herose game;
    private TextureAtlas atlas;
    public static boolean alreadyDestroyed = false;

    //basic playscreen variables
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    //Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;


    //sprites
    private Hero player;

    private Music music;

    //private Array<Item> items;
   // private LinkedBlockingQueue<ItemDef> itemsToSpawn;

    Texture bucketImage;
    Rectangle bucket;
    public GameScreen(Herose game){
        atlas = new TextureAtlas("sprite.txt");

        this.game = game;
        //create cam used to follow mario through cam world
        gamecam = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen size

        float w = 800;
        float h = 640;


        gamecam.setToOrtho(false,w,h);
        gamecam.update();
        //create our game HUD for scores/timers/level info
        hud = new Hud(game.batch);

        //Load our map and setup our map renderer
        maploader = new TmxMapLoader();
        map = maploader.load("untitled.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        //initially set our gamcam to be centered correctly at the start of of map
        //gamecam.position.set(gamePort.getWorldWidth()/2 , gamePort.getWorldHeight() , 0);

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, -10), true);
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();

        //creator = new B2WorldCreator(this);

        //create mario in our game world
        player = new Hero(this);

        //world.setContactListener(new WorldContactListener());

        //music = MarioBros.manager.get("audio/music/mario_music.ogg", Music.class);
        //music.setLooping(true);
        //music.setVolume(0.3f);
        //music.play();

        //items = new Array<Item>();
       // itemsToSpawn = new LinkedBlockingQueue<ItemDef>();

        HeroStand = new TextureRegion(getAtlas().findRegion("Narutobig"));
        bucketImage = new Texture(Gdx.files.internal("Narutobig.gif"));
        bucket = new Rectangle();
        bucket.x = 800 / 2 - 32 / 2; // center the bucket horizontally
        bucket.y = 32;
        bucket.width = 16;
        bucket.height = 16;


    }
/*
    public void spawnItem(ItemDef idef){
       itemsToSpawn.add(idef);
    }


   public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();
            if(idef.type == Mushroom.class){
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
        }
    }

*/
    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {


    }

    public void handleInput(float dt){
        //control our player using immediate impulses

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            bucket.x -= 100 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            bucket.x += 100 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            bucket.y += 100 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            bucket.y -= 100 * Gdx.graphics.getDeltaTime();

        // เช็คไม่ให้ถังน้ำมันล้นหน้าจอ
        if (bucket.x < 0)
            bucket.x = 0;
        if (bucket.x > 800 - 64)
            bucket.x = 800 - 64;
        if (bucket.y < 0)
            bucket.y = 0;
        if (bucket.y > 480 - 64)
            bucket.y = 480 - 64;


    }

    public void update(float dt){
        //handle user input first
        handleInput(dt);

       // handleSpawningItems();

        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);

        player.update(dt);

        hud.update(dt);
        gamecam.position.x = player.b2body.getPosition().x;
        //gamecam.position.y = player.b2body.getPosition().y;
        //attach our gamecam to our players.x coordinate
       /* if(player.currentState != Mario.State.DEAD) {
            gamecam.position.x = player.b2body.getPosition().x;
        }
    */
        //update our gamecam with correct coordinates after changes
        gamecam.update();
        //tell our renderer to draw only what our camera can see in our game world.
        renderer.setView(gamecam);

    }


    @Override
    public void render(float delta) {
        //separate our update logic from render
       // update(delta);

        //Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleInput(delta);
        //render our game map
        gamecam.update();
        renderer.setView(gamecam);
        renderer.render();
        //renderer our Box2DDebugLines
        //b2dr.render(world, gamecam.combined);

       // game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();

        game.batch.draw(HeroStand, bucket.x, bucket.y,32,32);
       /* player.draw(game.batch);
        for (Enemy enemy : creator.getEnemies())
            enemy.draw(game.batch);
        for (Item item : items)
            item.draw(game.batch);*/
        game.batch.end();

        //Set our batch to now draw what the Hud camera sees.
      //  game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if(gameOver()){
            //game.setScreen(new GameOverScreen(game));
            dispose();
        }

    }

    public boolean gameOver(){
      //  if(player.currentState == Mario.State.DEAD && player.getStateTimer() > 3){
        //    return true;
       // }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        //updated our game viewport
        //gamePort.update(width,height);

    }

    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        //dispose of all our opened resources
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

    public Hud getHud(){ return hud; }
}
