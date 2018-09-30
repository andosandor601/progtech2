package hu.elte.progtech.solid.iterator;

import java.util.Iterator;

public class Foo {

    private final int[] array = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    
    public static void main(String[] args) {
        Foo foo = new Foo();
        Iterator<Integer> iterator = foo.getIterator();
        
        while (iterator.hasNext()) {
            System.out.println(iterator.next());         
        }
    }
    
    public Iterator<Integer> getIterator(){
        return new FooIterator();
    }

    private class FooIterator implements Iterator<Integer> {
        
        int curr = 0;

        @Override
        public boolean hasNext() {
            return curr < 10;
        }

        @Override
        public Integer next() {
            return array[curr++];
        }
        
        @Override
        public void remove(){
            throw new UnsupportedOperationException("you can't remove element from unmodifiable array");
        }
    }

}
