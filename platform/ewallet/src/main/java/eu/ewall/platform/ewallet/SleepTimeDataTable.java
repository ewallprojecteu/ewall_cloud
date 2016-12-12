package eu.ewall.platform.ewallet;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

/**
 * Created by apne on 4/28/2016.
 */
public class SleepTimeDataTable extends DatabaseTableDef<SleepTimeData> {
    public static final String NAME = "sleeptimedata";

    private static final int VERSION = 0;

    public SleepTimeDataTable() {
        super(NAME, SleepTimeData.class, VERSION);
    }

    @Override
    public int upgradeTable(int version, Database db)
            throws DatabaseException {
        return 0;
    }
}
