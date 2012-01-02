package net.exathunk.base;

public class IntTypeChecker implements  TypeChecker<Class, String, Object> {
    public boolean canCast(Class fromType, Class toType) {
        if (Any.class.equals(fromType)) return true;
        else if (Object.class.equals(toType)) return true;
        else return toType.isAssignableFrom(fromType);
    }

    public Object cast(Class type, String fromValue) throws TypeException {
        if (Integer.class.equals(type)) {
            try {
                return new Integer(fromValue);
            } catch (NumberFormatException e) {
                throw new TypeException("Invalid integer", e);
            }
        } else if (String.class.equals(type)) {
            return fromValue;
        } else if (Boolean.class.equals(type)) {
            if ("true".equals(fromValue)) {
                return Boolean.TRUE;
            } else if ("false".equals(fromValue)) {
                return Boolean.FALSE;
            } else {
                throw new TypeException("Invalid boolean: "+fromValue);
            }
        } else if (Any.class.equals(type)) {
            return fromValue;  // TODO(ejconlon) ugly
        } else {
            throw new TypeException("Cannot convert "+fromValue+" to "+type);
        }
    }
}
