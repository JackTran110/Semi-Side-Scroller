package sidescroller.entity;

import sidescroller.entity.FpsCounter;

public class PointCounter extends FpsCounter {

	public PointCounter(double x, double y) {
		super(x, y);
	}
	
	@Override
	public void calculateFPS( long now){
		if( now - lastTime >= ONE_SECOND*3){
			fpsDisplay = "Point: " + Integer.toString( frameCount);
			lastTime = now;
		}
		frameCount++;
	}
	
	public void calculatePoint(long now) {
		calculateFPS(now);
	}

}
