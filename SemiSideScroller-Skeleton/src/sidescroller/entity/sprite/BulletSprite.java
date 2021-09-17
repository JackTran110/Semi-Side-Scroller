package sidescroller.entity.sprite;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import sidescroller.entity.property.Sprite;

public class BulletSprite extends Sprite {

	public static final Image TYPE_ONE =  new Image("file:assets\\bullet\\b_1.png"),
			TYPE_TWO = new Image("file:assets\\bullet\\b_2.png"),
			TYPE_THREE = new Image("file:assets\\bullet\\b_3.png");

	private boolean left;

	@Override
	public void draw( GraphicsContext gc){
		Image type = TYPE_ONE;
		if(left) {
			gc.drawImage(type, 0, 0, type.getWidth(), type.getHeight(), coord.x()+(type.getWidth() * scale), coord.y(), -type.getWidth() * scale, type.getHeight() * scale);
		} else {
			gc.drawImage(type, 0, 0, type.getWidth(), type.getHeight(), coord.x()+(type.getWidth() * scale), coord.y(), type.getWidth() * scale, type.getHeight() * scale);			
		}
	}
	
	public void setLeft(boolean left) {
		this.left = left;
	}
}
