

/**
 * Created by IntelliJ IDEA.
 * User: Lap
 * Date: 20.3.2011
 * Time: 15:50
 * To change this template use File | Settings | File Templates.
 */
public class KnowledgeBase
{

    public Rule nextRule;

    public KnowledgeBase()
    {
        nextRule = null;
    }

    public void Tell( Rule rule )
    {
        rule.Next = nextRule;
        nextRule = rule;
    }

    public void UndoTell( Rule cancelRule ) //Removes a rule from the KB
    {
        Rule ptr = nextRule;
        while( ptr != null )
        {
            if( ptr.connector == cancelRule.connector && ptr.type == cancelRule.type )
            {
                boolean allFactsEqual = false;
                if( ptr.type == RuleFactType.Facts )
                {
                    if( ptr.F1.isSameAs( cancelRule.F1 ) && ptr.F2.isSameAs( cancelRule.F2 ) )
                    {
                        allFactsEqual = true;
                    }
                }
                else if( ptr.type == RuleFactType.FactAndRule )
                {
                    if( ptr.F1.isSameAs( cancelRule.F1 ) && ptr.R2.F1.isSameAs( cancelRule.R2.F1 )
                            && ptr.R2.F2.isSameAs( cancelRule.R2.F2 ) )
                    {
                        allFactsEqual = true;
                    }
                }
                else if( ptr.type == RuleFactType.RuleAndFact )
                {
                    if( ptr.F2.isSameAs( cancelRule.F2 ) && ptr.R1.F1.isSameAs( cancelRule.R1.F1 )
                            && ptr.R1.F2.isSameAs( cancelRule.R1.F2 ) )
                    {
                        allFactsEqual = true;
                    }
                }
                else if( ptr.type == RuleFactType.Rules )
                {
                    if( ptr.R1.F1.isSameAs( cancelRule.R1.F1 ) && ptr.R1.F2.isSameAs( cancelRule.R1.F2 )
                            && ptr.R2.F1.isSameAs( cancelRule.R2.F1 ) && ptr.R2.F2.isSameAs( cancelRule.R2.F2 ) )
                    {
                        allFactsEqual = true;
                    }
                }
                else System.out.println( "UndoTell: Unidentified RuleFactType" );
                if( allFactsEqual )
                {
                    ptr.Next = ptr.Next.Next;
                }
                else ptr = ptr.Next;
            }
            else ptr = ptr.Next;
        }
    }

    public boolean Ask( Fact fact ) //Returns whether a fact is true or false
    {
        Rule rulePtr = nextRule;
        Knowledge cnfClauses = new Knowledge();

        while( rulePtr != null ) //Turns the rules in the KB into CNF
        {
            Literal cnf = CNF( rulePtr );

            //Some cnfs might have more heads which need to be disconnected from each other because the conjunction
            //function can only return one list at a time
            Literal cur = cnf;
            Literal prev = cur;
            Literal head = cur;
            while( cur != null )
            {
                prev = cur;
                head = cur;
                if( cur.Head == true )
                {
                    head = cur;
                    while( cur.Head != true && cur != null )
                    {
                        prev = cur;
                        cur = cur.Next;
                    }
                    prev.Next = null;
                    cnfClauses.Add( head );
                }
                cur = cur.Next;
            }
            rulePtr = rulePtr.Next;
        }

        //Adds the complementary fact thats being checked to the CNF KB
        Literal notFact = new Literal( fact, false, true, null );
        cnfClauses.Add( notFact );

        Knowledge newKb = new Knowledge();
        Knowledge curKb = cnfClauses;
        //Proves that the fact cannot be false given the current KB by proof of contradiction
        //See discussion in chapter 7, figure 7.12 of Artificial Intelligence: A modern approach, 3rd edition by Stuart Russel
        //and Peter Norvig, 2010
        while( curKb.Next != null )
        {
            Knowledge nextKb = curKb.Next;
            while( nextKb != null )
            {
                Knowledge resolvents = Resolve( curKb, nextKb );
                if( resolvents.containsEmptyClause() ) return true; //The fact and complementary cancelled each other out
                newKb.Union( resolvents );
                nextKb = nextKb.Next;
            }
            if( newKb.isSubsetOf( cnfClauses ) ) return false;
            cnfClauses.Union( newKb );
            curKb = curKb.Next;
        }
        return false;
    }

