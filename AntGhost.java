import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;
import java.awt.geom.*;

public class AntGhost {
     private static final int ENERGY_MIN     = 900;
     private static final int ENERGY_OPTIMAL = 1000;
     private static final int FRIGHT         = 50;
     private AntShell shell;
     private World world = World.getInstance();
     private Random rand = world.getRandomness();
     private enum State {
          EXPLORING,
          SEARCHING_FOOD,
          MOVING_TO_FOOD,
          FEEDING,
          HIDING
     };

     private State state;
     private LeafShell food;
     private int fright;
     private int deltaX;
     private int deltaY;

     public AntGhost(AntShell shell) {
          this.shell = shell;
          state = State.EXPLORING;
     }

     private Point getMousePosition() {
          Point mousePosition = MouseInfo.getPointerInfo().getLocation();
          mousePosition.x -= world.getLocationOnScreen().x;
          mousePosition.y -= world.getLocationOnScreen().y;
          return mousePosition;
     }

     public void run() throws Exception {
          Point mousePosition = getMousePosition();
          ArrayList<Shell> objects = world.getObjects();
          switch (state) {
          case EXPLORING:
               if (shell.contains(mousePosition)) {
                    fright = FRIGHT;
                    state = State.HIDING;
               } else if (shell.getEnergy() > ENERGY_MIN) {
                    if (rand.nextInt(10) == 0) {
                         deltaX = rand.nextInt(3) - 1;
                         deltaY = rand.nextInt(3) - 1;
                    }
                    shell.move(deltaX, deltaY);
               } else {
                    state = State.SEARCHING_FOOD;
               }
               break;
          case SEARCHING_FOOD:
               synchronized(objects) {
                    int distance = -1;
                    for (Shell obj : objects) {
                         if (obj instanceof LeafShell) {
                              LeafShell leaf = (LeafShell) obj;
                              int curDistance = (int) Math.sqrt(Math.pow(shell.getX() - leaf.getX(), 2)
                                                                + Math.pow(shell.getY() - leaf.getY(), 2));
                              if (distance < 0) {
                                   distance = curDistance;
                                   this.food = leaf;
                              } else if (curDistance < distance) {
                                   distance = curDistance;
                                   this.food = leaf;
                              }
                         }
                    }
                    state = State.MOVING_TO_FOOD;
               }
               break;
          case MOVING_TO_FOOD:
               if (shell.contains(mousePosition)) {
                    this.fright = FRIGHT;
                    this.state = State.HIDING;
               } else if (shell.intersects(food)) {
                    this.state = State.FEEDING;
               } else {
                    if (rand.nextInt(3) == 0) {
                         deltaX = ((((shell.getX() + (shell.getWidth() / 2))
                                     - (food.getX() + (food.getWidth() / 2))) > 0)
                                   ? -1 : 1)
                              * (rand.nextInt(2) + 1);
                         deltaY = ((((shell.getY() + (shell.getHeight() / 2))
                                     - (food.getY() + (food.getHeight() / 2))) > 0)
                                   ? -1 : 1)
                              * (rand.nextInt(2) + 1);
                    }
                    shell.move(deltaX, deltaY);
               }
               break;
          case FEEDING:
               shell.eat(food.getEnergy());
               world.remove(food);
               food = null;
               if (shell.getEnergy() > ENERGY_OPTIMAL) {
                    this.state = State.EXPLORING;
               } else {
                    this.state = State.SEARCHING_FOOD;
               }
               break;
          case HIDING:
               if (fright == 0) {
                    if (! shell.contains(mousePosition)) {
                         this.state = State.EXPLORING;
                    } else {
                         fright = 10;
                    }
               } else {
                    int deltaX = (shell.getX() - mousePosition.x) > 0 ? 2 : -2
                         * (rand.nextInt(3) + 1);
                    int deltaY = (shell.getY() - mousePosition.y) > 0 ? 2 : -2
                         * (rand.nextInt(3) + 1);
                    shell.move(deltaX, deltaY);
                    fright--;
               }
               break;
          }
     }
}
