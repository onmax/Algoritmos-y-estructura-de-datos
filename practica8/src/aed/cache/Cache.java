package aed.cache;

import java.util.Iterator;

import es.upm.aedlib.Position;
import es.upm.aedlib.map.*;
import es.upm.aedlib.positionlist.*;


public class Cache<Key,Value> {
	private int maxCacheSize;
	private Storage<Key,Value> storage;
	private Map<Key,CacheCell<Key,Value>> map;
	private PositionList<Key> lru;

	public Cache(int maxCacheSize, Storage<Key,Value> storage) {
		this.storage = storage;
		this.map = new HashTableMap<Key,CacheCell<Key,Value>>();
		this.lru = new NodePositionList<Key>();
		this.maxCacheSize = maxCacheSize;
	}

	public Value get(Key key) {
		Value res = null;
		boolean esta=false;
		Iterator<Key> cursor=map.keys();
		while(cursor.hasNext()) {
			if(cursor.next().equals(key)) {
				esta=true;
			}
		}
		if(esta) {
			lru.set(lru.first(), key);
			res = storage.read(key);
		}
		else {
			res = storage.read(key);
			lru.addFirst(key);
			CacheCell cell = new CacheCell<Key, Value>(res,false,lru.first());
			map.put(key, cell);
			if(lru.size()>this.maxCacheSize) {
				map.remove(lru.last().element());
				lru.remove(lru.last());
				if(cell.getDirty()) {
					storage.write(key, res);
				}
			}

		}
		return res;
	}

	public void put(Key key, Value value) {
		Value res = null;
		if(storage.read(key) == null){
			lru.addFirst(key);
		}else {
			res = storage.read(key);
			lru.addFirst(key);
			CacheCell cell = new CacheCell<Key, Value>(res,false,lru.first());
			map.put(key, cell);
			if(lru.size()>this.maxCacheSize) {
				map.remove(lru.first().element());
				lru.remove(lru.last());
				if(cell.getDirty()) {
					storage.write(key, res);
				}
			}

		}
	}
}
