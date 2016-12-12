package eu.ewall.platform.ekitchen;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

/**
 * Created by apne on 17/10/2016.
 */
public class ShoppingDataTable extends DatabaseTableDef<ShoppingData> {
    public static final String NAME = "shoppingdata";

    private static final int VERSION = 0;

    public ShoppingDataTable() {
        super(NAME, ShoppingData.class, VERSION);
    }

    @Override
    public int upgradeTable(int version, Database db)
            throws DatabaseException {
        return 0;
    }
}
