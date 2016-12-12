package eu.ewall.platform.ewallet;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

/**
 * EWalletData table. A class like this is needed for every data collection you
 * need in MongoDB. If you change a data structure, you can implement an
 * upgrade in this class. Details are stored in the database in
 * _table_metadata.
 *
 * Created by apne on 4/21/2016.
 */
public class EWalletDataTable extends DatabaseTableDef<EWalletData> {
    public static final String NAME = "ewalletdata";

    private static final int VERSION = 0;

    public EWalletDataTable() {
        super(NAME, EWalletData.class, VERSION);
    }

    @Override
    public int upgradeTable(int version, Database db)
            throws DatabaseException {
        return 0;
    }
}