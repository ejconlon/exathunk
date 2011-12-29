package org.fuelsyourcyb.exathunk;

public class IntTypeChecker implements  TypeChecker<Class, String, Object> {
    public boolean check(Class type, String fromValue) {
	try {
	    convert(type, fromValue);
	    return true;
	} catch (TypeException e) {
	    return false;
	}
    }

    public Object convert(Class type, String fromValue) throws TypeException {
	if (Integer.class.equals(type)) {
	    try {
		return new Integer(fromValue);
	    } catch (NumberFormatException e) {
		throw new TypeException("Invalid integer", e);
	    }
	} else if (String.class.equals(type)) {
	    return fromValue;
	} else {
	    throw new TypeException("Cannot convert "+fromValue+" to "+type);
	}
    }
}
