package eu.ewall.platform.ewallet;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;

/**
 * Created by apne on 4/28/2016.
 */
public class SleepTimeData extends AbstractDatabaseObject {
    @DatabaseField(value = DatabaseType.LONG, index = true)
    private long timestamp;
    @DatabaseField(value = DatabaseType.STRING, index = true)
    private String username;
    @DatabaseField(value = DatabaseType.INT)
    private int duration;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
