package sidescroller.animator;

import java.util.Iterator;
import java.util.function.Consumer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sidescroller.entity.player.Player;
import sidescroller.entity.property.Entity;
import sidescroller.entity.property.HitBox;

public class Animator extends AbstractAnimator {
	private Color background = Color.ANTIQUEWHITE;

	public void handle(GraphicsContext gc, long now) {
		this.updateEntities();
		this.clearAndFill(gc, this.background);
		this.drawEntities(gc);
	}

	public void drawEntities(GraphicsContext gc, long now) {
		this.drawEntities(gc);
	}

	@Override
	public void drawEntities(GraphicsContext gc) {
		Consumer<Entity> draw = e -> {
			if (e != null && e.isDrawable()) {
				e.getDrawable().draw(gc);
				if (map.getDrawBounds() && e.hasHitbox()) {
					e.getHitBox().getDrawable().draw(gc);
				}
			}
		};

		draw.accept(map.getBackground());

		for (Entity shape : map.staticShapes()) {
			draw.accept(shape);
		}
		;

		for (Entity player : map.players()) {
			draw.accept(player);
		}
		
		this.map.movingShapes().forEach(ent -> draw.accept(ent));
	}

	@Override
	public void updateEntities() {
		for (Entity ent : this.map.players()) {
			ent.update();
		}

		for (Entity ent : this.map.staticShapes()) {
			ent.update();
		}

		if (this.map.getDrawBounds()) {
			for (Entity player : this.map.players()) {
				HitBox hitBox = player.getHitBox();
				hitBox.getDrawable().setStroke(Color.RED);
			}
		}

		for (Entity shape : this.map.staticShapes()) {
			proccessEntityList(map.players().iterator(), shape.getHitBox());
		}
		
		this.map.movingShapes().forEach(ent -> ent.update());
	}

	@Override
	public void proccessEntityList(Iterator<Entity> iterator, HitBox shapeHitBox) {
		while (iterator.hasNext()) {
			Entity entity = iterator.next();
			HitBox bounds = entity.getHitBox();

			if (!this.map.inMap(bounds)) {
				this.updateEntity(entity, iterator);
			} else if (shapeHitBox != null && bounds.intersectBounds(shapeHitBox)) {
				if (map.getDrawBounds()) {
					bounds.getDrawable().setStroke(Color.BLUEVIOLET);
				}
				this.updateEntity(entity, iterator);
			}	
		}
	}

	@Override
	public void updateEntity(Entity entity, Iterator<Entity> iterator) {
		if (entity instanceof Player) {
			((Player) entity).stepBack();
		}
	}
}
