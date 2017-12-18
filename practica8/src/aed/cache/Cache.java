package aed.cache;


import es.upm.aedlib.Position;
import es.upm.aedlib.map.*;
import es.upm.aedlib.positionlist.*;

public class Cache<Key, Value> {
	private int maxCacheSize;
	private Storage<Key, Value> storage;
	private Map<Key, CacheCell<Key, Value>> map;
	private PositionList<Key> lru;

	public Cache(int maxCacheSize, Storage<Key, Value> storage) {
		this.storage = storage;
		this.map = new HashTableMap<Key, CacheCell<Key, Value>>();
		this.lru = new NodePositionList<Key>();
		this.maxCacheSize = maxCacheSize;
	}

	public Value get(Key key) {
		Value res = null;
		if (map.get(key) != null) {
			lru.addFirst(key);
			lru.remove(map.get(key).getPos());
			res = map.get(key).getValue();
			map.get(key).setValue(res);
			map.get(key).setPos(lru.first());
		} else {
			res = storage.read(key);
			lru.addFirst(key);
			CacheCell<Key,Value> cell = new CacheCell<Key, Value>(res, false, lru.first());
			map.put(key, cell);
			if (lru.size() > this.maxCacheSize) {
				Key aux=lru.last().element();
				CacheCell<Key,Value> cell2 = map.remove(lru.last().element());				
				lru.remove(lru.last());
				if (cell2.getDirty()) {
					storage.write(aux, cell2.getValue());
				}
			}
		}
		return res;
	}

	public void put(Key key, Value value) {
		Value res = null;
		if (map.get(key) != null) {
			lru.addFirst(key);
			lru.remove(map.get(key).getPos());
			res = map.get(key).getValue();
			map.get(key).setValue(res);
			map.get(key).setPos(lru.first());
		} else {
			res = storage.read(key);
			lru.addFirst(key);
			CacheCell<Key,Value> cell = new CacheCell<Key, Value>(res, false, lru.first());
			map.put(key, cell);
			if (lru.size() > this.maxCacheSize) {
				Key aux=lru.last().element();
				CacheCell<Key,Value> cell2 = map.remove(lru.last().element());				
				lru.remove(lru.last());
				if (cell2.getDirty()) {
					storage.write(aux, cell2.getValue());
				}
			}
		}
		map.get(key).setValue(value);
		map.get(key).setDirty(true);
	}
}
