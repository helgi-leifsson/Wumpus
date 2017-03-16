import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Lap
 * Date: 18.3.2011
 * Time: 00:19
 * To change this template use File | Settings | File Templates.
 * Knowledge base data structures and CNF algorithms modified from:
 * S. Cheng, A. Jung Moon, Wumpus World: Logic and Partially Observable Environments, 2008, University of Waterloo,
 * Canada
 * Proof by contradiction and resolution algorithms from:
 * Artificial Intelligence: A modern approach, 3rd edition by Stuart Russel and Peter Norvig, 2010
 */

public class Wumpus extends JFrame
{
    private int iWidth; //Width of program frame
    private int iHeight;    //Height of program frame
    private JButton jbnNext;

    private int xGold, yGold;   //Location of gold
    private static Cell[][] Perceived;  //What the agent has perceived so far
    private static Cell[][] Elements;   //The actual environment
    private static KnowledgeBase KB;
    private static Position Agent;
    private static int xGridSize, yGridSize;    //Size of the environment
    private static Wumpus wumpus;

    public Wumpus()
    {
        iWidth = 800;
        iHeight = 600;
        setSize( iWidth, iHeight );
        setName( "Wumpus World" );
        setLayout( new FlowLayout() );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        jbnNext = new JButton( "Next" );
        add( jbnNext );

        setVisible( true );

        xGold = 35;
        yGold = 230;

        xGridSize = yGridSize = 4;

        Perceived = new Cell[xGridSize][yGridSize];
        Elements = new Cell[xGridSize][yGridSize];
        Cell emptyCell = new Cell();
        for( int I = 0; I < xGridSize; I++ )
        {
            for( int J = 0; J < yGridSize; J++ )
            {
                Perceived[I][J] = emptyCell;
                Elements[I][J] = emptyCell;
            }
        }

        Agent = new Position( 0, 0 );
        KB = new KnowledgeBase();

    }

    public void drawGrid( Graphics g )
    {
        for( int x = 35; x <= 235; x += 50 )
        {
            g.drawLine( x, 55, x, 255 );
        }
        for( int y = 55; y <= 255; y += 50 )
        {
            g.drawLine( 35, y, 235, y );
        }
    }

    public void drawGold( Graphics g, int x, int y )
    {
        g.drawString( "Gold", x, y );
    }

    public void paint( Graphics g )
    {
        drawGrid( g );
        drawGold( g, 35, 230 );
    }

    public static void main( String[] args )
    {
        wumpus = new Wumpus();
        wumpus.repaint();

        InitializeMap();
        InitKB();

        Fact foundGold = new Fact( Agent.X, Agent.Y, "Glitter", true );
        if( KB.Ask( foundGold ) ) System.out.println( "Gold" );
        /*int moves = 0;
        int maxmoves = 20;
        while( moves < maxmoves )
        {
            if( !Perceived[Agent.X][Agent.Y].Visited )
            {
                Sense( Agent.X, Agent.Y );
            }
            DisplayMap();
            Act();
            moves++;
        }*/
    }

    public static void InitKB()
    {
        /*public boolean Stench, Breeze, Glitter, Bump, Scream;
        public boolean Visited, Safe, Pit, Wumpus, Arrow;*/

        Fact f1 = new Fact( 0, 0, "Stench", false );
        Fact f2 = new Fact( 0, 0, "Breeze", false );
        Fact f3 = new Fact( 0, 0, "Glitter", true );
        Fact f4 = new Fact( 0, 0, "Bump", true );
        Fact f5 = new Fact( 0, 0, "Scream", false );
        Fact f6 = new Fact( 0, 0, "Visited", true );
        Fact f7 = new Fact( 0, 0, "Safe", true );
        Fact f8 = new Fact( 0, 0, "Pit", false );
        Fact f9 = new Fact( 0, 0, "Wumpus", false );
        Fact f10 = new Fact( 0, 0, "Arrow", false );
        Rule r1 = new Rule( f1, f2, null, null, null, Connector.Conjunction, RuleFactType.Facts );
        Rule r2 = new Rule( f3, f4, null, null, null, Connector.Conjunction, RuleFactType.Facts );
        Rule r3 = new Rule( f5, f6, null, null, null, Connector.Conjunction, RuleFactType.Facts );
        Rule r4 = new Rule( f7, f8, null, null, null, Connector.Conjunction, RuleFactType.Facts );
        Rule r5 = new Rule( f9, f10, null, null, null, Connector.Conjunction, RuleFactType.Facts );
        Rule r6 = new Rule( null, null, r1, r2, null, Connector.Conjunction, RuleFactType.Rules );
        Rule r7 = new Rule( null, null, r3, r4, null, Connector.Conjunction, RuleFactType.Rules );
        Rule r8 = new Rule( null, null, r6, r7, null, Connector.Conjunction, RuleFactType.Rules );
        Rule r9 = new Rule( null, null, r8, r5, null, Connector.Conjunction, RuleFactType.Rules );
        KB.Tell( r9 );
    }

