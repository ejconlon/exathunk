package org.fuelsyourcyb.exathunk;

import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Scanner;
import java.util.regex.Pattern;


public class ArithmeticParser {

    private final ThunkFactory<String, Integer> factory;

    public ArithmeticParser(ThunkFactory<String, Integer> factory) {
	this.factory = factory;
    }

    public NTree<String, Integer> parse(Deque<String> src) {
	NTree<String, Integer> tree = new NTree<String, Integer>();
	String op = src.removeFirst();
	tree.setBranch(op, new ArrayList<NTree<String, Integer>>(2));
	for (int i = 0; i < 2; ++i) {
	    if (src.isEmpty()) {
		throw new IllegalArgumentException("Invalid expression - no more tokens");
	    }
	    String arg = src.peekFirst();
	    if (factory.knowsFunc(arg)) {
		tree.getChildren().add(parse(src));
	    } else {
		tree.getChildren().add(new NTree<String, Integer>(Integer.parseInt(src.removeFirst())));
	    }
	}
	return tree;
    }

    public NTree<String, Integer> parse(String expression) {
	Scanner scanner = new Scanner(expression);
	scanner.useDelimiter(Pattern.compile(" "));
	Deque<String> tokens = new ArrayDeque<String>();
	for (; scanner.hasNext(); ) {
	    tokens.add(scanner.next());
	}
	if (tokens.isEmpty()) {
	    throw new ThunkEvaluationException("Empty string");
	} else {
	    return parse(tokens);
	}
    }
}