package sidescroller.entity.player;

import utility.PointVector;
import utility.Tuple;
import sidescroller.entity.GenericEntity;
import sidescroller.entity.sprite.PlayerSprite;
import sidescroller.entity.sprite.tile.Samurai;

public class Player extends GenericEntity{

	private static final double MAX_GRAVITY = 7;

	private PlayerSprite playerSprite;
	private Tuple currentPos;
	private PointVector velocity, acceleration;
	private PlayerInput playerInput;

	private double gravity = MAX_GRAVITY;
	private double jumpSpeed = 0;
	
	private Runnable ePressed;

	public Player( double x, double y, double w, double h, PlayerSprite sprite){
		// size of the player sprite is 20x20. hitbox is reduced to 16x16. everything is scaled up by 2,
		// hence the +/-8 when setting the bounds. ideally hitbox has to come from outside so we don't have
		// to add these specific minor adjustments here.
		super( sprite, new PlayerHitBox().setBounds( (x - w / 2) + 8, (y - h / 2) + 8, w - 8, h - 8));
		currentPos = Tuple.pair( x - w / 2, y - h / 2);
		playerSprite = sprite;
		playerSprite.setCoord( currentPos);
		playerSprite.setTile( Samurai.IDLE);
		playerSprite.setLeft( true);
		setVectors();
	}
	
	public Tuple getCurrentPos() {
		return currentPos;
	}

	public Player setInput( PlayerInput input){
		this.playerInput = input;
		// aside from the preset config in PlayerInput, also register events for mouse and key
		// release to set the player animation to idle.
		input.getAdapter().registerKey( null, key -> playerSprite.setTile( Samurai.IDLE));
		input.getAdapter().registerMouseClick( null, key -> playerSprite.setTile( Samurai.IDLE));
		return this;
	}

	public void stepBack(){
		// this method is called when when player has made an invalid move.
		// simply step back to last valid position that was recorded.
		// hitbox has a continent method that can undo but player position must be
		// manually set as we adjusted the position for it in the constructor.
		hitbox.undoTranslate();
		Tuple pos = hitbox.getBounds().startPos();
		this.currentPos.move( pos.x() - 8, pos.y() - 8);
		gravity = 0;
	}
	
	public void onEPress(Runnable callback) {
		this.ePressed = callback;
	}

	@Override
	public void update(){
		double x, y;
		if( playerInput.hasMoved()){
			playerSprite.setTile( Samurai.RUN);
			if( playerInput.leftOrRight() != 0){
				playerSprite.setLeft( playerInput.leftOrRight() < 1);
			}
		}
		if( playerInput.leftClicked()){
			playerSprite.setTile( Samurai.SWORD);
		}
		if(playerInput.isE()) {
			playerSprite.setTile( Samurai.SHOOT);
			if(playerSprite.getFrame() > 3.9 && playerSprite.getFrame() < 4) ePressed.run();
		}
		if( playerInput.isSpace() || jumpSpeed > 0){
			if( jumpSpeed == 0){
				playerSprite.setTile( Samurai.JUMP);
				jumpSpeed = velocity.magnitude();
			}
			else {
				jumpSpeed -= acceleration.magnitude();
			}
			x = jumpSpeed * playerInput.leftOrRight();
			y = -jumpSpeed;
		}else{

			x = playerInput.leftOrRight() * 2.0;
			y = playerInput.down() * 2.0;
			if( playerInput.isShift()){
				x *= 2.4;
				y *= 2.4;
			}
			y += gravity;
		}
		currentPos.translate( x, y);
		hitbox.translate( x, y);
		gravity = MAX_GRAVITY;
		setVectors();
	}
	
	private void setVectors() {
		double x = currentPos.x();
		double y = currentPos.y();
		velocity = new PointVector(x, y-15).sub(new PointVector(x, y));
		acceleration = new PointVector(x, y+1).sub(new PointVector(x, y));
	}
}
