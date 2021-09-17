package sidescroller.entity.sprite;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import sidescroller.entity.property.Sprite;

public class HeartSprite extends Sprite{

	public static final String FULL_HEART = "file:assets\\heart\\full_heart.png",
			HALF_HEART = "file:assets\\heart\\half_heart.png",
			EMPTY_HEART = "file:assets\\heart\\empty_heart.png";

	private Image heartImage;

	@Override
	public void draw( GraphicsContext gc){
		gc.drawImage( heartImage, 0, 0);
	}

	public void createSnapshot( Canvas canvas, String heartType){
		Image image;
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect( 0, 0, canvas.getWidth(), canvas.getHeight());
		switch(heartType) {
		case FULL_HEART:
			image = new Image(FULL_HEART);
			break;
		case HALF_HEART:
			image = new Image(HALF_HEART);
			break;
		default:
			image = new Image(EMPTY_HEART);
			break;
		}
		gc.drawImage( image, 0, 0, 36, 32, coord.x()*scale*tileSize.x(), coord.y()*scale*tileSize.y()+3, tileSize.x() * scale, tileSize.y() * scale);
		heartImage = super.createSnapshot( canvas);
	}
}