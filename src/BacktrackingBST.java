import java.util.NoSuchElementException;

public class BacktrackingBST implements Backtrack, ADTSet<BacktrackingBST.Node> {
    private Stack stack;
    private Stack redoStack;
    private BacktrackingBST.Node root = null;

    // Do not change the constructor's signature
    public BacktrackingBST(Stack stack, Stack redoStack) {
        this.stack = stack;
        this.redoStack = redoStack;
    }

    public Node getRoot() {
    	if (root == null) {
    		throw new NoSuchElementException("empty tree has no root");
    	}
        return root;
    }
	
    public Node search(int k) {
    	if(this.root==null)
     	   throw new IllegalArgumentException("Tree is empty.");
    	BacktrackingBST.Node current=this.root;
    	while(current!=null) {		//this is the search loop
    		if(current.key==k)		//if we found the key we return it
    			return current;
    		if(current.key<k)		//if the requested key is larger then the current node's key, go right
    			current=current.right;
    		else
	    		if(current.key>k)	//if the requested key is smaller then the current node's key, go left
	    			current=current.left;
    	}
    	return null;		//this is the default variable, if the key wasn't found
    }

    public void insert(Node node) {
    	boolean insert = false;
    	if(!redoStack.isEmpty()) {			//checks if 'insert' was called from 'retrack'
    		Object[] redo = (Object[])stack.pop();
    		if((int)redo[0]!=3)
    			redoStack.clear();
    	}
    	if(this.root==null) {	//if the tree is empty
    		this.root=node;
    		Object[] ins= {1,-1,-1,-1,node};  //{insert=1/delete=-1/delete with 2 sons=-2 , parent , left son , right son , node}
    		stack.push(ins);
    	}
    	else {
    		BacktrackingBST.Node current=this.root;
        	while(current.left!=null||current.right!=null) {	//as long as we're not at a leaf, the loop keeps going
        		if(current.key<node.key)
        			if(current.right!=null)		//if there's still a path to go in the needed direction, we keep going
        				current=current.right;	
        			else {						//else, we have reached the destination
        				current.right=node;
        				node.parent=current;
        				insert = true;			//we make a mark that the insertion had happened
        				break;					//we exit the loop
        			}
        		if(current.key>node.key)
        			if(current.left!=null)		//if there's still a path to go in the needed direction, we keep going
        				current=current.left;
        			else {						//else, we have reached the destination
        				current.left=node;
        				node.parent=current;
        				insert = true;			//we make a mark that the insertion had happened
        				break;					//we exit the loop
        			}
        	}		//the loop had ended
        	if(!insert) {				//if throughout the loop, we didn't insert the node (we reached a leaf)
	    		if(current.key<node.key) {		//we place it in it's fitting spot
	    			current.right=node;
	    			node.parent=current;
	    			
	    		}
	    		if(current.key>node.key) {
	    			current.left=node;
	    			node.parent=current;
	    		}
        	}
			Object[] ins= {1,current,-1,-1,node};		//we insert this for the undo stack, so we can later know which node to delete
			stack.push(ins);
    	}
        	
    }

