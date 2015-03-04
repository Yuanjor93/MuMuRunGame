package myMobileCow.mumurun;

import myMobileCow.mumurun.SceneManager.AllScenes;
import myMobileCow.mumurun.SceneManager;
import org.andengine.engine.Engine;
import android.hardware.SensorManager;
import com.badlogic.gdx.math.Vector2;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/*
 * The cow is stationary, the fences will be moving toward the cow. The cow will have to jump over the fence.
 */

public class GameActivity extends BaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 840;
	private static final int CAMERA_HEIGHT = 420;
	BitmapTextureAtlas cowTexture;
	ITextureRegion cowTextureRegion;
	PhysicsWorld physicsWorld;
	Scene scene;
	SceneManager sceneManager;
	Camera mCamera; // used to pass in info from scene manage class

	@Override
	public EngineOptions onCreateEngineOptions() {
		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions options = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
		return options;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {

		sceneManager = new SceneManager(this, mEngine, mCamera);
		sceneManager.loadSplashResource();
		sceneManager.loadGameResource();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	// Sets physics onto Game Scene
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {

		// this.scene = sceneManager.createSplashScene();
		this.scene = sceneManager.createGameScene();
		
		final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, getVertexBufferObjectManager());
		PhysicsFactory.createBoxBody(this.physicsWorld, ground, BodyType.StaticBody, null);
		
		
		createPhysics();
		createGround();
		pOnCreateSceneCallback.onCreateSceneFinished(sceneManager
				.createSplashScene());
	}

	void createPhysics() {
		physicsWorld = new PhysicsWorld(new Vector2(0,
				SensorManager.GRAVITY_EARTH), false);
		this.scene.registerUpdateHandler(physicsWorld);
	}

	/*
	 * ground for GameScene: Used so the Cow will have a place to land
	 */

	private void createGround() {
		// TODO Auto-generated method stub
		FixtureDef WALL_FIX = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);
		Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 15, CAMERA_WIDTH,
				15, this.mEngine.getVertexBufferObjectManager());
		PhysicsFactory.createBoxBody(physicsWorld, ground, BodyType.StaticBody,
				WALL_FIX);
		this.scene.attachChild(ground);
	}

	public void fencephysics() {
		this.scene = sceneManager.createGameScene();
		Sprite sFence = new Sprite(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2,
				cowTextureRegion, this.mEngine.getVertexBufferObjectManager());
		sFence.setRotation(0.0f);
		// /density/Weight, elasticity, friction
		final FixtureDef PLAYER_FIX = PhysicsFactory.createFixtureDef(10.0f,
				0.8f, 1.0f);
		Body body = PhysicsFactory.createBoxBody(physicsWorld, sFence,
				BodyType.DynamicBody, PLAYER_FIX);
		this.scene.attachChild(sFence);
		// player body position rotation
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(sFence,
				body, true, false));

	}

	/*
	 * public void jump() { cow.setLinearVelocity(new
	 * Vector2(cow.getLinearVelocity().x, 12)); }
	 */

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		// TODO Auto-generated method stub

		// Adding physics to the Cow
		Sprite sPlayer = new Sprite(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2,
				cowTextureRegion, this.mEngine.getVertexBufferObjectManager());

		// sPlayer.setRotation(0.0f);
		// /density/Weight, elasticity, friction
		final FixtureDef PLAYER_FIX = PhysicsFactory.createFixtureDef(10.0f,
				0.8f, 1.0f);
		Body body = PhysicsFactory.createBoxBody(physicsWorld, sPlayer,
				BodyType.DynamicBody, PLAYER_FIX);
		sceneManager.createGameScene().attachChild(sPlayer);
		// player body position rotation
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(sPlayer,
				body, true, false));

		// event listener have two seconds to load the game
		mEngine.registerUpdateHandler(new TimerHandler(2f,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {

						mEngine.unregisterUpdateHandler(pTimerHandler);

						// TODO Auto-generated method stub
						sceneManager.loadGameResource();
						/**
						 * Attach physics to the scene
						 */

						sceneManager.createGameScene();
						sceneManager.setCurrentScene(AllScenes.GAME);
					}
				}));
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}