    //Creates the set of all clauses possible to create from two knowledges
    public Knowledge Resolve( Knowledge I, Knowledge J )
    {
        Literal curI = I.literal;
        Literal curJ = J.literal;
        Knowledge resolvents = new Knowledge();

        while( curI.Next != null )
        {
            Literal literals = new Literal();
            while( curJ != null )
            {
                if( !curI.isComplementaryOf( curJ ) ) //Discards complementaries
                {
                    literals.Add( curJ );
                }
                curJ = curJ.Next;
            }
            Literal start = I.literal;
            while( start != null )
            {
                if( start != curI ) literals.Add( start );
            }
            resolvents.Add( literals );
            curI = curI.Next;
        }
        return resolvents;
    }

    //Turns a rule into it's CNF format
    public Literal CNF( Rule rule )
    {
        Literal first = null;
        if( rule.connector == Connector.Conjunction )
        {
            first = Conjunction( rule );

            Rule flipped = new Rule(); //rule needs to be flipped and run twice to return two different sentences
            flipped.F1 = rule.F2;
            flipped.F2 = rule.F1;
            flipped.R1 = rule.R2;
            flipped.R2 = rule.R1;
            flipped.connector = rule.connector;
            if( rule.type == RuleFactType.FactAndRule )
            {
                flipped.type = RuleFactType.RuleAndFact;
            }
            else if( rule.type == RuleFactType.FactAndRule )
            {
                flipped.type = RuleFactType.RuleAndFact;
            }
            else flipped.type = rule.type;

            Literal second = Conjunction( flipped );

            first.Next = second;
            first.Head = second.Head = true;
        }
        else if( rule.connector == Connector.Biconditional )
        {
            first = Biconditional( rule );
        }
        else if( rule.connector == Connector.Implication )
        {
            first = Implication( rule );
        }
        else if( rule.connector == Connector.Disjunction )
        {
            first = Disjunction( rule );
        }
        return first;
    }

    //Turns a rule with a conjunction into it's CNF format
    public Literal Conjunction( Rule rule )
    {
        Literal first = null;
        if( rule.type == RuleFactType.Facts || rule.type == RuleFactType.FactAndRule )
        {
            first = new Literal( rule.F1 );
        }
        else if( rule.type == RuleFactType.RuleAndFact || rule.type == RuleFactType.Rules )
        {
            first = CNF( rule.R1 );
        }
        return first;
    }

    //Turns a rule with a disjunction into it's CNF format
    public Literal Disjunction( Rule rule )
    {
        Literal first = null, second = null;
        if( rule.type == RuleFactType.Facts )
        {
            first = new Literal( rule.F1 );
            second = new Literal( rule.F2 );
            first.Next = second;
        }
        else if( rule.type == RuleFactType.FactAndRule )
        {
            first = new Literal( rule.F1 );
            if( rule.R2.connector == Connector.Conjunction || rule.R2.connector == Connector.Biconditional )
            {
                second = CNF( rule.R2 );
                if( rule.R2.connector == Connector.Conjunction )
                {
                    second.Head = false;
                    Literal firstCopy = new Literal( first );
                    firstCopy.Head = true;
                    firstCopy.Next = second;

                    second.Next.Head = false;
                    Literal firstCopy2 = new Literal( first );
                    firstCopy2.Head = true;
                    firstCopy2.Next = second.Next;
                    second.Next = firstCopy2;
                    return firstCopy;
                }
                else if( rule.R2.connector == Connector.Biconditional )
                {
                    second.Head = false;
                    Literal firstCopy = new Literal( first );
                    firstCopy.Head = true;
                    firstCopy.Next = second;

                    Literal firstCopy2 = new Literal( first );
                    firstCopy2.Head = true;
                    firstCopy2.Next = second.Next.Next;
                    second.Next.Next = firstCopy2;
                    return firstCopy;
                }
                else if( rule.R2.connector == Connector.Disjunction || rule.R2.connector == Connector.Implication )
                {
                    first.Next = second;
                }
            }

        }
        else if( rule.type == RuleFactType.RuleAndFact )
        {
            Rule reversed = new Rule( rule.F2, null, rule.R1, null, null, Connector.Disjunction, RuleFactType.FactAndRule );
            return Disjunction( reversed );
        }
        else if( rule.type == RuleFactType.Rules )
        {
            first = CNF( rule.R1 );
            second = CNF( rule.R2 );
            Literal end = FindEnd( first );
            end.Next = second;
        }
        return first;
    }

