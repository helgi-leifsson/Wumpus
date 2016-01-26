/**
 * Created by IntelliJ IDEA.
 * User: Lap
 * Date: 20.3.2011
 * Time: 19:04
 * To change this template use File | Settings | File Templates.
 */

//Data structure for the facts stored in rules
public class Fact
{
    public int X;           //x location
    public int Y;           //y location
    public String What;     //What the fact is
    public boolean Val;     //Value of the fact

    public Fact()
    {
        X = -1;
        Y = -1;
        What = new String();
        Val = false;
    }

    public Fact( int x, int y, String what, boolean val )
    {
        X = x;
        Y = y;
        What = new String( what );
        Val = val;
    }

    public Fact( Fact fact )
    {
        X = fact.X;
        Y = fact.Y;
        What = new String( fact.What );
        Val = fact.Val;
    }

    //Compares two facts
    public boolean isSameAs( Fact fact )
    {
        return ( X != fact.X || Y != fact.Y || !What.equals( fact.What ) || Val != fact.Val );
    }
}
