package de.metro.robocode;
import robocode.*;

import java.awt.Color;
import static robocode.util.Utils.normalRelativeAngleDegrees;


// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * KlaW - a robot by (your name here)
 */
public class KlaW extends AdvancedRobot
{

    /**Initial energy of enemy at start of battle.*/
    private double energyOfEnemy = 100;
    /**Direction our robot will move.*/
    private int directionToMove = 1;
    /**Initialized to 1 to create oscillating effect.*/
    private int scanDirection = 1;
    /**Distance of enemy from my robot.*/
    private double distance = 0;

    /**
     * Main run function.
     */
    @Override
    public final void run() {
        //Set the color of our robot
        setColors(new Color(9, 0, 229), new Color(0, 3, 35),
                Color.black, new Color(58, 0, 71), Color.pink);

        //Do an initial radar scan of entire field to pick up our enemy robot.
        setTurnRadarRight(360 * scanDirection);
    }


    @Override
    public final void onScannedRobot(final ScannedRobotEvent event) {
        //Always stay at 90 degree angle to the enemy.
        setTurnRight(event.getBearing() + 90 - 30 * directionToMove);

        //If the enemy has a drop in energy of <=3 we assume the
        //enemy has fired so we move.
        double changeInEnergy = energyOfEnemy - event.getEnergy();
        if (changeInEnergy > 0 && changeInEnergy <= 3) {
            //Our robot changes direction and moves closer towards enemy.
            directionToMove = -directionToMove;
            setAhead((event.getDistance() / 4 + 27) * directionToMove);
        }

        //Changes scanDirection so that radar oscillates.
        scanDirection = -scanDirection;
        setTurnRadarRight(360 * scanDirection);

        //Turn gun to face enemy.  Normalize angle so gun
        //turns the shortest distance.
        setTurnGunRight(normalRelativeAngleDegrees(
                getHeading() - getGunHeading() + event.getBearing()));

        //Update distance with current distance of enemy
        distance = event.getDistance();

        //Fire at target with power varying with distance.
        if (distance < 50 ) {
            fire(2);
        }
        else if (distance < 100) {
            fire(1.5);
        }
        else if (distance < 200) {
            fire(1);
        }
        else {
            fire(0.1);
        }
        //Update the energy level of the enemy.
        energyOfEnemy = event.getEnergy();

    }


    @Override
    public final void onHitRobot(final HitRobotEvent event) {
        directionToMove = -directionToMove;
        setAhead(20 * directionToMove);

    }
}

