import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Helgi on 21.3.2017.
 */
class LiteralTest {
    /*
    1. lit1 null, lit2 null
    2. lit1 fact1, lit2 null
    3. lit1 null, lit2 fact2
    4. lit1 fact1..factN, lit2 fact1
    5. lit1 fact1, lit2 fact1...factN
     */
    @Test
    void isSameClauseAs() {
        Fact fact = new Fact(0, 0, "Glitter", true);
        Literal lit1 = new Literal(fact);
        Literal lit2 = new Literal(fact);
        boolean result = lit1.isSameClauseAs(lit2);
        assert(result == true);
    }

    @Test
    void isNotSameClauseAs() {
        Fact fact1 = new Fact(0, 0, "Glitter", true);
        Fact fact2 = new Fact(0, 0, "Stench", true);
        Literal lit1 = new Literal(fact1);
        Literal lit2 = new Literal(fact2);
        boolean result = lit1.isSameClauseAs(lit2);
        assert(result == false);
    }

    @Test
    void isComplementaryOf() {
        Fact fact = new Fact(0, 0, "Glitter", true);
        Literal literal = new Literal(fact);
        Literal complementary = new Literal(fact, false, true, null);
        boolean result = literal.isComplementaryOf(complementary);
        assert(result == true);
    }

    @Test
    void isNotComplementaryOf() {
        Fact fact = new Fact(0, 0, "Glitter", true);
        Literal literal = new Literal(fact);
        Literal notComplementary = new Literal(fact, true, true, null);
        boolean result = literal.isComplementaryOf(notComplementary);
        assert(result == false);
    }

    @Test
    void isEmpty() {
        Literal lit = new Literal();
        boolean result = lit.isEmpty();
        assert(result == true);
    }

    @Test
    void isNotEmpty() {
        Fact fact = new Fact();
        Literal lit = new Literal(fact);
        boolean result = lit.isEmpty();
        assert(result == false);
    }

    @Test
    void add() {
        Fact glitter = new Fact(6, 7, "Glitter", true);
        Fact stench = new Fact(8, 9, "Stench", false);
        Literal head = new Literal(glitter);
        Literal tail = new Literal(stench);
        head.Add(tail);
        Literal cur = head;
        while(cur.Next != null)
        {
            cur = cur.Next;
        }
        boolean result = cur.isSameClauseAs(tail);
        assert(result == true);
    }

}