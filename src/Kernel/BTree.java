package Kernel;

import java.util.Queue;

/**
 * Created by zcy on 4/10/2016.
 */
public class BTree <T extends Comparable> {
    //B+ Tree Storage Engine
    public static final int NODE_MAX_LENGTH = 3;//MUST BE ODD
    public static final int NODE_MIDDLE_LENGTH = NODE_MAX_LENGTH/2+1;

    public static class Node{
        boolean isLeaf;
        int size;
        Object[] key = new Object[NODE_MAX_LENGTH];
        Node p,next;
        Node[] son;
        Object[] val;
        Node(boolean _isLeaf){
            isLeaf = _isLeaf;
            if(!isLeaf) {
                son = new Node[NODE_MAX_LENGTH + 1];
            }
            else {
                val = new Object[NODE_MAX_LENGTH + 1];
            }
        }
        public boolean isFull(){return size == BTree.NODE_MAX_LENGTH;}
        Node(Node from){
            this(from.isLeaf);
            p = from.p;
            if(from.isLeaf){
                for(int i=BTree.NODE_MIDDLE_LENGTH-1;i<=BTree.NODE_MAX_LENGTH-1;i++){
                    //I never used switch case !!!! 6666666
                    key[i-NODE_MIDDLE_LENGTH+1] = from.key[i];
                    val[i-NODE_MIDDLE_LENGTH+1] = from.val[i];
                }
                size = BTree.NODE_MIDDLE_LENGTH - 1;
            }
            else{
                for(int i=NODE_MIDDLE_LENGTH;i<=BTree.NODE_MAX_LENGTH-1;i++){
                    //I never used switch case !!!! 6666666
                    key[i-NODE_MIDDLE_LENGTH] = from.key[i];
                    son[i-NODE_MIDDLE_LENGTH] = from.son[i];
                }
                size = BTree.NODE_MIDDLE_LENGTH;
            }

        }
    }
    Node root = new Node(true);

    @SuppressWarnings("unchecked")
    private Node findLeaf(T key){
        Node now = root;
        boolean isFind;
        while(!now.isLeaf){
            isFind = false;
            for(int i=0;i<=now.size-1;i++){
                if(key.compareTo(now.key[i]) < 0){
                    isFind = true;
                    now = now.son[i];
                }
            }
            if(!isFind)now = now.son[now.size];
        }
        return now;
    }
    private Object find(T key) throws Exception{
        Node leaf = findLeaf(key);
        for(int i=0;i<=leaf.size -1 ;i++){
            if(leaf.key[i] == key)return leaf.val[i];
        }
        throw new RuntimeException();
    }

    @SuppressWarnings("unchecked")
    private void spiltNode(Node now){
        //Divide Node
        //Step1 .. Copy Node

        //Step2 .. upFloat
        if(now.p == null){
            now.p = new Node(false);
            now.p.key[0] = now.key[NODE_MIDDLE_LENGTH-1];
            now.p.size = 1;
            now.p.son[0] = now;
            now.next = new Node(now);
            now.p.son[1] = now.next;
            root = now.p;
        }else{
            now.next = new Node(now);
            insertInto((T)now.key[NODE_MIDDLE_LENGTH-1],now.p,now);
        }
        //Step3 .. Resize
        now.size = NODE_MIDDLE_LENGTH - 1;
    }

    @SuppressWarnings("unchecked")
    private void insertInto(T key,Node now, Node from){
        //divide and float
        //Step 0 .. Insert
        //Find Pos
        int pos = 0;
        while(pos <= now.size - 1 && key.compareTo(now.key[pos]) > 0) pos ++;
        //Move
        for(int i = now.size;i>=pos +1;i--){
            now.son[i+1] = now.son[i];
            now.key[i] = now.key[i-1];
        }
        //Insert
        now.key[pos] = key;
        now.son[pos + 1] = from.next;
        //Resize
        now.size ++ ;
        if(now.isFull())spiltNode(now);
    }

    @SuppressWarnings("unchecked")
    public void insert(T key,Object val){
        Node now = findLeaf(key);
        //now is Leaf
        int pos = 0;
        while(pos <= now.size - 1 && key.compareTo(now.key[pos]) > 0) pos ++;
        //Move
        for(int i = now.size-1;i>=pos +1;i--){
            now.val[i] = now.val[i-1];
            now.key[i] = now.key[i-1];
        }
        //Insert
        now.key[pos] = key;
        now.val[pos] = val;
        //Resize
        now.size ++ ;

        if(now.isFull())spiltNode(now);

    }


    public static void main(String[] args){
        BTree<Integer> a = new BTree<>();
        a.insert(1,2);
        a.insert(3,2);
        a.insert(4,2);
        a.insert(5,2);
        a.insert(6,2);
        a.insert(7,2);
        a.insert(8,2);
        a.insert(9,2);
        a.insert(10,2);
        a.insert(11,2);
    }
}
