package sidescroller.entity.bullet;

import sidescroller.entity.GenericEntity;
import sidescroller.entity.sprite.BulletSprite;
import utility.Tuple;

public class Bullet extends GenericEntity {
	private Tuple currentPos;
	private boolean left;

	public Bullet(double x, double y, boolean left, double scale, BulletSprite sprite){
		super();
		this.currentPos = Tuple.pair(left ? x - 8 : x + 5, y + 23);
		sprite.setCoord(this.currentPos);
		sprite.setScale(scale);
		this.left = left;
		sprite.setLeft(left);
		this.sprite = sprite;
	}

	@Override
	public void update(){
		double velocity = 4.0;
		currentPos.translate(left ? -velocity : velocity, 0);
	}
}