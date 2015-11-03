package info.goodline.framework.test.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by g on 02.11.15.
 */
public class TestModel implements Serializable{
    private int expire;
    private List<String> mq;
    private String errorMessage;
    private int errorCode;

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public List<String> getMq() {
        return mq;
    }

    public void setMq(List<String> mq) {
        this.mq = mq;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
