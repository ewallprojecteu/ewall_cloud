package eu.ewall.platform.ewallet;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;

/**
 * Class to write transactions' data spending reward coins to the database.
 *
 * Created by apne on 4/24/2016.
 */
public class TransactionData extends AbstractDatabaseObject {
    @DatabaseField(value = DatabaseType.LONG, index = true)
    private long timestamp;
    @DatabaseField(value = DatabaseType.STRING, index = true)
    private String username;
    @DatabaseField(value = DatabaseType.STRING)
    private String item;
    @DatabaseField(value = DatabaseType.DOUBLE)
    private double cost;

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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}