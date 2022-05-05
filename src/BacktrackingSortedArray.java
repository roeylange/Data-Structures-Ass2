

public class BacktrackingSortedArray implements Array<Integer>, Backtrack {
    private Stack stack;
    public int[] arr; // This field is public for grading purposes. By coding conventions and best practice it should be private.
    int lastPlace;
    // TODO: implement your code here

    // Do not change the constructor's signature
    public BacktrackingSortedArray(Stack stack, int size) {
        this.stack = stack;
        arr = new int[size];
        lastPlace=0;
    }
    
    @Override
    public Integer get(int index){
        // TODO: implement your code here
    	if(index<0||index>arr.length)
    		throw new IllegalArgumentException();
    	return arr[index];
    }

    @Override
    public Integer search(int k) {
        // TODO: implement your code here
    	int low=0,high=lastPlace-1,mid=(high+low)/2;
    	while(low<=high) {
    		if(k==arr[mid])
    			return mid;
    		if(k<arr[mid])
    			high=mid;
    		if(k>arr[mid])
    			low=mid;
    	}
    	return -1;
    }

    @Override
    public void insert(Integer x) {
        // TODO: implement your code here
    	if(lastPlace==0) {
    		arr[0]=x;
    		int [] ins= {1,0,x};
    		stack.push(ins);
    	}
    	else {
    		int low=0,high=lastPlace-1,mid=(low+high)/2;
    		if(x>arr[high]) {
    			arr[high+1]=x;
        		int [] ins= {1,high+1,x};
        		stack.push(ins);
    		}
    		else if(x<arr[0]) {
    			for(int i=lastPlace;i>0;i--) {
    				arr[i]=arr[i-1];
            		int [] ins= {1,0,x};
            		stack.push(ins);
    			}
    			arr[0]=x;
    		}
    		else {
    			while((high-low)!=1) {
    				if(x>arr[mid])
    					low=mid;
    				if(x<arr[mid])
    					high=mid;
    				mid=(high+low)/2;
    			}
    			for(int i=lastPlace;i>high;i--) {
    				arr[i]=arr[i-1];
    			}
    			arr[high]=x;
        		int [] ins= {1,high,x};
        		stack.push(ins);
    		}
    	}
    	lastPlace++;
    }

    @Override
    public void delete(Integer index) {
        // TODO: implement your code here
    }

    @Override
    public Integer minimum() {
        // TODO: implement your code here
    	return null; // temporal return command to prevent compilation error
    }

    @Override
    public Integer maximum() {
        // TODO: implement your code here
    	return null; // temporal return command to prevent compilation error
    }

    @Override
    public Integer successor(Integer index) {
        // TODO: implement your code here
    	return null; // temporal return command to prevent compilation error
    }

    @Override
    public Integer predecessor(Integer index) {
        // TODO: implement your code here
    	return null; // temporal return command to prevent compilation error
    }

    @Override
    public void backtrack() {
        // TODO: implement your code here
    }

    @Override
    public void retrack() {
		/////////////////////////////////////
		// Do not implement anything here! //
		/////////////////////////////////////
    }

    @Override
    public void print() {
        // TODO: implement your code here
    }
    
}
