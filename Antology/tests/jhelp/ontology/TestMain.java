/*
 * Copyright:
 * License :
 *  The following code is deliver as is.
 *  I take care that code compile and work, but I am not responsible about any  damage it may  cause.
 *  You can use, modify, the code as your need for any usage.
 *  But you can't do any action that avoid me or other person use,  modify this code.
 *  The code is free for usage and modification, you can't change that fact.
 *  @author JHelp
 *
 */

package jhelp.ontology;

import jhelp.util.debug.Debug;
import jhelp.util.io.UtilIO;

/**
 * Created by jhelp on 22/07/17.
 */
public class TestMain
{
    public static void main(String[] args)
    {
        Graph graph = Graph.obtainGraph(UtilIO.obtainExternalFile("jhelp/graph.graph"));

        final Node sky         = Node.createNode("sky");
        final Node eyes        = Node.createNode("eyes");
        final Node grass       = Node.createNode("grass");
        final Node color       = Node.createNode("color");
        final Node composed    = Node.createNode("composed");
        final Node blue        = Node.createNode("blue");
        final Node black       = Node.createNode("black");
        final Node air         = Node.createNode("air");
        final Node oxygen      = Node.createNode("oxygen");
        final Node green       = Node.createNode("green");
        final Node n73         = Node.createNode(73);
        final Node n42         = Node.createNode(42);
        final Node number      = Node.createNode("number");
        final Node magic       = Node.createNode("magic");
        final Node answer      = Node.createNode("answer");
        final Node prime       = Node.createNode("prime");
        final Node odd         = Node.createNode("odd");
        final Node even        = Node.createNode("even");
        final Node hello       = Node.createNode("hello");
        final Node hi          = Node.createNode("hi");
        final Node concept     = Node.createNode("concept");
        final Node greeting    = Node.createNode("greeting");
        final Node home        = Node.createNode("home");
        final Node house       = Node.createNode("house");
        final Node livingPlace = Node.createNode("living place");
        final Node dad         = Node.createNode("dad");
        final Node is          = Node.createNode("is");
        final Node father      = Node.createNode("father");
        final Node uncle       = Node.createNode("uncle");
        final Node brother     = Node.createNode("brother");
        final Node mother      = Node.createNode("mother");
        final Node sister      = Node.createNode("sister");
        final Node carbon      = Node.createNode("carbon");

        Debug.verbose("");
        Debug.mark("Fill graph");
        graph.add(sky, color, blue);
        graph.add(sky, color, black);
        graph.add(sky, composed, air);
        graph.add(eyes, color, blue);
        graph.add(air, composed, oxygen);
        graph.add(grass, color, green);
        graph.add(n73, number, magic);
        graph.add(n42, number, answer);
        graph.add(n73, number, prime);
        graph.add(n42, number, even);
        graph.add(n73, number, odd);
        graph.add(hello, concept, greeting);
        graph.add(hi, concept, greeting);
        graph.add(home, concept, livingPlace);
        graph.add(house, concept, livingPlace);
        graph.add(dad, is, father);
        graph.addTwoWay(uncle, brother, father);
        graph.add(uncle, brother, mother);
        graph.add(mother, sister, uncle);

        Debug.verbose("");
        Debug.mark("sky color blue");

        graph.search(sky, color, blue).consume(Debug::information);

        Debug.verbose("");
        Debug.mark("sky color ?");

        graph.search(sky, color, Node.WILDCARD).consume(Debug::information);

        Debug.verbose("");
        Debug.mark("sky ? blue");

        graph.search(sky, Node.WILDCARD, blue).consume(Debug::information);

        Debug.verbose("");
        Debug.mark("? color blue");

        graph.search(Node.WILDCARD, color, blue).consume(Debug::information);

        Debug.verbose("");
        Debug.mark("sky ? ?");

        graph.search(sky, Node.WILDCARD, Node.WILDCARD).consume(Debug::information);

        Debug.verbose("");
        Debug.mark("? color ?");

        graph.search(Node.WILDCARD, color, Node.WILDCARD).consume(Debug::information);

        Debug.verbose("");
        Debug.mark("? ? blue");

        graph.search(Node.WILDCARD, Node.WILDCARD, blue).consume(Debug::information);

        Debug.verbose("");
        Debug.mark("? ? ?");

        graph.search(Node.WILDCARD, Node.WILDCARD, Node.WILDCARD).consume(Debug::information);

        Debug.verbose("");
        Debug.mark("? composed oxygen");

        graph.search(Node.WILDCARD, composed, oxygen).consume(Debug::information);

        Debug.verbose("");
        Debug.mark("computePath dad -> uncle");

        graph.computePath(dad, uncle).consume(path -> Debug.information(path.toString()));

        Debug.verbose("");
        Debug.mark("RULE : X composed Y & Y composed Z => X composed Z");
        Rule rule = new Rule(Node.WILDCARD, composed, Node.WILDCARD,
                             Node.WILDCARD, composed, Node.WILDCARD,
                             RuleResult.ruleResult(Result.FIRST_SUBJECT),
                             RuleResult.ruleResult(composed),
                             RuleResult.ruleResult(Result.SECOND_INFORMATION));
        rule.addConstraint(RuleConstraint.FIRST_INFORMATION_EQUALS_SECOND_SUBJECT);
        graph.addAndApplyRule(rule);

        Debug.verbose("");
        Debug.mark("? ? ?");

        graph.search(Node.WILDCARD, Node.WILDCARD, Node.WILDCARD).consume(Debug::information);

        Debug.verbose("");
        Debug.mark("air composed carbon");

        graph.add(air, composed, carbon);

        Debug.verbose("");
        Debug.mark("? ? ?");

        graph.search(Node.WILDCARD, Node.WILDCARD, Node.WILDCARD).consume(Debug::information);

        Debug.verbose("");
        Debug.mark("REMOVE ? ? magic");

        graph.remove(Node.WILDCARD, Node.WILDCARD, magic);

        Debug.verbose("");
        Debug.mark("? ? ?");

        graph.search(Node.WILDCARD, Node.WILDCARD, Node.WILDCARD).consume(Debug::information);

        Debug.mark("REMOVE ? ? odd");

        graph.remove(Node.WILDCARD, Node.WILDCARD, odd);

        Debug.verbose("");
        Debug.mark("? ? ?");

        graph.search(Node.WILDCARD, Node.WILDCARD, Node.WILDCARD).consume(Debug::information);

        Debug.mark("REMOVE ? ? prime");

        graph.remove(Node.WILDCARD, Node.WILDCARD, prime);

        Debug.verbose("");
        Debug.mark("? ? ?");

        graph.search(Node.WILDCARD, Node.WILDCARD, Node.WILDCARD).consume(Debug::information);

        Debug.mark("REMOVE ? color ?");

        graph.remove(Node.WILDCARD, color, Node.WILDCARD);

        Debug.verbose("");
        Debug.mark("? ? ?");

        graph.search(Node.WILDCARD, Node.WILDCARD, Node.WILDCARD).consume(Debug::information);

        Debug.mark("REMOVE sky ? ?");

        graph.remove(sky, Node.WILDCARD, Node.WILDCARD);

        Debug.verbose("");
        Debug.mark("? ? ?");

        graph.search(Node.WILDCARD, Node.WILDCARD, Node.WILDCARD).consume(Debug::information);

        Debug.mark("REMOVE ALL uncle");

        graph.removeAll(uncle);

        Debug.verbose("");
        Debug.mark("? ? ?");

        graph.search(Node.WILDCARD, Node.WILDCARD, Node.WILDCARD).consume(Debug::information);

        Debug.mark("Add Bob");

        final Node person1 = Node.createNode("person1");
        final Node age     = Node.createNode("age");
        final Node name    = Node.createNode("name");
        final Node bob     = Node.createNode("Bob");

        graph.add(person1, age, n42);
        graph.add(person1, name, bob);

        Debug.verbose("");
        Debug.mark("? ? ?");

        graph.search(Node.WILDCARD, Node.WILDCARD, Node.WILDCARD).consume(Debug::information);
        Query query = new Query(QueryElement.newString("person1"), QueryElement.newVariable("predicate"),
                                QueryElement.newVariable("information"));

        Person person = graph.query(query, Person.class);

        Debug.mark("Person1");
        Debug.verbose("name=", person.name());
        Debug.verbose("age=", person.age());

        Debug.mark("Add Person group");

        final Node person2 = Node.createNode("person2");
        final Node ted     = Node.createNode("Ted");

        graph.add(person2, age, n73);
        graph.add(person2, name, ted);

        final Node personGroup = Node.createNode("Person");
        final Node contains    = Node.createNode("contains");
        graph.add(personGroup, contains, person1);
        graph.add(personGroup, contains, person2);

        Debug.verbose("");
        Debug.mark("? ? ?");

        graph.search(Node.WILDCARD, Node.WILDCARD, Node.WILDCARD).consume(Debug::information);
        query = new Query(QueryElement.newString("Person"),
                          QueryElement.newString("contains"),
                          QueryElement.newQuery(new Query(QueryElement.newVariable("subject"),
                                                          QueryElement.newVariable("predicate"),
                                                          QueryElement.newVariable("information"))));

        PersonList personList = graph.query(query, PersonList.class);

        Debug.mark("PersonList");

        for (Person person3 : personList.list())
        {
            Debug.information(person3.name(), ". Age=", person3.age());
        }

        Debug.mark("THE END");

        // Utilities.sleep(4096);
        // System.exit(0);
    }
}
