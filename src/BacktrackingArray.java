public class BacktrackingArray implements Array<Integer>, Backtrack {
    private Stack stack;
    private int[] arr;
    // TODO: implement your code here
    private int lastPlace;		//this would help us know where the current last cell in use is

    // Do not change the constructor's signature
    public BacktrackingArray(Stack stack, int size) {
        this.stack = stack;
        arr = new int[size];
        lastPlace=0;	//we also set the last place to 0
    }

    @Override
    public Integer get(int index){
        // TODO: implement your code here
    	if(index<0||index>arr.length)	//if the index<0 or the index>length of the array, we throw an exception
    		throw new IllegalArgumentException();
    	return arr[index];
    }

    @Override
    public Integer search(int k) {
        // TODO: implement your code here
    	int index=0;
    	while(index<lastPlace) {		//regular, linear search (array is not sorted)
    		if(arr[index]==k)
    			return index;
    		index++;
    	}
    	return -1;
    }

    @Override
    public void insert(Integer x) {
        // TODO: implement your code here
    	if(lastPlace==arr.length)
    		throw new RuntimeException("Data structure overflow");
    	arr[lastPlace]=x;			//here we use the lastPlace as a pointer, regular insertion to the last place in the array
    	int [] ins= {1,lastPlace,x};
    	stack.push(ins);			//for the undo stack, {insert= 1,index,value}
    	lastPlace++;				//we move lastPlace one step ahead, as a pointer to the next free cell
    }

    @Override
    public void delete(Integer index) {
        // TODO: implement your code here
    	if(index<0 || index>arr.length)
    		throw new IllegalArgumentException("Index out of bounds");
    	if(index==lastPlace-1) {	//we delete the last cell
        	int [] ins= {-1,index,arr[index]};	//for the undo stack, {delete= -1,index,value}
        	stack.push(ins);
    		lastPlace--;
    	}
    	else {		//we delete a cell that's not at last place
        	int [] ins= {-2,index,arr[index]};	//for the undo stack, {delete= -1,index,value}
        	int	[] new_ins= {-2,lastPlace-1,arr[lastPlace-1]};
        	stack.push(ins);
        	stack.push(new_ins);
    		arr[index]=arr[lastPlace-1];
    		lastPlace--;
    		}
    	}

    @Override
    public Integer minimum() {
        // TODO: implement your code here
    	if(lastPlace==0)
    		throw new IllegalArgumentException("Array is empty.");
    	int index=0;              //we set the minimum as the first item in the array
		for(int i=1;i<lastPlace;i++) {	//regular linear search
			if(arr[i]<arr[index])
				index=i;
		}
    	return index;
    }

    @Override
    public Integer maximum() {
        // TODO: implement your code here
    	if(lastPlace==0)
    		throw new IllegalArgumentException("Array is empty.");
    	int index=0;     //we set the minimum as the first item in the array
		for(int i=1;i<lastPlace;i++) {	//regular linear search
			if(arr[i]>arr[index])
				index=i;
		}
    	return index;
    }

    @Override
    public Integer successor(Integer index) {
        // TODO: implement your code here
    	if(index<0 || index >=lastPlace) 
        	throw new IllegalArgumentException("Index out of bounds");
    	if(arr[index]==this.maximum())
    		throw new IllegalArgumentException("This index has no successor");
    	int min=0;
    	while(arr[min]<=arr[index])		//we find the first item that's bigger then arr[index]
    		min++;
    	for(int i=min+1;i<lastPlace;i++) {		//linear search from the first item that's bigger then arr[index] until the last place
    		if(arr[i]>arr[index]&&arr[i]<arr[min])	//if the current cell is both bigger then arr[index] and smaller then arr[min]
    				min=i;
    	}
    	return min;
    }

    @Override
    public Integer predecessor(Integer index) {
        // TODO: implement your code here
    	if(index<0 || index >=lastPlace) 
        	throw new IllegalArgumentException("Index out of bounds");
    	if(arr[index]==this.minimum())
    		throw new IllegalArgumentException("This index has no predecessor");
    	int min=0;
    	while(arr[min]>=arr[index])		//we find the first item that's smaller then arr[index]
    		min++;
    	for(int i=min+1;i<lastPlace;i++) {//linear search from the first item that's smaller then arr[index] until the last place
    		if(arr[i]<arr[index]&&arr[i]>arr[min])		//if the current cell is both smaller then arr[index] and bigger then arr[min]
    				min=i;
    		}
    	return min;
    	}

    @Override
    public void backtrack() {
        if(!stack.isEmpty()) {
        	int[] lastAction = (int[])(stack.pop());
        	if(lastAction[0]==1)         //if last action was insertion
        		this.delete(lastAction[1]);
        	else if(lastAction[0]==-1) {						//if last action was delete, -1
        		arr[lastPlace]=lastAction[2];
        		lastPlace++;
    		}
        	else{		//delete was last place, -2
        		arr[lastPlace]=lastAction[2];
        		int[] secAction = (int[])(stack.pop());
        		arr[secAction[1]]=secAction[2];
        		lastPlace++;
        	}
        }
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
    	for(int i=0;i<lastPlace-1;i++) {		//regular array print
    		System.out.print(arr[i]+" ");
    	}
		System.out.print(arr[lastPlace-1]);
    }
    
    public static void main(String[] args) {
    	Stack st1 = new Stack();
    	BacktrackingArray a = new BacktrackingArray(st1,10);
    	a.insert(5);
    	a.insert(1);
    	a.insert(9);
    	a.insert(13);
    	a.insert(4);
    	a.print();
    	System.out.println();
    	a.delete(1);
    	a.print();
    	System.out.println();
    	a.delete(3);
    	a.print();
    	System.out.println();
    	a.backtrack();
    	a.print();
    	System.out.println();
    	a.backtrack();
    	a.print();
    }
    
    
}