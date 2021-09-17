package sidescroller.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import sidescroller.animator.AnimatorInterface;
import sidescroller.entity.property.Entity;
import sidescroller.entity.property.HitBox;
import sidescroller.entity.property.Sprite;
import sidescroller.entity.sprite.HeartSprite;
import sidescroller.entity.sprite.tile.BackgroundTile;
import sidescroller.entity.sprite.tile.FloraTile;
import sidescroller.entity.sprite.tile.PlatformTile;
import utility.Tuple;

public class MapScene implements MapSceneInterface{
	
	private Tuple count, size;
	private double scale;
	private AnimatorInterface animator;
	private static List<Entity> players, staticShapes;
	public static List<Entity> movingShapes;
	private BooleanProperty drawBound, drawFPS, drawGrid;
	private Entity background;
	private Sprite pointSprite;
	
	public MapScene() {
		players = new ArrayList<>();
		staticShapes = new ArrayList<>();
		movingShapes = new ArrayList<>();
		drawBound = new SimpleBooleanProperty();
		drawFPS = new SimpleBooleanProperty();
		drawGrid = new SimpleBooleanProperty();
	}

	@Override
	public BooleanProperty drawFPSProperty() {
		return drawFPS;
	}

	@Override
	public boolean getDrawFPS() {
		return drawFPS.get();
	}

	@Override
	public BooleanProperty drawBoundsProperty() {
		return drawBound;
	}

	@Override
	public boolean getDrawBounds() {
		return drawBound.get();
	}

	@Override
	public BooleanProperty drawGridProperty() {
		return drawGrid;
	}

	@Override
	public boolean getDrawGrid() {
		return drawGrid.get();
	}

	@Override
	public MapSceneInterface setRowAndCol(Tuple count, Tuple size, double scale) {
		this.count = count;
		this.size = size;
		this.scale = scale;
		return this;
	}

	@Override
	public Tuple getGridCount() {
		return count;
	}

	@Override
	public Tuple getGridSize() {
		return size;
	}

	@Override
	public double getScale() {
		return scale;
	}

	@Override
	public MapSceneInterface setAnimator(AnimatorInterface newAnimator) {
		if(animator != null) animator.stop();
		if(newAnimator == null) throw new NullPointerException();
		animator = newAnimator;
		return this;
	}

	@Override
	public Entity getBackground() {
		return background;
	}

	@Override
	public void start() {
		if(animator != null) animator.start();
	}

	@Override
	public void stop() {
		if(animator != null) animator.stop();
	}

	@Override
	public List<Entity> staticShapes() {
		return staticShapes;
	}

	@Override
	public List<Entity> players() {
		return players;
	}

	@Override
	public MapSceneInterface createScene(Canvas canvas) {
		MapBuilder mb = MapBuilder.createBuilder();
		mb.setGridScale(scale);
		mb.setGrid(count, size);
		mb.setCanvas(canvas);
		mb.buildBackground((Integer row, Integer col) -> {
			BackgroundTile[] backgroundTiles = new BackgroundTile[] { BackgroundTile.MORNING, BackgroundTile.MORNING_CLOUD };
			if(row == 0) return BackgroundTile.MORNING_TOP;
			if(row <= 2) return backgroundTiles[new Random().nextInt(backgroundTiles.length)];
			if(row > 2 && row <= 8) return BackgroundTile.MORNING;
			return BackgroundTile.EVENING;
		});
		
		mb.buildLandMass(11, 3, 3, 9)
		.buildLandMass(3, 22, 5, 7)
		.buildLandMass(5, 18, 3, 4)
		.buildLandMass(10, 14, 5, 3)
		.buildLandMass(12, 17, 3, 17)
		.fixLandMass(5, 21, MapBuilder.FIX_TOP_LEFT_CORNER)
		.fixLandMass(7, 21, MapBuilder.FIX_BOTTOM)
		.fixLandMass(12, 16, MapBuilder.FIX_TOP_RIGHT_CORNER)
		.fixLandMass(14, 16, MapBuilder.FIX_BOTTOM)
		.buildPlatform(8, 10, 3, PlatformTile.STONE)
		.buildPlatform(6, 14, 3, PlatformTile.STONE)
		.buildTree(4, 3, FloraTile.TREE)
		.buildTree(7, 30, FloraTile.TREE_DEAD)
		.buildTree(10, 3, FloraTile.GRASS_FULL)
		.buildTree(10, 4, FloraTile.FLOWER_YELLOW)
		.buildTree(9, 4, FloraTile.SUNFLOWER_SHORT)
		.buildTree(10, 7, FloraTile.FLOWER_YELLOW)
		.buildTree(9, 7, FloraTile.SUNFLOWER_LONG)
		.buildTree(10, 8, FloraTile.GRASS_FULL)
		.buildTree(10, 9, FloraTile.FLOWER_RED)
		.buildTree(2, 26, FloraTile.FLOWER_PURPLE)
		.buildTree(2, 27, FloraTile.SIGN)
		.buildTree(2, 28, FloraTile.BUSH)
		.buildTree(11, 30, FloraTile.GRASS_FULL)
		.buildTree(11, 31, FloraTile.GRASS_FULL)
		.buildTree(11, 32, FloraTile.BUSH)
		.buildHealth(0, 2, HeartSprite.FULL_HEART)
		.buildHealth(0, 3, HeartSprite.FULL_HEART)
		.buildHealth(0, 4, HeartSprite.FULL_HEART)
		.buildHealth(0, 5, HeartSprite.FULL_HEART)
		.buildHealth(0, 6, HeartSprite.FULL_HEART)
		.buildHealth(0, 7, HeartSprite.FULL_HEART)
		.buildHealth(0, 8, HeartSprite.FULL_HEART)
		.getEntities(staticShapes);
		
		pointSprite = new Sprite() {
			@Override
			public void draw(GraphicsContext gc) {
				pointSprite.setCoord(new Tuple(scale*9, 3));
				gc.setFill( getFill());
				gc.fillText("00197", 0, 0);
				gc.setStroke( getStroke());
				gc.setLineWidth( 1000);
				gc.strokeText("00197", 0, 0);
				gc.setFont( Font.font( Font.getDefault().getFamily(), FontWeight.BLACK, 24));
			}
		};
		
		pointSprite.draw(canvas.getGraphicsContext2D());
		
		background = mb.getBackground();
		return this;
	}

	@Override
	public boolean inMap(HitBox hitBox) {
		return background.getHitBox().containsBounds(hitBox);
	}

	@Override
	public List<Entity> movingShapes() {
		return MapScene.movingShapes;
	}

}