    public void delete(Node node) {
    	BacktrackingBST.Node current=search(node.key);
    	if(current==null)	//either the tree is empty or the node isn't inside the tree
    		throw new IllegalArgumentException(); 
    	if(!redoStack.isEmpty()) {         //checks if 'delete' was called from 'retrack'
    		Object[] redo = (Object[])redoStack.pop();
    		if((int)redo[0]!=-3)
    			redoStack.clear();	
    	}
    	if(current.right==null) { 		//no right son
    		if(current.left==null) {    //no left son, node=leaf
    			
    			//if node=root (& node is a leaf)
    			if(this.root.key==node.key) {
    				this.root=null;
    				Object[] ins= {-1,-1,-1,-1,current};
    				stack.push(ins);
    			}
    			
    			//if node!=root (& node is a leaf)
    			else {
    				BacktrackingBST.Node current_par=current.parent;
    				if(current_par.right!=null && current_par.right==current)		//the leaf is a right son
    					current_par.right=null;
    				if(current_par.left!=null && current_par.left==current)		//the leaf is a left son
    					current_par.left=null;
    				Object [] ins= {-1,current_par,-1,-1,current};		//for the undo stack
    				stack.push(ins);
    			}
    		}
    		else {			//not a leaf (only LEFT son)
    			if(this.root.key==current.key) {	//node=root (only left sons)
    				this.root=current.left;
    				current.left.parent=null;
    				Object [] ins= {-1,-1,current.left,-1,current};
    				stack.push(ins);
    			}
    			else {			//node!=root (only left sons)
    				if(current.parent.right==current)		//if current was a right son
    					current.parent.right=current.left;
    				if(current.parent.left==current)		//if current was a left son
    					current.parent.left=current.left;
    				current.left.parent=current.parent;
    				Object [] ins= {-1,current.parent,current.left,-1,current};
    				stack.push(ins);
    			}
    		}
    	}
    	else {  	//there is a RIGHT son
    		if(current.left!=null) {		//THERE IS ALSO A LEFT SON
    			BacktrackingBST.Node replace=successor(current);   	//finds the minimal value at right_son_tree to replace 'current'
    			BacktrackingBST.Node replace_par=replace.parent;
    			if(replace.right!=null) {	//if REPLACE had a right son (can't have left son, minimum)
    				BacktrackingBST.Node replace_son=replace.right;
    				if(replace.parent.right!=null && replace.key==replace.parent.right.key) {   //if 'replace' was only one step to the right
    					replace_son.parent=replace_par;	
    					replace_par.right=replace_son;
    				}
    				else {     //we DID walk left
    					replace_par.left=replace_son;  		//since all we did was go left (minimum)
        				replace_son.parent=replace_par;		//now, we deleted 'replace' from the tree
    				}
    				Object [] ins= {-2,replace_par,-1,replace_son,replace};		//either way, we mark this delete proccess with -2, for the undo stack
    				stack.push(ins);
    			}
    			else {		//if REPLACE was a leaf
    				replace_par.left=null;
    				Object [] ins= {-2,replace_par,-1,-1,replace};
    				stack.push(ins);
    			}
    			replace.right=current.right;	//we match replace and current_right_son
    			current.right.parent=replace;
				current.left.parent=replace;
				replace.left=current.left;
    			if(this.root.key==current.key) {	//node=root
    				this.root=replace;
   					Object [] ins= {-2,-1,current.left,current.right,current};
   					stack.push(ins);
    				}
    			else {   //node!=root, had right son AND left son 
    					Object [] ins= {-2,current.parent,current.left,current.right,current};
    					stack.push(ins);
        				replace.parent=current.parent;
        				if(current.parent.left==current)	//node was a left son
        					current.parent.left=replace;
        				if(current.parent.right==current)	//node was a right son
        					current.parent.right=replace;

    				}
    			}
    			else {		//only RIGHT sons, NO LEFT
        			if(this.root.key==current.key) {	//node=root (only right sons)
        				this.root=current.right;
        				current.right.parent=null;
        				Object [] ins= {-1,-1,-1,current.right,current};
        				stack.push(ins);
        			}
        			else {			//node!=root (only right sons)
        				if(current.parent.right==current)		//if current was a right son
        					current.parent.right=current.right;
        				if(current.parent.left==current)		//if current was a left son
        					current.parent.left=current.right;
        				current.right.parent=current.parent;
        				Object [] ins= {-1,current.parent,-1,current.right,current};
        				stack.push(ins);
        			}
    			}
    		}
    }

    public Node minimum() {
       if(this.root==null)
    	   throw new IllegalArgumentException("Tree is empty.");
       BacktrackingBST.Node current=this.root;
		while(current.left!=null)		//to find the minimal value, we must go left until we reach a leaf (BST properties)
			current=current.left;
		return current;
    }

    public Node maximum() {
    	if(this.root==null)
    		throw new IllegalArgumentException("Tree is empty.");
 	   BacktrackingBST.Node current = this.root;
 	   while(current.right!=null)		//to find the maximal value, we must go right until we reach a leaf (BST properties)
 		   current = current.right;
        return current;
    }

    public Node successor(Node node) {
    	if(this.root==null || node.key==this.maximum().key)	//if we seek the next object after the maximum/the tree is empty
    		throw new IllegalArgumentException();
    	BacktrackingBST.Node current=search(node.key);		//first we find the node at the BST
    	if(current==null)		//if the node isn't in the tree to begin with, we throw an exception
    		throw new IllegalArgumentException();
    	if(current.right!=null) {		//if there's a right son:
    		current=current.right;		//we go one step to the right,
        	while(current.left!=null)	//then we go left until we find a leaf
        		current=current.left;
        	return current;
    	}
    	else {
    		BacktrackingBST.Node par=current.parent;	//if there isn't a right son, we must go up
    		while(par!=null && current.key==par.right.key) {	//we keep going until we find a key bigger then current (until current is a LEFT son)
    			current=current.parent;
    			par=par.parent;
    		}
    		return par;
    	}
    }

