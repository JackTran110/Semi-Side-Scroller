package sidescroller.animator;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sidescroller.entity.FpsCounter;
import sidescroller.entity.Grid;
import sidescroller.entity.PointCounter;
import sidescroller.entity.property.Drawable;
import sidescroller.scene.MapSceneInterface;
import utility.Tuple;

public abstract class AbstractAnimator extends AnimationTimer implements AnimatorInterface {
	protected MapSceneInterface map;
	protected Tuple mouse;
	private Canvas canvas;
	private FpsCounter fps;
	private PointCounter point;
	private Grid grid;
	
	public AbstractAnimator() {
		this.mouse = new Tuple();
		this.fps = new FpsCounter(10, 25);
		point = new PointCounter(310, 25);
		
		Drawable<?> fpsSprite = this.fps.getDrawable();
		fpsSprite.setFill(Color.BLACK);
		fpsSprite.setStroke(Color.WHITE);
		fpsSprite.setWidth(1);
		
		Drawable<?> pointSprite = this.point.getDrawable();
		pointSprite.setFill(Color.BLACK);
		pointSprite.setStroke(Color.WHITE);
		pointSprite.setWidth(1);
	}
	
	public void clearAndFill(GraphicsContext gc, Color background) {
		gc.setFill(background);
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
	
	public void handle(long now) {
		GraphicsContext gc = this.canvas.getGraphicsContext2D();
		point.calculatePoint(now);
		point.getDrawable().draw(gc);
		if(this.map.getDrawFPS()) {
			this.fps.calculateFPS(now);
		}
		this.handle(gc, now);
		if(map.getDrawGrid()) {
			if(this.grid == null) {
				this.grid = new Grid(map.getGridCount(), canvas.getWidth(), canvas.getHeight());
				Drawable<?> gridSprite = this.grid.getDrawable();
				
				gridSprite.setStroke(Color.BLACK);
				gridSprite.setWidth(1);
				gridSprite.setScale(map.getScale());
				gridSprite.setTileSize(map.getGridSize());
			}
			
			this.grid.getDrawable().draw(gc);
		}
		
		if(map.getDrawFPS()) {
			this.fps.getDrawable().draw(gc);
		}
		point.getDrawable().draw(gc);
	}
	
	public abstract void handle(GraphicsContext gc, long now);
	
	public void setMapScene(MapSceneInterface map) {
		this.map = map;
	}
	
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}
}
