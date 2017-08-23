package kr.re.etri.kmtk.io;

import static kr.re.etri.kmtk.Common.*;

import org.rocksdb.*;

import java.io.IOException;

public class Rocks {
    private String path;
    private boolean readOnly;
    private RocksDB db;

    class Quiet extends Logger {
        Quiet() {
            super(new Options());
        }

        protected void log(InfoLogLevel lv, String s) {
        }
    }

    public Rocks(String path, boolean readOnly) throws RocksDBException {
        RocksDB.loadLibrary();
        Options opts = new Options().setCreateIfMissing(true).setLogger(new Quiet());
        this.db = readOnly ? RocksDB.openReadOnly(opts, path) : RocksDB.open(opts, path);
        this.path = path;
        this.readOnly = readOnly;
    }

    @Override
    public String toString() {
        return String.format("Rocks(%s?mode=%s)", path, readOnly ? "R" : "RW");
    }

    public void close() {
        db.close();
    }

    public boolean contains(Object k) throws RocksDBException, IOException {
        return db.get(bytes(k)) != null;
    }

    public Object get(Object k) throws RocksDBException, IOException {
        return db.get(bytes(k));
    }

    public String getString(Object k) throws RocksDBException, IOException {
        return asStr(get(k));
    }

    public void put(Object k, Object v) throws RocksDBException, IOException {
        db.put(bytes(k), bytes(v));
    }

    public void delete(Object k) throws RocksDBException, IOException {
        db.delete(bytes(k));
    }

    public static void main(String[] args) throws RocksDBException, IOException {
        Rocks data = new Rocks("testDB", false);
        System.out.println("DB = " + data);
        System.out.println("DB.put(apple, 사과)");
        System.out.println("DB.put(banana, 바나나)");
        System.out.println("DB.put(carrot, 당근)");
        data.put("apple", "사과");
        data.put("banana", "바나나");
        data.put("carrot", "당근");
        System.out.println("DB.get(apple) = " + asStr(data.get("apple")));
        System.out.println("DB.getString(apple) = " + data.getString("apple"));
        System.out.println("DB.contains(apple) = " + data.contains("apple"));
        data.delete("apple");
        System.out.println("DB.delete(apple)");
        System.out.println("DB.contains(apple) = " + data.contains("apple"));
        data.close();
    }
}
