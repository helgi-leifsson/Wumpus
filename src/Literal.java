/**
 * Created by IntelliJ IDEA.
 * User: Lap
 * Date: 20.3.2011
 * Time: 19:05
 * To change this template use File | Settings | File Templates.
 */
//Class for negating a fact without modifying it. Linked together in CNF clauses
public class Literal
{
    public Literal Next;
    public Fact fact;

    //Whether this literal is the head of a linked list of literals.
    public boolean Head;

    //Value of the literal
    public boolean Val;

    public Literal()
    {
        Next = null;
        fact = null;
        Head = false;
        Val = false;
    }

    public Literal( Fact f )
    {
        Next = null;
        fact = new Fact( f );
        Head = false;
        Val = true;
    }

    public Literal( Fact f, boolean v, boolean h, Literal n )
    {
        fact = new Fact( f );
        Val = v;
        Head = h;
        Next = n;
    }

    public Literal( Literal lit )
    {
        fact = new Fact( lit.fact );
        Val = lit.Val;
        Head = lit.Head;
        Next = lit.Next;
    }

    //For comparing two literals or literal lists
    public boolean isSameClauseAs(Literal clause)
    {
        Literal cur = this;
        //Rúlla gegnum alla literals í þessum literal lista...
        while(cur != null)
        {
            Literal cur2 = clause;
            boolean same = false;
            //...og bera saman við alla literals í hinum listanum...
            while(cur2 != null)
            {
                //...og ef ein nóða finnst sem er eins og ein í hinum listanum...
                if(cur.fact.isSameAs(cur2.fact) && cur.Val == cur2.Val)
                {
                    //...þá segjum við að þessar tvær setningar séu eins.
                    //Hvernig getum við sagt að þessar klausur séu eins? Literals mynda klausur og klausur eru
                    //literals tengdir saman með disjunctions. Af hverju er nóg að finna bara eina literal sem er eins?
                    //Það er af því að það literal sem við erum að leita að gæti verið hvar sem er í klausunni sem
                    //verið er að bera saman við. Uppröðunin getur verið ólík. Ef við finnum einhverja literal ekki
                    //einhversstaðar í hinni klausunni þá augljóslega eru klausurnar ekki eins.
                    same = true;
                    break;
                }
                cur2 = cur2.Next;
            }
            //Ef við fundum ekki eina einustu literal sem var eins þá segjum við að þessar tvær setningar séu ekki eins.
            if(!same)
            {
                return false;
            }
            cur = cur.Next;
        }
        return true;
    }

    //Whether this specific literal is the complementary of another literal
    public boolean isComplementaryOf(Literal lit)
    {
        return fact.isSameAs(lit.fact) && Val != lit.Val;
    }

    public boolean isEmpty()
    {
        return fact == null && Next == null;
    }

    //Adds a literal to the back of the list
    public void Add(Literal lit)
    {
        if(fact == null)
        {
            fact = new Fact(lit.fact);
            Head = true;
            Val = lit.Val;
            Next = null;
        }
        else
        {
            Literal l = new Literal(lit);
            l.Head = false;
            Literal cur = this;
            while(cur.Next != null)
            {
                cur = cur.Next;
            }
            cur.Next = l;
        }
    }
}
