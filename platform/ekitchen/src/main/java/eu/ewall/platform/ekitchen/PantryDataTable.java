package eu.ewall.platform.ekitchen;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

/**
 * FridgeData table. A class like this is needed for every data collection you
 * need in MongoDB. If you change a data structure, you can implement an
 * upgrade in this class. Details are stored in the database in
 * _table_metadata.
 *
 * Created by apne on 17/10/2016.
 */
public class PantryDataTable extends DatabaseTableDef<PantryData> {
    public static final String NAME = "pantrydata";

    private static final int VERSION = 0;

    public PantryDataTable() {
        super(NAME, PantryData.class, VERSION);
    }

    @Override
    public int upgradeTable(int version, Database db)
            throws DatabaseException {
        return 0;
    }
}
