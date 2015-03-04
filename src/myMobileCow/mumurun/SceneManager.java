package myMobileCow.mumurun;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
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

public class SceneManager {
	private AllScenes currentScene;
	private BaseGameActivity activity;
	private Engine engine;
	private Camera camera;
	public BitmapTextureAtlas splashTA, backGroundTA, cowTexture, fenceTA;
	public ITextureRegion splashTR, backGroundTR, cowTextureRegion, fenceTR;
	private Scene splashScene, gameScene;
	PhysicsWorld physicsWorld;

	public enum AllScenes {
		SPLASH, GAME
	}

	public SceneManager(BaseGameActivity act, Engine eng, Camera cam) {
		this.activity = act;
		this.engine = eng;
		this.camera = cam;

		// return currentScene;
	}

	public void loadSplashResource() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gtx/");
		splashTA = new BitmapTextureAtlas(this.activity.getTextureManager(),
				1024, 512);
		splashTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTA, this.activity, "splash.png", 0, 0);
		splashTA.load();

	}

	public void loadGameResource() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gtx/");
		backGroundTA = new BitmapTextureAtlas(
				this.activity.getTextureManager(), 1024, 512);
		backGroundTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				backGroundTA, this.activity, "background.png", 0, 0);

		/*
		 * Cow sprite
		 */

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gtx/");
		cowTexture = new BitmapTextureAtlas(this.activity.getTextureManager(),
				64, 32);
		cowTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(backGroundTA, this.activity, "run1.png", 0, 0);

		/*
		 * Wall sprite 
		 */
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gtx/");
		fenceTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 16,
				64);
		fenceTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				backGroundTA, this.activity, "fence.png", 0, 0);

		backGroundTA.load();
		cowTexture.load();
		fenceTA.load();
	}

	public Scene createSplashScene() {
		splashScene = new Scene();
		Sprite icon = new Sprite(0, 0, splashTR,
				engine.getVertexBufferObjectManager());
		// centers the image
		icon.setPosition((camera.getWidth() - icon.getWidth()) / 2,
				(camera.getHeight() - icon.getHeight()) / 2);
		splashScene.attachChild(icon);
		return splashScene;
	}

	public Scene createGameScene() {
		gameScene = new Scene();
		gameScene.setBackground(new Background(100, 0, 0));

		
		Sprite background = new Sprite(0, 0, backGroundTR,
				engine.getVertexBufferObjectManager());
		// centers the image
		background.setPosition((camera.getWidth() - background.getWidth()) / 2,
				(camera.getHeight() - background.getHeight()) / 2);
		gameScene.attachChild(background);

		
		// physics for cows
		
		physicsWorld = new PhysicsWorld(new Vector2(0,
				SensorManager.GRAVITY_EARTH), false);
		this.gameScene.registerUpdateHandler(physicsWorld);
		
		
		Sprite cow = new Sprite(100, 100, cowTextureRegion,
				engine.getVertexBufferObjectManager());
		cow.setPosition(420, 400);
		gameScene.attachChild(cow);
		
		
		//physics for fence

		Sprite fence = new Sprite(100, 100, fenceTR,
				engine.getVertexBufferObjectManager());
		fence.setPosition(520, 400);
		gameScene.attachChild(fence);
		return gameScene;
	}

	public AllScenes getCurrentScene() {
		return currentScene;
	}

	public void setCurrentScene(AllScenes currentScene) {
		this.currentScene = currentScene;

		switch (currentScene) {
		case SPLASH:
			break;
		case GAME:
			engine.setScene(gameScene);
			break;
		default:
			break;
		}

	}
}