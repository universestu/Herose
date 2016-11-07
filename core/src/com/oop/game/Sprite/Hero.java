package com.oop.game.Sprite;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.oop.game.Herose;
import com.oop.game.Screen.GameScreen;


/**
 * Created by brentaureli on 8/27/15.
 */
public class Hero extends Sprite {
    public enum State {  STANDING, RUNNING};
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;

    private TextureRegion HeroStand;
    private TextureRegion HeroRun;

    private float stateTimer;
    private boolean runningRight;

    private GameScreen screen;



    public Hero(GameScreen screen){
        //initialize default values
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        //get run animation frames and add them to marioRun Animation


        HeroRun =new TextureRegion(screen.getAtlas().findRegion("Narutobig"));



        //create texture region for mario standing
        HeroStand = new TextureRegion(screen.getAtlas().findRegion("Narutobig"));


        //define mario in Box2d
        defineHero();

        //set initial values for marios location, width and height. And initial frame as marioStand.
        setBounds(0, 0, 16 / Herose.PPM, 16 / Herose.PPM);
        setRegion(HeroStand);



    }

    public void update(float dt){



        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        setRegion(getFrame(dt));

    }

    public TextureRegion getFrame(float dt){
        //get marios current state. ie. jumping, running, standing...
        currentState = getState();

        TextureRegion region;

        //depending on the state, get corresponding animation keyFrame.
        switch(currentState){
            case RUNNING:
                region = HeroRun;
                break;
            case STANDING:
            default:
                region = HeroStand;
                break;
        }

        //if mario is running left and the texture isnt facing left... flip it.
      //  if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
      //      region.flip(true, false);
     //       runningRight = false;
       // }

        //if mario is running right and the texture isnt facing right... flip it.
//        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
   //         region.flip(true, false);
      //      runningRight = true;
     //   }

        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        //update previous state
        previousState = currentState;
        //return our final adjusted frame
        return region;

    }

    public State getState(){
        //Test to Box2D for velocity on the X and Y-Axis
        //if mario is going positive in Y-Axis he is jumping... or if he just jumped and is falling remain in jump state

        if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
            //if none of these return then he must be standing
        else
            return State.STANDING;
    }




    public void defineHero(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / Herose.PPM, 32 / Herose.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Herose.PPM);


        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / Herose.PPM, 6 / Herose.PPM), new Vector2(2 / Herose.PPM, 6 / Herose.PPM));


        b2body.createFixture(fdef).setUserData(this);
    }


    public void draw(Batch batch){
        super.draw(batch);

    }
}