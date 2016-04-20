package com.siftthemout.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by PC on 19-Apr-16.
 */
public class GameScreen implements Screen, GestureDetector.GestureListener {
    private Game game;
    private static final float dragSpeedDecreaser = 1f;

    private SpriteBatch batch;
    private List<Dot> dots;
    private List<Dot> bins;
    private Texture redDotImage;
    private Texture blueDotImage;
    private Texture backGround;
    private OrthographicCamera camera;
    private Sprite sprite;
    private BitmapFont font;
    private static Random randomGenerator = new Random();
    private Dot redBin;
    private Dot blueBin;
    private int score = 0;
    private Sound droppedInTheBin;

    //just for test purposes
    private String testCoordinatesTap = "tap";
    private String testCoordinatesPan = "pan";

    public GameScreen(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.backGround = new Texture(Gdx.files.internal("background.png"));
        this.redDotImage = new Texture(Gdx.files.internal("reddot.png"));
        this.blueDotImage = new Texture(Gdx.files.internal("bluedot.png"));
        this.sprite = new Sprite(this.backGround);
        this.font = new BitmapFont();
        this.font.setColor(Color.BLACK);

        Gdx.input.setInputProcessor(new GestureDetector(this));
        this.camera.translate(this.backGround.getWidth() / 2, this.backGround.getHeight() / 2);
        this.camera.update();
        this.droppedInTheBin = Gdx.audio.newSound(Gdx.files.internal("sounds/DroppedInTheBin.wav"));

        populateDots();
    }

    @Override
    public void dispose() {
        this.batch.dispose();
        this.redDotImage.dispose();
        this.blueDotImage.dispose();
        this.font.dispose();
        this.backGround.dispose();
        this.blueBin.getPicture().dispose();
        this.redBin.getPicture().dispose();
        this.droppedInTheBin.dispose();
        this.game.dispose();
    }

    private void populateDots() {
        int numberOfDots = randomGenerator.nextInt(10) + 1;

        this.blueBin = new Dot((int)(this.camera.position.x - Gdx.graphics.getWidth() / 2 + 30),
                (int)(this.camera.position.y + Gdx.graphics.getHeight()/2 - 100),
                Color.BLUE,
                new Texture(Gdx.files.internal("bluebin.png")));

        this.redBin = new Dot((int)(this.camera.position.x + Gdx.graphics.getWidth() / 2 - 100),
                (int)(this.camera.position.y - Gdx.graphics.getHeight()/2),
                Color.RED,
                new Texture(Gdx.files.internal("redbin.png")));

        this.bins = new ArrayList<Dot>(Arrays.asList(blueBin, redBin));
        this.dots = new ArrayList<Dot>();

        for (int i = 0; i < numberOfDots; i++) {
            Texture dotImage = i % 2 == 0 ? this.redDotImage : this.blueDotImage;
            Color dotColor = i % 2 == 0 ? Color.RED : Color.BLUE;

            // giving the dot such coordinates that it appears on the screen on start, so
            // that we don't have to look for it all around the map - just for easy testing.
            this.dots.add(new Dot(randomGenerator.nextInt(Gdx.graphics.getWidth())
                    + this.backGround.getWidth() / 2 - Gdx.graphics.getWidth() / 2,
                    randomGenerator.nextInt(Gdx.graphics.getHeight())
                            + this.backGround.getHeight() / 2 - Gdx.graphics.getHeight() / 2,
                    dotColor,
                    dotImage ));
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        sprite.draw(batch);
        batch.draw(blueBin.getPicture(), blueBin.getX(), blueBin.getY());
        batch.draw(redBin.getPicture(), redBin.getX(), redBin.getY());

        for (Dot dot : dots) {
            batch.draw(dot.getPicture(), dot.getX(), dot.getY());
        }

        font.draw(batch, "Score: " + this.score, this.camera.position.x, this.camera.position.y + Gdx.graphics.getHeight() / 2 - 20);

        // just for tests:
        font.draw(batch, "camera: " + camera.position.x + " - " + camera.position.y + "\n"
                        + "tap: " + testCoordinatesTap + "\n"
                        + "pan: " + testCoordinatesPan + "\n"
                        + "dots: " + this.dots.size(),
                camera.position.x + Gdx.graphics.getWidth() /2 - 150, camera.position.y + Gdx.graphics.getHeight()/2 - 20);
        batch.end();

        if (dots.size() == 0) {
            this.dispose();
            game.setScreen(new GameScreen(this.game));
        }
    }

    @Override
    public void show() {

    }



    @Override
    public void resize(int width, int height) {

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
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        // just for tests:
        this.testCoordinatesTap = x + " - " + y;
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        boolean foundDotInRange = false;

        float adjustedXWithCam = x - (Gdx.graphics.getWidth()/2 - camera.position.x);
        float adjustedYWithCam = (Gdx.graphics.getHeight() - y)
                + (camera.position.y - Gdx.graphics.getHeight()/2);

        for (int i = 0; i < dots.size(); i++) {
            boolean isInRange = Math.abs(adjustedXWithCam - dots.get(i).getX()) < 150
                    && Math.abs(adjustedYWithCam - dots.get(i).getY()) < 150;

            if (isInRange) {
                foundDotInRange = true;
                dots.get(i).setX((int) (dots.get(i).getX() + deltaX / dragSpeedDecreaser));
                dots.get(i).setY((int) (dots.get(i).getY() - deltaY / dragSpeedDecreaser));

                boolean isInBin = checkIfItIsInBin(dots.get(i));
                if (isInBin) {
                    this.droppedInTheBin.play();
                    dots.remove(i);
                    i--;
                }
            }
        }



        boolean isOnTheEdge = camera.position.x - deltaX/2< Gdx.graphics.getWidth()/2
                || camera.position.x - deltaX/2 > this.backGround.getWidth() - Gdx.graphics.getWidth() / 2
                || camera.position.y + deltaY / 2 < Gdx.graphics.getHeight() / 2
                || camera.position.y + deltaY / 2> this.backGround.getHeight() - Gdx.graphics.getHeight() / 2;

        if (!foundDotInRange && !isOnTheEdge) {
            camera.translate(-deltaX / 2, deltaY / 2);
            camera.update();
        }

        // just for tests:
        testCoordinatesPan = adjustedXWithCam + " - " + adjustedYWithCam;

        return true;
    }

    private boolean checkIfItIsInBin(Dot dot) {
        for (Dot bin : bins) {
            if (bin.getCircle().overlaps(dot.getCircle())) {
                if (bin.getColor() == dot.getColor()) {
                    score += 10;
                } else {
                    score -= 10;
                }

                return true;
            }
        }
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