    //TODO
    public static void InitializeMap()
    {
        Cell gold = new Cell();
        gold.Glitter = true;
        Elements[0][0] = gold;
    }

    public static void Sense( int x, int y ) //Senses the environment in a cell
    {
        boolean stench = Elements[x][y].Stench;
        boolean breeze = Elements[x][y].Breeze;
        boolean glitter = Elements[x][y].Glitter;
        boolean bump = Elements[x][y].Bump;
        boolean scream = Elements[x][y].Scream;

        Fact f1 = new Fact( x, y, "Stench", stench );
        Fact f2 = new Fact( x, y, "Breeze", breeze );
        Fact f3 = new Fact( x, y, "Glitter", glitter );
        Fact f4 = new Fact( x, y, "Bump", bump );
        Fact f5 = new Fact( x, y, "Scream", scream );
        Rule r1 = new Rule( f1, f2, null, null, null, Connector.Conjunction, RuleFactType.Facts );
        Rule r2 = new Rule( f3, f4, null, null, null, Connector.Conjunction, RuleFactType.Facts );
        Rule r3 = new Rule( null, null, r1, r2, null, Connector.Conjunction, RuleFactType.Rules );
        Rule rule = new Rule( f5, null, r3, null, null, Connector.Conjunction, RuleFactType.FactAndRule );

        KB.Tell( rule );

        //TODO
        if( stench )
        {

        }
        if( breeze )
        {

        }

        Elements[x][y].Visited = true;
        Perceived[x][y].Visited = true;

        Perceived[x][y].Stench = stench;
        Perceived[x][y].Breeze = breeze;
        Perceived[x][y].Glitter = glitter;
        Perceived[x][y].Bump = bump;
        Perceived[x][y].Scream = scream;
    }

    //TODO
    public static void DisplayMap() //Redraws the map
    {

    }

    //TODO
    public static void Act() //Chooses the next action
    {
        Fact foundGold = new Fact( Agent.X, Agent.Y, "Glitter", true );
        if( KB.Ask( foundGold ) )
        {
            System.out.println( "Found Gold" );
            Elements[Agent.X][Agent.Y].Glitter = false;
            Perceived[Agent.X][Agent.Y].Glitter = false;
            Rule empty = new Rule();
            Rule rule = new Rule( foundGold, null, empty, null, null, Connector.Empty, RuleFactType.FactAndRule );
            KB.UndoTell( rule );
        }
        Position NewPos = FindMove();
        Agent = NewPos;
    }

