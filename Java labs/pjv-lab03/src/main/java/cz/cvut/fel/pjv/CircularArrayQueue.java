package cz.cvut.fel.pjv;

/**
 * Implementation of the {@link Queue} backed by fixed size array.
 */
public class CircularArrayQueue implements Queue {
    private String [] array;
    private int beginning; // index of beginning queue
    private int end; // index of end queue
    private int size; // total of number of elements in queue
    private int capacity;

    public CircularArrayQueue() {
        this(5);

    }

    /**
     * Creates the queue with given {@code capacity}. The capacity represents maximal number of elements that the
     * queue is able to store.
     * @param capacity of the queue
     */
    public CircularArrayQueue(int capacity) {
        this.array = new String[capacity];
        this.beginning = -1;
        this.end = -1;
        this.size = 0;
        this.capacity = capacity;
    }

    @Override
    public int size() {
        if(isEmpty()){
        return 0;
        } else if(beginning <= end) {
            return end - beginning + 1;
        } else {
            return end + array.length - beginning + 1;
        }
    }

    @Override
    public boolean isEmpty() {
        if(size == 0){ //puvodne to bylo beginning == -1
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isFull() {
        if(size == array.length){ //puvodne to bylo size == capacity
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean enqueue(String obj) {
        if(isFull() || obj == null) {
            return false;
        }
        if(isEmpty()){
            beginning = 0;
        }
        end = (end + 1) % array.length; // puvodne end = (end + 1) % capacity
        array[end] = obj;
        size ++;
        return true;
    }

    @Override
    public String dequeue() {
        if(isEmpty()){
            return null;
        }
        String obj = array[beginning];
        array[beginning] = null;
        if (beginning == end) {
            beginning = -1;
            end = -1;
        } else {
            beginning = (beginning + 1) % array.length; //beginning = (beginning + 1) % capacity
        }
        size--;
        return obj;
    }

    @Override
    public void printAllElements() {
        if (isEmpty()) {
            System.out.println("Empty queue.");
            return;
        }
        int index = beginning;
        while (index != end) {
            System.out.println(array[index]);
            index = (index + 1) % capacity;
        }
        System.out.println(array[end]);
    }

}
