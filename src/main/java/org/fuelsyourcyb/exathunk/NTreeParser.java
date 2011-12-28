package org.fuelsyourcyb.exathunk;

public interface NTreeParser<Source, FuncId, Value> {
    NTree<Unit, FuncId, Value> parse(Source expression) throws ParseException;
}