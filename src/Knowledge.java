import java.net.NetPermission;

/**
 * Created by IntelliJ IDEA.
 * User: Lap
 * Date: 20.3.2011
 * Time: 19:06
 * To change this template use File | Settings | File Templates.
 */

//For storing linked lists of literals. Links between literals are treated as disjunctions
//and links between knowledges as conjunctions
public class Knowledge
{
    public Knowledge Next;
    public Literal literal;

    public Knowledge()
    {
        Next = null;
        literal = new Literal();
    }

    public Knowledge( Literal l )
    {
        Next = null;
        literal = new Literal( l );
    }

    public Knowledge( Literal l, Knowledge k )
    {
        literal = new Literal( l );
        Next = k;
    }

    //Unions two knowledges
    public void Union( Knowledge clauses )
    {
        Knowledge cur = this;
        while( cur != null )
        {
            Knowledge cur2 = clauses;
            while( cur2 != null )
            {
                if( !cur.literal.isSameClauseAs( cur2.literal ) )
                    this.Add( cur2.literal );
                cur2 = cur2.Next;
            }
            cur = cur.Next;
        }
    }

    //Checks whether the empty clause is contained within the knowledge
    public boolean containsEmptyClause()
    {
        if( literal.isEmpty() ) return true;
        if( Next != null ) Next.containsEmptyClause();
        return false;
    }

    //Checks whether the knowledge is a subset of another knowledge
    public boolean isSubsetOf( Knowledge clauses )
    {
        Knowledge cur = this;
        while( cur != null )
        {
            Knowledge cur2 = clauses;
            while( cur2 != null )
            {
                if( !cur.literal.isSameClauseAs( cur2.literal ) ) return false;
                cur2 = cur2.Next;
            }
            cur = cur.Next;
        }
        return true;
    }

    //Adds a literal to the front of the knowledge
    public void Add( Literal lit )
    {
        if( literal != null )
        {
            Knowledge newKnowledge = new Knowledge( lit );
            newKnowledge.Next = Next;
            this.Next = newKnowledge;
        }
        else
        {
            literal = new Literal( lit );
        }
    }
}
