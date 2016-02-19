package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;

public class MyGdxGame implements ApplicationListener {

    //Variables
    private int screenX;
    private int screenY;

    private boolean checkAccelerometer;

    private float accelX;
    private float accelY;
    private float accelZ;

    //Objectives
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Environment environment;
    private AssetManager assetManager;
    private ModelInstance compass;

	@Override
	public void create() {
        //Log all messages
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        //Check for Accelerometer
        boolean check = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
        if(check) {
            Gdx.app.debug("LibGDX", "CheckAccelerometer: " + check);
            checkAccelerometer = check;
        }
        else {
            Gdx.app.debug("LibGDX", "CheckAccelerometer: " + check);
            checkAccelerometer = check;
            //Force close application?
            //Gdx.app.exit();
        }

        screenX = Gdx.graphics.getWidth();
        screenY = Gdx.graphics.getHeight();

        modelBatch = new ModelBatch();

        accelX = 0f;
        accelY = 0f;
        accelZ = 0f;

        camera = new PerspectiveCamera(70, screenX, screenY);
        camera.position.set(0f, -3f, 3f);
        camera.lookAt(0, 0, 0);
        camera.near = 0.1f;
        camera.far = 300f;
        camera.update();

        assetManager = new AssetManager();
        assetManager.load("cube.g3db", Model.class);
        assetManager.finishLoading();

        Model model = assetManager.get("cube.g3db", Model.class);
        compass = new ModelInstance(model);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
        environment.add(new DirectionalLight().set(1f, 1f, 1f, -0.8f, 0.3f, -1f));
	}

	@Override
	public void resize(int width, int height) {
        screenX = width;
        screenY = height;

        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
	}

	@Override
	public void render() {
        camera.update();
        if(checkAccelerometer) {
            accelX = Gdx.input.getAccelerometerX();
            accelY = Gdx.input.getAccelerometerY();
            //accelZ = Gdx.input.getAccelerometerZ();
            Gdx.app.debug("LibGDX", "Acceleration X: " + accelX);
            Gdx.app.debug("LibGDX", "Acceleration Y: " + accelY);
            //Gdx.app.debug("LibGDX", "Acceleration Z: " + accelZ);
        }

        Gdx.gl.glViewport(0, 0, screenX, screenY);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(camera);
        compass.transform.setFromEulerAngles(accelX, accelY, accelZ);
        modelBatch.render(compass, environment);
        modelBatch.end();
	}


    @Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
        modelBatch.dispose();
        compass = null;
        assetManager.dispose();
	}

}
