
package nss.cviceni2.server;

import nss.cviceni2.compute.DBRecord;
import nss.cviceni2.compute.ServerInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

public class ServerImpl implements ServerInterface {

    CsvWriter writer;
    CsvReader reader;
    static final String data_directory = System.getProperty("user.dir") + System.getProperty("file.separator") + "data" + System.getProperty("file.separator");
    HashMap<String, DB> map;
    DBRecord[] records = null;
    File dbfile;


    public ServerImpl() {

        map = new HashMap<String, DB>();

        File dir = new File(data_directory);

        String[] list = dir.list(new MyFileFilter());
        for (int i = 0; i < list.length; i++) {
            list[i] = list[i].substring(0, list[i].length() - 6);
        }

        for (int i = 0; i < list.length; i++) {
            String dbname = list[i];
            DB db = new DB(dbname);

            try {
                reader = new CsvReader(data_directory + dbname + ".dbcsv", ';', Charset.forName("UTF-8"));

                while (reader.readRecord()) {

                    DBRecord radek = new DBRecord(reader.get(0), reader.get(1), Integer.valueOf(reader.get(2)), reader.get(3));

                    db.addRecord(radek);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            map.put(dbname, db);
        }
    }

    @Override
    public String[] listDB() {
        return (String[]) map.keySet().toArray(new String[map.size()]);
    }

    @Override
    public boolean createDB(String dbname) throws DBExistException {
        if (map.containsKey(dbname)) {
            throw new DBExistException("ERORR - Databáze se jmenem " + dbname + " uz existuje");
        }

        map.put(dbname, new DB(dbname));
        return true;
    }

    @Override
    public Integer insert(String dbname, Integer key, String message)
            throws DBNotFoundException, DuplicateKeyException {
        if (!map.containsKey(dbname)) {
            throw new DBNotFoundException("ERORR - Databáze \"" + dbname + "\" neexistuje");
        } else if (map.get(dbname).foundKey(key)) {
            throw new DuplicateKeyException("ERORR - Duplikace klíčů - takový klíč (\"" + key + "\") v databázi \"" + dbname + "\" už existuje.");
        }

        map.get(dbname).insert(key, message);
        return 0;
    }

    @Override
    public Integer update(String dbname, Integer key, String message)
            throws DBNotFoundException, KeyNotFoundException {
        if (!map.containsKey(dbname)) {
            throw new DBNotFoundException("ERORR - Databáze \"" + dbname + "\" nebyla nalezena.");
        }
        DB db = map.get(dbname);
        if (!db.foundKey(key)) {
            throw new KeyNotFoundException("ERORR - Takový klíč (\"" + key + "\") v databázi není.");
        }

        db.update(key, message);
        return 0;
    }

    @Override
    public DBRecord get(String dbname, Integer key) throws DBNotFoundException,
            KeyNotFoundException {
        if (!map.containsKey(dbname)) {
            throw new DBNotFoundException("ERORR - Databáze \"" + dbname + "\" nebyla nalezena.");
        }
        DB db = map.get(dbname);
        if (!db.foundKey(key)) {
            throw new KeyNotFoundException("ERORR - Takový klíč (\"" + key + "\") v databázi není.");
        }
        return db.get(key);
    }

    @Override
    public DBRecord[] getA(String dbname, Integer[] keys)
            throws DBNotFoundException, KeyNotFoundException {
        if (!map.containsKey(dbname)) {
            throw new DBNotFoundException("ERORR - Databáze \"" + dbname + "\" nebyla nalezena.");
        }

        DB db = map.get(dbname);

        DBRecord[] result = new DBRecord[keys.length];

        for (int i = 0; i < keys.length; i++) {
            if (!db.foundKey(keys[i])) {
                throw new KeyNotFoundException("ERORR - Takový klíč (\"" + keys[i] + "\") v databázi není.");
            }
            for (int j = 0; j < db.getRecordList().length; j++) {

                if (db.getRecordList()[j].getKey() == keys[i]) {
                    result[i] = db.getRecordList()[j];
                }
            }
        }
        return result;
    }

    /**
     * Zapíše změny na disk
     */
    @Override
    public void flush() {
        String[] list = listDB();
        for (int i = 0; i < list.length; i++) {
            String dbname = list[i];
            DB db = map.get(dbname);
            try {
                writer = new CsvWriter(data_directory + dbname + ".dbcsv", ';', Charset.forName("UTF-8"));
                for (int j = 0; j < db.getRecordList().length; j++) {
                    DBRecord dbrecord = db.getRecordList()[j];
                    writer.write(dbrecord.getTscreate());
                    writer.write(dbrecord.getTsmodify());
                    writer.write(String.valueOf(dbrecord.getKey()));
                    writer.write(dbrecord.getMessage());
                    writer.endRecord();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer.close();
        }
    }
}