    public Node predecessor(Node node) {
    	if(this.root==null || node.key==this.minimum().key)	//if we seek the previous object before the minimum/the tree is empty
    		throw new IllegalArgumentException();
    	BacktrackingBST.Node current=search(node.key);		//first we find the node at the BST
    	if(current==null)		//if the node isn't in the tree to begin with, we throw an exception
    		throw new IllegalArgumentException();
    	if(current.left!=null) {			//if there's a left son:
    		current=current.left;			//we go one step to the left,
        	while(current.right!=null)		//then we go right until we find a leaf
        		current=current.right;
        	return current;
    	}
    	else {
    		BacktrackingBST.Node par=current.parent;	//if there isn't a left son, we must go up
    		while(par!=null && current.key==par.left.key) {	//we keep going until we find a key smaller then current (until current is a RIGHT son)
    			current=current.parent;
    			par=par.parent;
    		}
    		return par;
    	}
    }

    @Override
    public void backtrack() {
    	if(stack.isEmpty())
    		return;
    	 Object [] ins=(Object[]) stack.pop();
    	 redoStack.push(ins);			//we instantly push this to the re-do stack
    	 if((int)ins[0]==1) {  			//this was insert
    		 BacktrackingBST.Node current=(BacktrackingBST.Node) ins[4];
    		 delete(current);
    	 }
    	 if((int)ins[0]==-1) {  //this was delete, no RIGHT son OR no LEFT son
    		 BacktrackingBST.Node current = (BacktrackingBST.Node) ins[4];
    		 if(ins[2] instanceof BacktrackingBST.Node) {  		//had a LEFT son!!!
    			 BacktrackingBST.Node current_son=(BacktrackingBST.Node)ins[2];		//we find the left son in the tree
    			 if(current_son.parent!=null) {		//current_son!=root
    				 BacktrackingBST.Node current_par=current_son.parent;		//we find the father
    				 current.left=current_son;
    				 current_son.parent=current;
    				 current.parent=current_par;
    				 if(current_par.left==current_son)		//if current_son was a left son
    					 current_par.left=current;
    				 if(current_par.right==current_son)		//if current_son was a right son
    					 current_par.right=current;
    			 }
    			 else {		//currrent_son=root
    				 this.root=current;
    				 current_son.parent=current;
    				 current.left=current_son;
    			 }
    		 }  										//done with: had LEFT son, NO RIGHT son
    		 
    		 
    		 else if(ins[3] instanceof BacktrackingBST.Node) {  		//has RIGHT son!!!
    			 BacktrackingBST.Node current_son = (BacktrackingBST.Node) ins[3];		//we find the right son in the tree
    			 if(current_son.parent!=null) {		//current_son!=root
    				 BacktrackingBST.Node current_par=current_son.parent;		//we find the father
    				 current.right=current_son;
    				 current_son.parent=current;
    				 current.parent=current_par;
    				 if(current_par.left==current_son)		//if current_son was a left son
    					 current_par.left=current;
    				 if(current_par.right==current_son)		//if current_son was a right son
    					 current_par.right=current;
    			 }
    			 else {		//currrent_son=root
    				 this.root=current;
    				 current_son.parent=current;
    				 current.right=current_son;
    			 }
    		 }  										//done with: had left son, no right son
    		 
    		 else {		//didn't have a left son or right son= leaf!!
    			if(ins[1] instanceof BacktrackingBST.Node) { 	//leaf, but NOT the root!
    					 BacktrackingBST.Node current_par=(BacktrackingBST.Node) ins[1];	//we find the parent inside the tree
    					 current.parent=current_par;
    					 if(current_par.key<current.key)		//current is a left son
    						 current_par.right=current;
    					 if(current_par.key>current.key)		//current is a right son
    						 current_par.left=current;
    				 }
    				 else { 	//leaf, WAS the root!
    					 this.root = (BacktrackingBST.Node) ins[4];
    				 }
    			 }
    		 }
															//done with:no right son OR left son
    	 
    	 if((int)ins[0]==-2) {   	//this was delete, RIGHT SON AND LEFT SON
    		 BacktrackingBST.Node current=(BacktrackingBST.Node) ins[4];
    		 Object[]new_ins=(Object[]) stack.pop();
    		 BacktrackingBST.Node replace=(BacktrackingBST.Node) new_ins[4];
    		 BacktrackingBST.Node temp = null;
    		 if(this.root.key==replace.key)
    			 temp=this.getRoot();
    		 else if (current.parent.left!=null && current.parent.left.key==replace.key)
    			 temp = current.parent.left;   //temp=the one to replace 'current' ATM,left son 
    		 else
    			 temp = current.parent.right;	//temp=the one to replace 'current' ATM,right son 
    		 temp.right.parent=current;
    		 temp.left.parent=current;
    		 current.left=temp.left;
    		 current.right=temp.right;
    		 if(this.root.key==temp.key) {		//if temp was root
    			 this.root=current;
    			 current.parent=null;
    		 }
    		 else {								//temp was NOT a root
    			 current.parent=temp.parent;
    			 if(temp.parent.left==temp)
    				 temp.parent.left=current;
    			 if(temp.parent.right==temp)
    				 temp.parent.right=current;
    		 }
    		 				//we placed current back in it's place, now it's replace's time
    		 replace.parent=null;
    		 replace.right=null;
    		 replace.left=null;        //we reset replace's sons and parents so there won't be any mix ups
    		 if(new_ins[3] instanceof BacktrackingBST.Node) {	//replace had a RIGHT SON (can't have a left one)
    			 BacktrackingBST.Node replace_son=(BacktrackingBST.Node) new_ins[3];
    			 replace.parent=replace_son.parent;		//replace CANNOT be a root
    			 replace.right=replace_son;
    			 if(replace.key<replace.parent.key)      //we check if replace is a left or right son
	    			 replace_son.parent.left=replace;
    			 if(replace.key>replace.parent.key)
    				 replace_son.parent.right=replace;
			 	replace_son.parent=replace;
    			 
    		 }
    		 else {		//replace was a leaf
    			 BacktrackingBST.Node replace_par=(BacktrackingBST.Node) new_ins[1];
    			 replace_par.left=replace;
    			 replace.parent=replace_par;
    		 }
    	}
    }

