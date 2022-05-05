

import java.util.NoSuchElementException;

public class Stack {
    private StackElement head = null;

    private static class StackElement {
        Object value;
        StackElement next;

        StackElement(Object value)
        {
            this.value = value;
        }
    }

    public boolean isEmpty(){
        return head == null;
    }

    public void push(Object value){
        StackElement newElement = new StackElement(value);
        newElement.next = head;
        head = newElement;
    }

    public Object pop(){
    	if (isEmpty()) {
    		throw new NoSuchElementException("cannot pop from an empty stack");
    	}
        StackElement returnedValue = head;
        head = returnedValue.next;
        return returnedValue.value;
    }

    public void clear(){
        head = null;
    }
    
}
