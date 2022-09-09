import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

public class MouseShaker extends Thread {
  protected boolean shakerActive = true;
  protected MouseShakerTray tray;
  
  public MouseShaker(boolean shakerActive, MouseShakerTray tray) {
    super();
    this.shakerActive = shakerActive;
    this.tray = tray;
  }

  public void run() {
    Robot robot;
    Point currentMousePosition;
    Point tempMousePosition;
    try {
      robot = new Robot();
    } catch (Exception e) {
      return;
    }
    while(true) {
      try {    
        if(shakerActive) {
          tray.toggleIcon();
          currentMousePosition = MouseInfo.getPointerInfo().getLocation();
          tempMousePosition = (currentMousePosition.x > 0) 
              ? new Point(currentMousePosition.x - 10, currentMousePosition.y) 
              : new Point(currentMousePosition.x + 10, currentMousePosition.y);
      
          robot.mouseMove(tempMousePosition.x, tempMousePosition.y);
          robot.mouseMove(currentMousePosition.x, currentMousePosition.y);          
        }
        Thread.sleep(5000);
      } catch (Exception e) {}//do nothing - retry
    }
  }
}
