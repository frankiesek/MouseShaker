import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;

public class MouseShakerTray {

  private boolean shakerActive = true;
  private TrayIcon trayIcon;
  private MouseShaker mouseShaker;
  private Image[] icon = {null, null, null};
  private boolean leftRight = false;

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    } catch (Exception e) {
      return;
    }
    UIManager.put("swing.boldMetal", Boolean.FALSE);
    //Schedule a job for the event-dispatching thread:
    //adding TrayIcon.
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        MouseShakerTray app = new MouseShakerTray();
        app.initialize();
      }
    });
  }

  private void initialize() {
    //Check the SystemTray support
    if (!SystemTray.isSupported()) {
      System.out.println("SystemTray is not supported");
      return;
    }
    PopupMenu popup = new PopupMenu();
    icon[0] = createImage("image/left.png", "tray icon");
    icon[1] = createImage("image/right.png", "tray icon");
    icon[2] = createImage("image/inactive.png", "tray icon"); 
    trayIcon = new TrayIcon(icon[0]);
    trayIcon.setImageAutoSize(true);
    mouseShaker = new MouseShaker(shakerActive, this);
    SystemTray tray = SystemTray.getSystemTray();

    CheckboxMenuItem activeMenuItem = new CheckboxMenuItem("Mouse shaker active");
    activeMenuItem.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        setActive(e.getStateChange() == ItemEvent.SELECTED);        
      }
    });
    activeMenuItem.setState(true);
    setActive(true);

    MenuItem exitItem = new MenuItem("Exit");

    //Add components to popup menu
    popup.add(activeMenuItem);
    popup.addSeparator();
    popup.add(exitItem);

    trayIcon.setPopupMenu(popup);

    try {
      tray.add(trayIcon);
    } catch (AWTException e) {
      System.out.println("TrayIcon could not be added.");
      return;
    }

    trayIcon.addActionListener(new ActionListener() {//tray icon toggle
      public void actionPerformed(ActionEvent e) {
        setActive(!shakerActive);
        toggleIcon();
      }
    });

    exitItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        tray.remove(trayIcon);
        System.exit(0);
      }
    });
    mouseShaker.start();
  }
  
  protected void setActive(boolean active) {
    shakerActive = active;
    trayIcon.setToolTip("Mouse shaker " + (shakerActive ? "enabled" : "disabled"));
    mouseShaker.shakerActive = shakerActive;
  }
  
  //Obtain the image URL
  protected Image createImage(String path, String description) {
    URL imageURL = MouseShakerTray.class.getResource(path);

    if (imageURL == null) {
      System.err.println("Resource not found: " + path);
      return null;
    } else {
      return (new ImageIcon(imageURL, description)).getImage();
    }
  }
  
   public void toggleIcon() {
     int iconselect = 0;
     leftRight = !leftRight;
     if(shakerActive) iconselect = leftRight ? 1 : 0;
     else iconselect = 2;
     trayIcon.setImage(icon[iconselect]);
  }
  
  
}
