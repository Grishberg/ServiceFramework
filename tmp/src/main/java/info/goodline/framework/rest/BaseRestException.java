package info.goodline.framework.rest;

/**
 * Created by g on 25.10.15.
 */
public class BaseRestException extends RuntimeException {
    private int code;

    public BaseRestException(String detailMessage, int code) {
        super(detailMessage);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