    //Turns a rule with an Implication into it's CNF format
    public Literal Implication( Rule rule )
    {
        Literal notAlpha = null;
        if( rule.type == RuleFactType.Facts )
        {
            notAlpha = new Literal( rule.F1 );
            Literal beta = new Literal( rule.F2 );
            notAlpha.Val = false;
            notAlpha.Head = true;
            notAlpha.Next = beta;
            return notAlpha;
        }
        else if( rule.type == RuleFactType.FactAndRule )
        {
            notAlpha = new Literal( rule.F1 );
            notAlpha.Val = false;
            notAlpha.Head = true;
            Literal beta = CNF( rule.R2 );
            notAlpha.Next = beta;
            return notAlpha;
        }
        else if( rule.type == RuleFactType.RuleAndFact )
        {
            notAlpha = CNF( rule.R1 );
            Negate( notAlpha );
            Literal beta = new Literal( rule.F2 );
            beta.Head = true;
            Literal cur = FindEnd( notAlpha );
            cur.Next = beta;
            return notAlpha;
        }
        else if( rule.type == RuleFactType.Rules )
        {
            notAlpha = CNF( rule.R1 );
            Negate( notAlpha );
            Literal beta = CNF( rule.R2 );
            Literal end = FindEnd( notAlpha );
            end.Next = beta;
            return notAlpha;
        }
        return notAlpha;
    }

    //Returns a pointer to the end of a list of literals
    public Literal FindEnd( Literal literal )
    {
        Literal cur = literal;
        while( cur.Next != null ) cur = cur.Next;
        return cur;
    }

    //Negates a literal using DeMorgan
    public void Negate( Literal literal )
    {
        Literal cur = literal;
        while( cur != null )
        {
            cur.Val = !cur.Val;
            cur.Head = !cur.Head;
            cur = cur.Next;
        }
    }

    //Turns a rule with a Biconditional into it's CNF format
    public Literal Biconditional( Rule rule )
    {
        Rule alpha, beta;
        alpha = beta = null;
        if( rule.type == RuleFactType.Facts )
        {
            alpha = new Rule( rule.F1, rule.F2, null, null, null, Connector.Implication, RuleFactType.Facts );
            beta = new Rule( rule.F2, rule.F1, null, null, null, Connector.Implication, RuleFactType.Facts );
        }
        else if( rule.type == RuleFactType.FactAndRule )
        {
            alpha = new Rule( rule.F1, null, null, rule.R1, null, Connector.Implication, RuleFactType.FactAndRule );
            beta = new Rule( null, rule.F1, rule.R1, null, null, Connector.Implication, RuleFactType.RuleAndFact );
        }
        else if( rule.type == RuleFactType.RuleAndFact )
        {
            alpha = new Rule( null, rule.F1, rule.R1, null, null, Connector.Implication, RuleFactType.RuleAndFact );
            beta = new Rule( rule.F1, null, null, rule.R1, null, Connector.Implication, RuleFactType.FactAndRule );
        }
        else if( rule.type == RuleFactType.Rules )
        {
            alpha = new Rule( null, null, rule.R1, rule.R2, null, Connector.Implication, RuleFactType.Rules );
            beta = new Rule( null, null, rule.R2, rule.R1, null, Connector.Implication, RuleFactType.Rules );
        }
        else
        {
            System.out.println( "Biconditional error" );
        }
        Literal first = Implication( alpha );
        Literal second = Implication( beta );
        Literal end = FindEnd( first );
        end.Next = second;
        return first;
    }
}

