package org.fbi.hmfsjm.gateway.domain.base;

import java.io.Serializable;

public abstract class Toa implements Serializable {
    public Toa toBean(String str) {
        return toToa(str);
    }

    protected abstract Toa toToa(String str);
}