    public static Position FindMove() //Finds the next move
    {
        boolean upVisited, rightVisited, downVisited, leftVisited;

        upVisited = Agent.Y + 1 >= yGridSize || Perceived[Agent.X][Agent.Y + 1].Visited;
        rightVisited = Agent.X + 1 >= xGridSize || Perceived[Agent.X + 1][Agent.Y].Visited;
        downVisited = Agent.Y - 1 < 0 || Perceived[Agent.X][Agent.Y - 1].Visited;
        leftVisited = Agent.X - 1 < 0 || Perceived[Agent.X - 1][Agent.Y].Visited;

        Random rand = new Random();
        int random = rand.nextInt( 4 );
        int movesExplored = 0;
        Position newPos = null;

        while( movesExplored < 4 )
        {
            if( !upVisited && random == 0 )
            {
                if( CheckSafe( Agent.X, Agent.Y + 1 ) )
                {
                    newPos = new Position( Agent.X, Agent.Y + 1 );
                    break;
                }
            }
            else if( !rightVisited && random == 1 )
            {
                if( CheckSafe( Agent.X + 1, Agent.Y ) )
                {
                    newPos = new Position( Agent.X + 1, Agent.Y );
                    break;
                }
            }
            else if( !downVisited && random == 2 )
            {
                if( CheckSafe( Agent.X, Agent.Y - 1 ) )
                {
                    newPos = new Position( Agent.X, Agent.Y - 1 );
                    break;
                }
            }
            else if( !leftVisited && random == 3 )
            {
                if( CheckSafe( Agent.X - 1, Agent.Y ) )
                {
                    newPos = new Position( Agent.X - 1, Agent.Y );
                    break;
                }
            }
            movesExplored++;
            if( random == 3 ) random = 0;
            else random++;
        }

        //TODO: Backup in case movesExplored == 4

        return newPos;
    }

    public static boolean CheckSafe( int x, int y ) //Checks if a cell is safe
    {
        Fact noWumpus = new Fact( x, y, "Wumpus", false );
        Fact noPit = new Fact( x, y, "Pit", false );
        Fact safe = new Fact( x, y, "Safe", true );
        Rule alpha = new Rule( noWumpus, noPit, null, null, null, Connector.Conjunction, RuleFactType.Facts );
        Rule beta = new Rule( safe, null, null, null, null, Connector.Disjunction, RuleFactType.Facts );
        Rule cellSafe = new Rule( null, null, alpha, beta, null, Connector.Implication, RuleFactType.Rules );

        if( KB.Ask( safe ) == true )
        {
            Perceived[x][y].Safe = true;
            Rule thisCellisSafe = new Rule( safe, null, null, null, null, Connector.Disjunction, RuleFactType.Facts );
            KB.Tell( thisCellisSafe );
            return true;
        }
        else
        {
            Fact pit = new Fact( x, y, "Pit", true );
            Fact wumpus = new Fact( x, y, "Wumpus", true );
            if( KB.Ask( pit ) == true )
                Perceived[x][y].Pit = true;
            if( KB.Ask( wumpus ) )
            {
                Perceived[x][y].Wumpus = true;
                ShootArrow( x, y );
            }
            Perceived[x][y].Safe = false;
            return false;
        }
    }

    public static void ShootArrow( int x, int y )
    {
        Fact arrow = new Fact( x, y, "Arrow", true );
        Fact wumpus = new Fact( x, y, "Wumpus", true );
        Fact scream = new Fact( x, y, "Scream", true );
        Rule alpha = new Rule( arrow, wumpus, null, null, null, Connector.Conjunction, RuleFactType.Facts );
        Rule beta = new Rule( scream, null, null, null, null, Connector.Disjunction, RuleFactType.Facts );
        Rule wumpusdeadrule = new Rule( null, null, alpha, beta, null, Connector.Implication, RuleFactType.Rules );
        KB.Tell( wumpusdeadrule );

        if( KB.Ask( scream ) )
        {
            Perceived[x][y].Wumpus = false;
            Elements[x][y].Wumpus = false;
            RemoveStenches( x, y );
            Rule screamRule = new Rule( scream, null, null, null, null, Connector.Disjunction, RuleFactType.Facts );
            KB.Tell( screamRule );
        }
    }

    public static void RemoveStenches( int x, int y )
    {
        if( x + 1 < xGridSize )
        {
            Perceived[x + 1][y].Stench = false;
            Elements[x + 1][y].Stench = false;
        }
        if( x - 1 >= 0 )
        {
            Perceived[x - 1][y].Stench = false;
            Elements[x - 1][y].Stench = false;
        }
        if( y + 1 < yGridSize )
        {
            Perceived[x][y + 1].Stench = false;
            Elements[x][y + 1].Stench = false;
        }
        if( y - 1 >= 0 )
        {
            Perceived[x][y - 1].Stench = false;
            Elements[x][y - 1].Stench = false;
        }
    }
}
