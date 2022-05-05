

public class Warmup {
    public static int backtrackingSearch(int[] arr, int x, int forward, int back, Stack myStack) {
        // TODO: implement your code here
    	if(back>=forward)
    		throw new IllegalArgumentException("back isn't smaller then forward!");
    	int index=0;
    	while(index<arr.length) {
    		for(int i=0;i<forward&&index<arr.length;i++) {
				myStack.push(arr[index]);
    			if (arr[index]==x) {
    				return index;
    			}
    			else {
    				System.out.println(index);
    				index++;
    			}
    		}
    		for(int i=0;i<back&&index>=0;i++) {
    			System.out.println(index);
    			myStack.pop();
    			index--;
    		}
    	}
    	return -1; // temporal return command to prevent compilation error 
    }

    public static int consistentBinSearch(int[] arr, int x, Stack myStack) {
        // TODO: implement your code here
    	
    	// Your implementation should contain a this line:
    	int inconsistencies = Consistency.isConsistent(arr);
    	int answer= consistentBinSearch(arr,x,myStack,0,arr.length-1);
    	return answer;
    }
    
    public static int consistentBinSearch(int[] arr, int x, Stack myStack, int low, int high) {
    	int ans=-1;
    	int inconsistencies= Consistency.isConsistent(arr);
    	if(inconsistencies!=0)
    		return(-2);
    	if(low<=high) {
    		int mid=(low+high)/2;
			myStack.push(arr[mid]);
    		if(x==arr[mid])
    			return mid;
    		else {
    			while(x<arr[mid]) {
    				ans= consistentBinSearch(arr,x,myStack,low,mid);
    				if(ans>=-1)
    					return ans;
    				else{
    					inconsistencies= Consistency.isConsistent(arr);
    					if(inconsistencies!=0) {
    						myStack.pop();
    						return (-2);
    					}
    				}
    			}
    			while(x>arr[mid]) {
    				ans= consistentBinSearch(arr,x,myStack,mid,high);
    				if(ans>=-1)
    					return ans;
    				else {
    					inconsistencies= Consistency.isConsistent(arr);
    					if(inconsistencies!=0) {
    						myStack.pop();
    						return (-2);
						}
    				}
    			}
    		}
    	}
    	return -1;
    }
 	public static void main(String args[]) {
 		int [] array= {1,7,9,15,4,2,8};
 		Stack myStack=new Stack();
 		System.out.println(backtrackingSearch(array,2,3,2,myStack));
 	}
}
