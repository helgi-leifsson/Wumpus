/**
 * Created by IntelliJ IDEA.
 * User: Lap
 * Date: 21.3.2011
 * Time: 15:25
 * To change this template use File | Settings | File Templates.
 */

//Contains information about a cell in the environment
public class Cell
{
    public boolean Stench, Breeze, Glitter, Bump, Scream;
    public boolean Visited, Safe, Pit, Wumpus, Arrow;

    public Cell()
    {
        Stench = false;
        Breeze = false;
        Glitter = false;
        Bump = false;
        Scream = false;
        Visited = false;
        Safe = false;
        Pit = false;
        Wumpus = false;
        Arrow = false;
    }

    public Cell(boolean stench, boolean breeze, boolean glitter, boolean bump, boolean scream,
                boolean visited, boolean safe, boolean pit, boolean wumpus, boolean arrow)
    {
        Stench = stench;
        Breeze = breeze;
        Glitter = glitter;
        Bump = bump;
        Scream = scream;
        Visited = visited;
        Safe = safe;
        Pit = pit;
        Wumpus = wumpus;
        Arrow = arrow;
    }
}
