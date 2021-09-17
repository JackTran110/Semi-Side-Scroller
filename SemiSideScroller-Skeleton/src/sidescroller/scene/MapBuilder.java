package sidescroller.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import javafx.scene.canvas.Canvas;
import sidescroller.entity.GenericEntity;
import sidescroller.entity.property.Entity;
import sidescroller.entity.property.HitBox;
import sidescroller.entity.sprite.BackgroundSprite;
import sidescroller.entity.sprite.HeartSprite;
import sidescroller.entity.sprite.LandSprite;
import sidescroller.entity.sprite.PlatformSprite;
import sidescroller.entity.sprite.SpriteFactory;
import sidescroller.entity.sprite.TreeSprite;
import sidescroller.entity.sprite.tile.LandTile;
import sidescroller.entity.sprite.tile.Tile;
import utility.Tuple;

public class MapBuilder implements MapBuilderInterface {
	
	private Tuple rowColCount, dimension;
	private double scale;
	private Canvas canvas;
	private Entity background;
	private List<Entity> landMass, other;
	public static final String FIX_TOP_RIGHT_CORNER = "fix top right corner",
			FIX_TOP_LEFT_CORNER = "fix top left corner",
			FIX_BOTTOM = "fix bottom",
			FIX_DIRT = "fix dirt";
	
	MapBuilder(){
		landMass = new ArrayList<>();
		other = new ArrayList<>();
	}
	
	public static MapBuilder createBuilder() {
		return new MapBuilder();
	}

	@Override
	public MapBuilderInterface setCanvas(Canvas canvas) {
		this.canvas = canvas;
		return this;
	}

	@Override
	public MapBuilderInterface setGrid(Tuple rowColCount, Tuple dimension) {
		this.rowColCount = rowColCount;
		this.dimension = dimension;
		return this;
	}

	@Override
	public MapBuilderInterface setGridScale(double scale) {
		this.scale = scale;
		return this;
	}

	@Override
	public MapBuilderInterface buildBackground(BiFunction<Integer, Integer, Tile> callback) {
		BackgroundSprite backgroundSprite = SpriteFactory.get("Background");
		backgroundSprite.init(scale, dimension, Tuple.pair(0, 0));
		backgroundSprite.createSnapshot(canvas, rowColCount, callback);
		
		HitBox hitBox = HitBox.build(0, 0, scale * dimension.x() * rowColCount.y(), scale * dimension.y() * rowColCount.x());
		
		background = new GenericEntity(backgroundSprite, hitBox);
		return this;
	}

	@Override
	public MapBuilderInterface buildLandMass(int rowPos, int colPos, int rowConut, int colCount) {
		LandSprite landSprite = SpriteFactory.get("Land");
		landSprite.init(scale, dimension, Tuple.pair(colPos, rowPos));
		landSprite.createSnapshot(canvas, rowConut, colCount);
		
		HitBox hitBox = HitBox.build(colPos * dimension.x() * scale, rowPos * dimension.y() * scale, scale * dimension.x() * colCount, scale * dimension.y() * rowConut);
		
		landMass.add(new GenericEntity(landSprite, hitBox));
		return this;
	}

	@Override
	public MapBuilderInterface fixLandMass(int rowPos, int colPos, String fixType) {
		switch(fixType) {
		case FIX_TOP_RIGHT_CORNER:
			buildTree(rowPos, colPos, LandTile.DIRT_FIXED_RIGHT);
			buildTree(rowPos, colPos+1, LandTile.GRASS);
			buildTree(rowPos+1, colPos, LandTile.DIRT_FIXED_BOTTOM_RIGHT_CORNER);
			buildTree(rowPos+1, colPos+1, LandTile.DIRT_TOP);
			break;
		case FIX_TOP_LEFT_CORNER:
			buildTree(rowPos, colPos, LandTile.GRASS);
			buildTree(rowPos, colPos+1, LandTile.DIRT_FIXED_LEFT);
			buildTree(rowPos+1, colPos, LandTile.DIRT_TOP);
			buildTree(rowPos+1, colPos+1, LandTile.DIRT_FIXED_BOTTOM_LEFT_CORNER);
			break;
		case FIX_DIRT:
			buildTree(rowPos, colPos, LandTile.DIRT);
			buildTree(rowPos, colPos+1, LandTile.DIRT);
		case FIX_BOTTOM:
			buildTree(rowPos, colPos, LandTile.DIRT_BOTTOM);
			buildTree(rowPos, colPos+1, LandTile.DIRT_BOTTOM);
		}
		return this;
	}

	@Override
	public MapBuilderInterface buildTree(int rowPos, int colPos, Tile tile) {
		TreeSprite treeSprite = SpriteFactory.get("Tree");
		treeSprite.init(scale, dimension, Tuple.pair(colPos, rowPos));
		treeSprite.createSnapshot(canvas,tile);
		
		other.add(new GenericEntity(treeSprite, null));
		return this;
	}

	@Override
	public MapBuilderInterface buildPlatform(int rowPos, int colPos, int length, Tile tile) {
		PlatformSprite platformSprite = SpriteFactory.get("Platform");
		platformSprite.init(scale, dimension, Tuple.pair(colPos, rowPos));
		platformSprite.createSnapshot(canvas,tile, length);
		
		HitBox hitBox = HitBox.build((colPos + .5) * dimension.x() * scale, rowPos * dimension.y() * scale, scale * dimension.x() * (length - 1), scale * dimension.y() / 2);
		
		other.add(new GenericEntity(platformSprite, hitBox));
		return this;
	}
	
	@Override
	public MapBuilderInterface buildHealth(int rowPos, int colPos, String heartType) {
		HeartSprite heartSprite = SpriteFactory.get("Heart");
		heartSprite.init(scale, dimension, Tuple.pair(colPos, rowPos));
		heartSprite.createSnapshot(canvas, heartType);
		
		other.add(new GenericEntity(heartSprite, null));
		return this;
	}

	@Override
	public Entity getBackground() {
		return background;
	}

	@Override
	public List<Entity> getEntities(List<Entity> list) {
		list.addAll(landMass);
		list.addAll(other);
		return list;
	}

}
