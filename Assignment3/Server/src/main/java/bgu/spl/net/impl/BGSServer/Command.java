package bgu.spl.net.impl.BGSServer;

import java.io.Serializable;

public interface Command<T> extends Serializable {

    Serializable execute(T arg);
}
