package net.exathunk.base;

import net.exathunk.functional.Unit;

public interface NTreeParser<Source, FuncId, Value> {
    NTree<Unit, FuncId, Value> parse(Source expression) throws ParseException;
}