package net.exathunk.base;

import net.exathunk.functional.Unit;

public interface NTreeParser {
    NTree<Unit, String, String> parse(String expression) throws ParseException;
}