package aed.cache;

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
	  System.out.println(key.toString());
	  if(map.get(key) == null){
		  lru.addFirst(key);
		  System.out.println("______asd");
	  }else {
		  Value a = storage.read(key);
		  System.out.println(a.toString()+"_____");
		  
	  }
    return null;
  }
  
  public void put(Key key, Value value) {
    // CAMBIA este metodo
  }
}

