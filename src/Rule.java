/**
 * Created by IntelliJ IDEA.
 * User: Lap
 * Date: 20.3.2011
 * Time: 19:04
 * To change this template use File | Settings | File Templates.
 */

enum Connector { Empty, Conjunction, Disjunction, Implication, Biconditional }

//Datastructure for rules stored in the Knowledge base
public class Rule
{
    public Fact F1;            //Reference to fact 1
    public Fact F2;            //Reference to fact 2
    public Rule R1;            //Reference to rule 1
    public Rule R2;            //Reference to rule 2
    public Rule Next;          //Reference to next rule

    public Connector connector;

    public RuleFactType type;

    public Rule()
    {
        F1 = null;
        F2 = null;
        R1 = null;
        R2 = null;
        Next = null;
        connector = Connector.Empty;
        type = RuleFactType.Empty;
    }

    public Rule( Fact f1, Fact f2, Rule r1, Rule r2, Rule next, Connector c, RuleFactType t )
    {
        F1 = f1;
        F2 = f2;
        R1 = r1;
        R2 = r2;
        Next = next;
        connector = c;
        type = t;
    }

    public Rule( Rule rule )
    {
        F1 = new Fact( rule.F1 );
        F2 = new Fact( rule.F2 );
        R1 = new Rule( rule.R1 );
        R2 = new Rule( rule.R2 );
        Next = rule.Next;
        connector = rule.connector;
        type = rule.type;
    }

}
