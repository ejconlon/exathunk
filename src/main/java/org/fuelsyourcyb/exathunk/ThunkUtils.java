package org.fuelsyourcyb.exathunk;

public class ThunkUtils {
    public static boolean statelessEquals(Thunk a, Thunk b) {
	if (a.isFinished()) {
	    return b.isFinished() &&
		a.getResult().equals(b.getResult());
	} else {
	    return !b.isFinished();
	}
    }
    

    public static boolean statefulEquals(Thunk a, Thunk b) {
	if (!a.getState().equals(b.getState())) return false;
	return statelessEquals(a, b);
    }
}