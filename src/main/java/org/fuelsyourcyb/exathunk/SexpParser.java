package org.fuelsyourcyb.exathunk;

import java.util.List;
import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SexpParser {

    private static final String OPEN_PAREN = "(";
    private static final String CLOSE_PAREN = ")";

    public NTree<Unit, String, String> parse(Deque<String> src, int depth) throws ParseException {
	if (src.isEmpty()) throw new ParseException("Ran out of tokens");
	String op = src.removeFirst();

	NTree<Unit, String, String> tree = new NTree<>();
	List<NTree<Unit, String, String>> children = new ArrayList<NTree<Unit, String, String>>();
	tree.setBranch(Unit.getInstance(), op, children);

	while (!src.isEmpty()) {
	    String arg = src.removeFirst();
	    if (OPEN_PAREN.equals(arg)) {
		children.add(parse(src, depth+1));
	    } else if (CLOSE_PAREN.equals(arg)) {
		return tree;
	    } else {
		children.add(new NTree<Unit, String, String>(Unit.getInstance(), arg));
	    }
	}

	if (depth > 0) throw new ParseException("No closing paren");
	return tree;
    }

    public NTree<Unit, String, String> parse(String expression) throws ParseException {
	Scanner scanner = new Scanner(expression);
	scanner.useDelimiter(Pattern.compile(" |(?<=\\()|(?=\\))"));
	Deque<String> tokens = new ArrayDeque<>();
	for (; scanner.hasNext(); ) {
	    tokens.add(scanner.next());
	}
	// for (String t : tokens) { System.out.println("TOKEN: "+t); }
	if (tokens.size() < 3) {
	    throw new ParseException("Not enough tokens: "+expression);
	} else {
	    String first = tokens.removeFirst();
	    String last = tokens.removeLast();
	    if (OPEN_PAREN.equals(first) && CLOSE_PAREN.equals(last)) {
		return parse(tokens, 0);
	    } else {
		throw new ParseException("Need open and close paren: "+first+" "+last+" "+expression);
	    }
	}
    }
}