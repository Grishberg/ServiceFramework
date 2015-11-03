package info.goodline.framework.test.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created on 03.11.15.
 *
 * @author g
 */
public class ListTvShowRest {
    private static final String TAG = ListTvShowRest.class.getSimpleName();
    public ArrayList<TvShowRest> items;

    public static class TvShowRest {
        private long startTime;
        private long endTime;
        private String title;
        private int psfile;

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getPsfile() {
            return psfile;
        }

        public void setPsfile(int psfile) {
            this.psfile = psfile;
        }
    }

}
