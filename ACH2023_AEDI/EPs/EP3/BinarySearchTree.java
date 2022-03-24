import java.util.Comparator;
import java.util.LinkedList;
import java.lang.String;

public class BinarySearchTree<K,V>
  extends LinkedBinaryTree<Entry<K,V> > implements Dictionary<K,V> {
  protected Comparator<K> C;
  protected Position<Entry<K,V> > actionPos; //pai do nó a ser removido/inserido
  protected int numEntries;

  public BinarySearchTree(Comparator<K> c) {
    addRoot(null);
    C = c;
    numEntries = 0;
  }

  protected K key(Position<Entry<K,V> > position) {
    return position.element().getKey();
  }

  protected V value(Position<Entry<K,V> > position) {
    return position.element().getValue();
  }

  protected Entry<K,V> entry(Position<Entry<K,V> > position) {
    return position.element();
  }

  protected void replaceEntry(Position <Entry<K,V> > pos, Entry<K,V> ent) {
    ((BSTEntry <K,V>) ent).pos = pos;
    replace(pos,ent);
  }

  protected void checkKey(K key) throws InvalidKeyException {
    if(key == null) throw new InvalidKeyException("chave nula");
  }

  protected void checkEntry(Entry<K,V> ent) throws InvalidEntryException {
    if(ent == null || !(ent instanceof BSTEntry))
      throw new InvalidKeyException("entrada inválida");
  }

  protected Entry<K,V> insertAtExternal(Position<Entry<K,V> > v, Entry<K,V> e) {
    expandExternal(v);
    replace(v, e);
    numEntries++;

    return e;
  }

  protected Position<Entry<K,V> > treeSearch(K key, Position<Entry<K,V> > pos) {
    if(isExternal(pos)) return pos;
    else {
      K curKey = key(pos);
      int comp = C.compare(key, curKey);
      if(comp < 0) return treeSearch(key, left(pos));
      else if(comp > 0) return treeSearch(key, right(pos));
      return pos;
    }
  }

  protected void addAll(LinkedList<Entry<K,V> > L, Position<Entry<K,V> > v, K k) {
    if (isExternal(v)) return;
    Position<Entry<K, V> > pos = treeSearch(k,v);
    if(!isExternal(pos)) {
      addAll(L, left(pos), k);
      L.addLast(pos.element());
      addAll(L, right(pos), k);
    }
  }

  // aqui começam os métodos públicos

  public int size() { return numEntries; }

  public boolean isEmpty() { return size() == 0; }

  public Entry<K,V> find(K key) throws InvalidKeyException {
    checkKey(key);
    Position<Entry<K,V> > curPos = treeSearch(key, root());
    actionPos = curPos;
    if(isInternal(curPos)) return entry(curPos);
    return null;
  }


  public Entry<K,V> insert(K k, V x) throws InvalidKeyException {
    checkKey(k);
    Position<Entry<K,V> > insPos = treeSearch(k, root());
    while(!isExternal(insPos))
      insPos = treeSearch(k, left(insPos));
    actionPos = insPos;
    return insertAtExternal(insPos, new BSTEntry<K,V>(k, x, insPos));
  }


  protected static class StringComparator implements Comparator<String> {
      public int compare(String v1, String v2) {
        return v1.compareTo(v2);
      }
  }

  public static void main(String[] args) {
    BinarySearchTree bst = new BinarySearchTree<String, String>(new StringComparator());
    bst.insert(1, "1");
    bst.insert(2, "2");
    bst.insert(3, "3");
    bst.insert(4, "4");
    System.out.println(bst.find(4).getValue());
  }

}
