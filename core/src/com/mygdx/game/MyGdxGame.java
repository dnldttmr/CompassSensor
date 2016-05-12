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
    private boolean checkCompass;

    private float accelX;
    private float accelY;
    private float accelZ;

    private float azimuth;
    //private float pitch;
    //private float roll;

    //Objectives
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Environment environment;
    private AssetManager assetManager;
    private ModelInstance compass;
    private ModelInstance needle;

	@Override
	public void create() {
        //Log all messages
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        //Check for Accelerometer
        boolean check = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
        if(check) {
            Gdx.app.debug("LibGDX", "Check Accelerometer: " + check);
            checkAccelerometer = check;
        }
        else {
            Gdx.app.debug("LibGDX", "Check Accelerometer: " + check);
            checkAccelerometer = check;
            //Force close application?
            //Gdx.app.exit();
        }

        //Check for compass
        check = Gdx.input.isPeripheralAvailable(Input.Peripheral.Compass);
        if(check) {
            Gdx.app.debug("LibGDX", "Check Compass: " + check);
            checkCompass = check;
        }
        else {
            Gdx.app.debug("LibGDX", "Check Compass: " + check);
            checkCompass = check;
        }

        //Get screen size
        screenX = Gdx.graphics.getWidth();
        screenY = Gdx.graphics.getHeight();

        //Initialise variables
        modelBatch = new ModelBatch();

        accelX = 0f;
        accelY = 0f;
        accelZ = 0f;

        azimuth = 0f;
        //pitch = 0f;
        //roll = 0f;

        //Create camera object
        camera = new PerspectiveCamera(70, screenX, screenY);
        camera.position.set(0f, -2f, 2f);
        camera.lookAt(0f, 0f, 0f);
        camera.near = 0.1f;
        camera.far = 300f;
        camera.update();

        //Load models
        assetManager = new AssetManager();
        assetManager.load("compass.g3db", Model.class);
        assetManager.load("needle.g3db", Model.class);
        assetManager.finishLoading();

        Model model = assetManager.get("compass.g3db", Model.class);
        compass = new ModelInstance(model);

        Model model2 = assetManager.get("needle.g3db", Model.class);
        needle = new ModelInstance(model2);

        //Setup environment
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
            accelX = accelX * (-2f);
            accelY = accelY * (2f);

            accelZ = Gdx.input.getAccelerometerZ();
            //Gdx.app.debug("LibGDX", "Acceleration X: " + accelX);
            //Gdx.app.debug("LibGDX", "Acceleration Y: " + accelY);
            //Gdx.app.debug("LibGDX", "Acceleration Z: " + accelZ);
        }

        if(checkCompass) {
            azimuth = Gdx.input.getAzimuth();
            //pitch = Gdx.input.getPitch();
            //roll = Gdx.input.getRoll();

            //Gdx.app.debug("LibGDX", "Azimuth: " + azimuth);
            //Gdx.app.debug("LibGDX", "Pitch: " + pitch);
            //Gdx.app.debug("LibGDX", "Roll: " + roll);
        }

        //Clear view
        Gdx.gl.glViewport(0, 0, screenX, screenY);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        //Detect whether the device is upside down
        if(accelZ <= 0) {
            azimuth *= -1;
        }

        //Round azimuth to stop the needle from flactuating
        round(azimuth);

        //Draw
        modelBatch.begin(camera);
        //needle.transform.setToLookAt(new Vector3(0f, 0f, 0f), new Vector3(0f, 0f, 0f));
        needle.transform.setFromEulerAngles(accelX, accelY, azimuth);
        compass.transform.setFromEulerAngles(accelX, accelY, 0f);
        modelBatch.render(needle, environment);
        modelBatch.render(compass, environment);
        modelBatch.end();
	}

    private void round(float a) {
        double number = (double) a;
        int multipleOf = 4;
        double degree = Math.floor((number +  multipleOf / 2) / multipleOf) * multipleOf;
        azimuth = (float) degree;
        Gdx.app.debug("LibGDX", "Rounded value: " + degree);
    }


    @Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
        //Clear variables
        modelBatch.dispose();
        compass = null;
        assetManager.dispose();
	}

}
