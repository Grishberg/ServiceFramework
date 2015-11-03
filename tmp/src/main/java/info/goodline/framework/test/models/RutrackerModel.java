package info.goodline.framework.test.models;

import java.io.Serializable;

/**
 * Created by g on 03.11.15.
 */
public class RutrackerModel implements Serializable {
    public Format format;
    long update_time;

    public static class Format{
        C c;
        F f;
    }
    public static class C{
        String cat_id;
    }
    public static class F{
        String forum_id;
    }
}
