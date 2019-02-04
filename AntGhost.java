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
     private Point delta;

     public AntGhost(AntShell shell) {
          this.shell = shell;
          this.state = State.EXPLORING;
	  this.delta = new Point();
     }

     private Point getMousePosition() {
          Point mousePosition = MouseInfo.getPointerInfo().getLocation();
          mousePosition.x -= world.getLocationOnScreen().x;
          mousePosition.y -= world.getLocationOnScreen().y;
          return mousePosition;
     }

     private int calculateDistanceToLeaf(LeafShell leaf) {
	  return (int) Math.sqrt(Math.pow(shell.getX() - leaf.getX(), 2)
				 + Math.pow(shell.getY() - leaf.getY(), 2));
     }

     private LeafShell searchNearestLeaf() {
	  LeafShell nearestLeaf = null;
	  LeafShell currentLeaf;
	  int distance = -1;
	  int currentDistance;
	  ArrayList<Shell> objects = world.getObjects();
	  synchronized(objects) {
	       for (Shell obj : objects) {
		    if (obj instanceof LeafShell) {
			 currentLeaf = (LeafShell) obj;
			 currentDistance
			      = calculateDistanceToLeaf(currentLeaf);
			 if (distance < 0) {
			      distance = currentDistance;
			      nearestLeaf = currentLeaf;
			 } else if (currentDistance < distance) {
			      distance = currentDistance;
			      nearestLeaf = currentLeaf;
			 }
		    }
	       }	  
	  }
	  return nearestLeaf;
     }

     private Point calculateRandomMove() {
	  Point delta = new Point();
	  delta.x = (rand.nextInt(3) - 1)
	       * shell.getX() > 0 ? 1 : -1;
	  delta.y = (rand.nextInt(3) - 1)
	       * shell.getY() > 0 ? 1 : -1;
	  return delta;
     }
     private Point calculateRandomMoveToFood() {
	  Point delta = new Point();
	  delta.x = ((((this.shell.getX() + (this.shell.getWidth() / 2))
		       - (this.food.getX() + (this.food.getWidth() / 2))) > 0)
		     ? -1 : 1)
	       * (rand.nextInt(2) + 1);
	  delta.y = ((((this.shell.getY() + (this.shell.getHeight() / 2))
		       - (this.food.getY() + (this.food.getHeight() / 2))) > 0)
		     ? -1 : 1)
	       * (rand.nextInt(2) + 1);
	  return delta;
     }
     private Point calculateRandomMoveForHiding(Point mousePosition) {
	  Point delta = new Point();
	  delta.x = (this.shell.getX() - mousePosition.x) > 0 ? 2 : -2
	       * (rand.nextInt(3) + 1) * (this.shell.getX() > 0 ? 1 : -1);
	  delta.y = (this.shell.getY() - mousePosition.y) > 0 ? 2 : -2
	       * (rand.nextInt(3) + 1) * (this.shell.getY() > 0 ? 1 : -1);

	  return delta;
     }

     public void run() throws Exception {
          Point mousePosition = getMousePosition();
          switch (state) {
          case EXPLORING:
               if (shell.contains(mousePosition)) {
                    fright = FRIGHT;
                    state = State.HIDING;
               } else if (shell.getEnergy() > ENERGY_MIN) {
		    if (rand.nextInt(15) == 0) {
			 delta = calculateRandomMove();
		    }
                    shell.move(delta);
               } else {
                    state = State.SEARCHING_FOOD;
               }
               break;
          case SEARCHING_FOOD:
	       this.food = searchNearestLeaf();
	       state = State.MOVING_TO_FOOD;
               break;
          case MOVING_TO_FOOD:
               if (shell.contains(mousePosition)) {
                    this.fright = FRIGHT;
                    this.state = State.HIDING;
               } else if (shell.intersects(food)) {
                    this.state = State.FEEDING;
               } else {
		    if (rand.nextInt(3) == 0) {
			 delta = calculateRandomMoveToFood();
		    }
                    shell.move(delta);
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
		    if (rand.nextInt(3) == 0) {
			 delta = calculateRandomMoveForHiding(mousePosition);
		    }
                    shell.move(delta);
                    fright--;
               }
               break;
          }
     }
}
