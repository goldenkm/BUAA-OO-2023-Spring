package src;

import java.util.List;

public class MyJvm {
    private static final int DEFAULT_CAPACITY = 16;
    private final JvmHeap heap;

    MyJvm() {
        heap = new JvmHeap(DEFAULT_CAPACITY);
    }

    // [4] 依据提示，完整书写以下方法的JML
    /*@ public normal_behavior
      @ assignable heap;
      [4-1]; 写出调用方法前后，状态发生变化的字段
      @ requires \old(heap.getSize()) + count < DEFAULT_CAPACITY;
      [4-2]; 条件分支1：加入count数量的object，size未达到上限
      @ ensures size < DEFAULT_CAPACITY;
      [4-3]; 调用方法后，size应满足的条件
      @ ensures (\forall int i; 1 <= i && i <= \old(size);
      @          (\exists int j; 1 <= j && j <= size;
      @          \old(heap.getElementData[i]).equals(heap.getElementData[j]);
      [4-4]; 调用方法后，elements数组应满足仍包含原elements数组全部元素的条件
      @ also
      @ requires \old(heap.getSize()) + count >= DEFAULT_CAPACITY;
      [4-5]; 条件分支2：加入count数量的object，size已达到上限
      @ ensures [4-6]; heap.getSize() >= DEFAULT_CAPACITY
      调用方法后，size应满足的条件
      @ ensures (\forall int i; 1 <= i && i <= \old(size);
      @          \old(heap.getElementData[i].isReferenced()) ==>
      @           (\exists int j; 1 <= j && j <= size; heap.getElementData[j].equals(\old(heap.getElementData[i]))));
      调用方法后，elements数组应满足: (1)若原elements数组中元素仍被引用，则该元素应包含于elements数组
      @ ensures (\forall int i; 1 <= i && i <= \old(size);
      @          !(\old(heap.getElementData[i].isReferenced())) ==>
      @           (\forall int j; 1 <= j && j <= size;
      @           !heap.getElementData[j].equals(\old(heap.getElementData[i]))));
      调用方法后，elements数组应满足: (2)若原elements数组中元素未被引用，则该元素应不包含于elements数组
      @*/
    public void createObject(int count) {
        for (int i = 0; i < count; i++) {
            MyObject newObject = new MyObject();
            heap.add(newObject);
            if (heap.getSize() == DEFAULT_CAPACITY) {
                System.out.println("Heap reaches its capacity,triggered Garbage Collection.");
                GC();
            }
        }
    }

    public void setUnreferenced(List<Integer> objectId) {
        heap.setUnreferencedId(objectId);
    }

    public void GC() {
        heap.removeUnreferenced();
    }

    public void getSnapShot() {
        System.out.println("Heap: " + heap.getSize());
        for (int i = 1; i <= heap.getSize(); i++) {
            MyObject mo = (MyObject) heap.getElementData()[i];
            System.out.print(mo.getId() + " ");
        }
        System.out.println("");
        MyObject youngest = heap.getYoungestOne();
        if (youngest != null) {
            System.out.print("the youngest one's id is " + youngest.getId());
        }
        System.out.println("");
        System.out.println("\n---------------------------------");
    }
}
/*
CreateObject
5
SetUnreferenced
1 4
SnapShot
CreateObject
10
SetUnreferenced
0 6 10 15
CreateObject
5
SnapShot
 */