    @Override
    public void retrack() {
    	if(redoStack.isEmpty())
    		return;
        Object[] ins = (Object[])(redoStack.pop());
        if((int)(ins[0])==1) {		//if we undid an insert
        	ins[0]=3;         	 //avoids 
        	redoStack.push(ins);		
        	this.insert((BacktrackingBST.Node)(ins[4]));	//we re-insert it
        }
        else {							//we undid a deletion
        	ins[0]=-3;
        	redoStack.push(ins);
        	this.delete((BacktrackingBST.Node)(ins[4]));	//we re-delete it
        }
    }


    @Override
    public void print() {
    	printPreOrder();
    }
    
    public void printPreOrder(){
    	String str="";
    	str = printPreOrder(this.root, str);	//we use a recursive function
    	System.out.println(str.substring(0,str.length()-1));	//we remove the last space bar
    }
    
    public String printPreOrder(BacktrackingBST.Node node, String str) {	//'helper' recursive function
    	str = str + node.key + " ";	//we insert current value first (with a space bar)- 'pre-order' method
    	if(node.left!=null)		//has left son
    		str = printPreOrder(node.left,str);
    	if(node.right!=null)	//has right son
    		str = printPreOrder(node.right,str);
    	return str;
    }
    
    public static class Node {
    	// These fields are public for grading purposes. By coding conventions and best practice they should be private.
        public BacktrackingBST.Node left;
        public BacktrackingBST.Node right;
        
        private BacktrackingBST.Node parent;
        private int key;
        private Object value;

        public Node(int key, Object value) {
            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }
        
    }

    public static void main(String[] args) {
    	Stack stack=new Stack();
    	Stack redoStack=new Stack();
    	BacktrackingBST tree=new BacktrackingBST(stack, redoStack);
    	Node node1= new Node(1,null);
    	Node node2= new Node(2,null);
    	Node node3= new Node(3,null);
    	Node node4= new Node(4,null);
    	Node node5= new Node(5,null);
    	Node node5b= new Node(5,null);
    	Node node6= new Node(6,null);
    	Node node7= new Node(7,null);
    	Node node8= new Node(8,null);
    	
    	tree.insert(node5);
    	tree.insert(node3);
    	tree.insert(node7);
    	tree.insert(node2);
    	tree.insert(node4);
    	tree.insert(node6);
    	tree.insert(node8);
    	tree.insert(node1);
    	tree.printPreOrder();
    }
}