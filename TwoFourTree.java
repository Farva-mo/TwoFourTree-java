//Mohammad Anisi
//COP 3503

// 2nd uploaded version of the TwoFourTree class.
public class TwoFourTree {
    /*
    this class is a two four tree implementation and is considered 1 node
    each node can carry up to three numbers, this makes it easier when adding, deleting or swaping numbers around
    all you have to do is update the values of the node and the tree will update itself.
    each node also contains a parent pointer which points its parent node, only the root has NULL as its parent.
    each node also contains 5 pointers which points to its children, the usage of this will vary based on the node type.
    if values is 1 left and right will be used
    if values is 2, left, centerChild, and right will be used
    if values is 3, left, centerLeft, centerRight, and right will be used
    if a node has children it is a non-leaf node and isleaf should be updated to false
     */
    private class TwoFourTreeItem {
        int values = 1;
        int value1 = 0; // always exists.
        int value2 = 0; // exists iff the node is a 3- node or 4-node.
        int value3 = 0; // exists iff the node is a 4- node.
        boolean isLeaf = true;
        TwoFourTreeItem parent = null; // parent exists iff the node is not root.
                TwoFourTreeItem leftChild = null; // left and right child exist iff the note is a non- leaf.
                TwoFourTreeItem rightChild = null;
        TwoFourTreeItem centerChild = null; // center child exists iff the node is a non-leaf 3- node.
                TwoFourTreeItem centerLeftChild = null; // center-left and center-right children exist iff the node is a non-leaf 4- node.
                TwoFourTreeItem centerRightChild = null;

        public boolean isTwoNode() {
            if(values == 1){
                return true;
            }
            return false;
        }

        public boolean isThreeNode() {
            if(values == 2){
                return true;
            }
            return false;
        }

        public boolean isFourNode() {
            if(values == 3){
                return true;
            }
            return false;
        }

        public boolean isRoot() { ///
            if(parent == null){
                return true;
            }
            return false;
        }

        public TwoFourTreeItem(int value1) {
            this.value1 = value1;
        }

        public TwoFourTreeItem(int value1, int value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        public TwoFourTreeItem(int value1, int value2, int value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        private void printIndents(int indent) {
            for (int i = 0; i < indent; i++) System.out.printf(" ");
        }

        public void printInOrder(int indent) {
            if (!isLeaf) leftChild.printInOrder(indent + 1);
            printIndents(indent);
            System.out.printf("%d\n", value1);
            if (isThreeNode()) {
                if (!isLeaf) centerChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
            } else if (isFourNode()) {
                if (!isLeaf) centerLeftChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
                if (!isLeaf) centerRightChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value3);
            }
            if (!isLeaf) rightChild.printInOrder(indent + 1);
        }
    }

    //Initializes the tree to null
    TwoFourTreeItem root = null;

    // this function adds a new number into the tree
    // if the tree is empty, create a new node and make it the root
    // if the tree is not empty, traverse down the tree to find the leaf node
    // if it comes across a values 3 node, it will split the node and perform the correct actions to change the tree
    public boolean addValue(int value) {
        if (root == null) {
            root = new TwoFourTreeItem(value);
            return true;
        }
        if (root.values == 3) {

            // System.out.println(" the root values are" + root.value1+" "+root.value2+" "+root.value3);

            TwoFourTreeItem newRoot = new TwoFourTreeItem(root.value2);
            TwoFourTreeItem newLeft = new TwoFourTreeItem(root.value1);
            TwoFourTreeItem newRight = new TwoFourTreeItem(root.value3);

            //fitting in the leftChild and moving pointers around in correct spots
            newRoot.leftChild = newLeft;
            newLeft.parent = newRoot;
            newLeft.leftChild = root.leftChild;
            newLeft.rightChild = root.centerLeftChild;

            //fitting in the RightChild and moving pointers around in correct spots
            newRoot.rightChild = newRight;
            newRight.parent = newRoot;
            newRight.leftChild = root.centerRightChild;
            newRight.rightChild = root.rightChild;

            if (root.leftChild != null) {
                root.leftChild.parent = newLeft;
            }
            if (root.rightChild != null) {
                root.rightChild.parent = newRight;
            }

            if (root.centerLeftChild != null) {
                root.centerLeftChild.parent = newLeft;
            }
            if (root.centerRightChild != null) {
                root.centerRightChild.parent = newRight;
            }

            if (newLeft.leftChild != null || newLeft.rightChild != null) {
                newLeft.isLeaf = false;
            }
            if (newRight.leftChild != null || newRight.rightChild != null) {
                newRight.isLeaf = false;
            }

            //changing the root and making sure it's labeled as not a leaf.
            root = newRoot;
            root.isLeaf = false;
        }




        TwoFourTreeItem tracker = root;

        //purpose is to go down the tree to find the leaf node
        //while going down move around what you need to such as:
        //when reaching a values 3 node.

        int loops =0;

            while (!tracker.isLeaf) {
                if (tracker.values == 3) { // Split
                    // have a function that can handle 4 leaf node and update it
                    tracker = threeNodeSplit(tracker, value);
                }
                //check leaf values is 2
                // inside move around and add nodes
                // if so move accordingly
                else if (tracker.values == 2) { // compare left, middle, right

                    if (tracker.value1 > value) {

                        tracker = tracker.leftChild; //traverse left


                    } else if (value > tracker.value1 && value < tracker.value2) { //traverse down the center

                        tracker = tracker.centerChild;
                    } else if (value > tracker.value1 && value > tracker.value2) { /// updated with else if

                        tracker = tracker.rightChild; //else traverse down the right
                    }
                }
                //check if leaf values is 1
                //if so move accordingly
               else if (tracker.values == 1) { // compare left and right
                    if (tracker.value1 > value) {
                        tracker = tracker.leftChild; //traverse left

                    } else {
                        tracker = tracker.rightChild; //traverse right
                    }
                }
            }


        //once you reach a leaf node, check if values is either 1,2, or 3
        //add the value into node accordingly
        //if it's a 4-leaf node move pointers and nodes around,
        // use a function to save time,space, and errors
        if(tracker.values == 1){

            if(tracker.value1 < value){
                tracker.value2 = value;
                tracker.values= 2;
               // System.out.println("Inserted: " + value+"\n");

                return true;

            }else{ //if tracker.value1 is greater than value, swap around
                tracker.value2 = tracker.value1;
                tracker.value1 = value;
                tracker.values= 2;
               // System.out.println("Inserted: " + value+"\n");
                return true;
            }
        }else if(tracker.values == 2){
            // have to compare to values1,2 to value
            // and update tracker.values to 3
            if(value < tracker.value1){
                tracker.value3 = tracker.value2;
                tracker.value2 = tracker.value1;
                tracker.value1 = value;
                tracker.values = 3;
               // System.out.println("Inserted: " + value+"\n");
                return true;
            }else if(value > tracker.value1 && value < tracker.value2){
                tracker.value3 = tracker.value2;
                tracker.value2 = value;
                tracker.values = 3;
              //  System.out.println("Inserted: " + value+"\n");
                return true;
            }else{
                tracker.value3 = value;
                tracker.values = 3;
              //  System.out.println("Inserted: " + value+"\n");
                return true;
            }
        }else if(tracker.values == 3){


            tracker = threeNodeSplit(tracker, value);

            // tracker should be in correct node

            if(tracker.values == 1){

                if(tracker.value1 < value){
                    tracker.value2 = value;
                    tracker.values= 2;
                  //  System.out.println("Inserted: " + value+"\n");
                    return true;

                }else{ //if tracker.value1 is greater than value, swap around
                    tracker.value2 = tracker.value1;
                    tracker.value1 = value;
                    tracker.values= 2;
                   // System.out.println("Inserted: " + value+"\n");
                    return true;
                }
            }else if(tracker.values == 2){
                // have to compare to values1,2 to value
                // and update tracker.values to 3
                if(value < tracker.value1){
                    tracker.value3 = tracker.value2;
                    tracker.value2 = tracker.value1;
                    tracker.value1 = value;
                    tracker.values = 3;
                  //  System.out.println("Inserted: " + value+"\n");
                    return true;
                }else if(value > tracker.value1 && value < tracker.value2){
                    tracker.value3 = tracker.value2;
                    tracker.value2 = value;
                    tracker.values = 3;
                  //  System.out.println("Inserted: " + value+"\n");
                    return true;
                }else{
                    tracker.value3 = value;
                    tracker.values = 3;
                   // System.out.println("Inserted: " + value+"\n");
                    return true;
                }
            }
        }
      //  System.out.println("Insert failed for value: " + value +"\n");
        return false;
    }


    public boolean hasValue(int value) {

        if(root == null){
            return false;
        }
       // System.out.println("hasValue number is: " + value + "\n");
        TwoFourTreeItem walker = root;
        while(!walker.isLeaf){
            if(value == walker.value1 ||value == walker.value2 ||value == walker.value3){
            return true;
            }
            //traversing through 4 leaf node tree
            else if(walker.values == 3){
                if(value < walker.value1){
                    walker = walker.leftChild;
                }
                else if(value > walker.value1 && value < walker.value2){
                    walker = walker.centerLeftChild;
                }
               else if(value > walker.value2 && value < walker.value3){
                    walker = walker.centerRightChild;
                }
                else if(value > walker.value3) {
                    walker = walker.rightChild;
                }

            }
            //traversing through 3 leaf node tree
            else if(walker.values == 2){
                if(value < walker.value1){
                    walker = walker.leftChild;
                }
                else if(value > walker.value1 && value < walker.value2){
                    walker = walker.centerChild;
                }
                else if(value > walker.value2){
                    walker = walker.rightChild;
                }
            }
            //traversing through 2 leaf node tree
            else if(walker.values == 1){
                if(value < walker.value1){
                    walker = walker.leftChild;
                }
                else if(value > walker.value1){
                    walker = walker.rightChild;
                }
            }
        }
       // return false; returns true or false if
        return (value == walker.value1 ||value == walker.value2 ||value == walker.value3);
    }

    //supposed to delete the value from the tree,
    //could not finish due to personal reasons.
    public boolean deleteValue(int value) {
//        if(root == null){
//            return false;
//        }
//        TwoFourTreeItem walker = root;
//
//        while (!walker.isLeaf) {
//            if(walker.values == 1){
//
//            }
//            if(walker.values == 2){
//
//            }
//            if (walker.values == 3){
//            }
//        }

        return false;
    }

    public void printInOrder() {
        if (root != null) root.printInOrder(0);
    }

    public TwoFourTree() {
    }

    public TwoFourTreeItem threeNodeSplit(TwoFourTreeItem payLoad, int value){
        //find if parent is 2 or 1 values
        //then compare values of parent
        //if parent values is 1
            //if middle child is now left then move accordingly
            //if middle child is now right then move accordingly
        /// above should be done
        //if parent values in 2
            //if middle child is now in left then move accordingly
            //if middle child is now middle then move accordingly
            //if middle child is now right then move accordingly
        //make sure parent pointers are updated accordingly
        /// find where value would go, and either traverse it or add
        /// value does have to be added, but traverse it properly
        /// after split has happened, nodes and pointers are set
        /// compare value with previous payLoad.value2
        /// it's only going 1 of 2 directions, left or right of that (could be center or centerRight as exp...)
        /// return the pointer that it's going to traverse to
        //pointer for parent of payload

        TwoFourTreeItem parentPointer = payLoad.parent;

        TwoFourTreeItem tempError;

        if(parentPointer.values == 1){
            if(payLoad.value2 < parentPointer.value1){ // if middle child less than parent

                parentPointer.value2=parentPointer.value1;
                parentPointer.value1= payLoad.value2;
                parentPointer.values = 2;

                //right child stays

                //create New nodes for payload value1 & 2
                TwoFourTreeItem leftNewNode = new TwoFourTreeItem(payLoad.value1);

                //have parent pointer center child be payload
                parentPointer.centerChild = payLoad;

                //pointer for parentPointer->leftChild is updated and, leftNewNode->parent points to parentPointer
                parentPointer.leftChild = leftNewNode;
                leftNewNode.parent=parentPointer;

                ChilrenSetter(leftNewNode,payLoad);

              /*tempError = findingCorrectTraversalAfterSplit(leftNewNode, payLoad, payLoad.value1, value);
                */

                tempError = findingCorrectTraversalAfterSplit(leftNewNode, payLoad, parentPointer.value1, value);

                if (tempError == null){
                    System.out.println("Error 1");
                    return null;
                }else return tempError;

            }

            //same concept as above, just values and pointers in different spots
            //payload.value2 is greater than parentPointer.value1, it moves to parentPointer.value2 spot
            else if(payLoad.value2 > parentPointer.value1){

                parentPointer.value2 = payLoad.value2;
                parentPointer.values = 2;

                //create node for parentPointer.centerChild, and move pointers around
                TwoFourTreeItem newCenterChild = new TwoFourTreeItem(payLoad.value1);
                newCenterChild.parent = parentPointer;
                parentPointer.centerChild = newCenterChild;
                //payload is the right child so it doesn't need to be moved
                //payload.parent is still in the right spot

                ChilrenSetter(newCenterChild,payLoad);

                tempError = findingCorrectTraversalAfterSplit(newCenterChild, payLoad, parentPointer.value2, value);

                if (tempError == null){
                    System.out.println("Error 2");
                    return null;
                }
                else return tempError;
            }

        }
        else if(payLoad.parent.values == 2){
            if(payLoad.value2 < parentPointer.value1){ // if middle child less than value one

                //moving values around
                parentPointer.value3= parentPointer.value2;
                parentPointer.value2 = parentPointer.value1;
                parentPointer.value1 = payLoad.value2;
                parentPointer.values = 3;

                //now move parentPointer children pointers around
                //use if statements to check if the children are null
                if(parentPointer.centerChild != null){
                    parentPointer.centerRightChild= parentPointer.centerChild;
                    parentPointer.centerChild=null;
                }
                //right child stays the same.

                //move payLoad to from left to centerLeft
                parentPointer.centerLeftChild = payLoad;

                //now make a new node for left child
                //set up its parent and parentPointer.left
                TwoFourTreeItem newLeft = new TwoFourTreeItem(payLoad.value1);
                newLeft.parent = parentPointer;
                parentPointer.leftChild = newLeft;

                //can i make a function that does the rest of the work?
                //purpose is to move children of payload around
                ChilrenSetter(newLeft,payLoad);

                tempError = findingCorrectTraversalAfterSplit(newLeft, payLoad, parentPointer.value1, value);

                if (tempError == null){
                    System.out.println("Error 3");
                    return null;
                }
                else return tempError;

               // return findingCorrectTraversalAfterSplit(newLeft, payLoad, parentPointer.value1, value);

            }
            else if(payLoad.value2 > parentPointer.value1 && payLoad.value2 < parentPointer.value2){

                //moving values around
                parentPointer.value3 = parentPointer.value2;
                parentPointer.value2 = payLoad.value2;
                parentPointer.values =3;

                //move payLoad pointer from center to center right
                parentPointer.centerRightChild = payLoad;
                parentPointer.centerChild = null;

                //create new node for CenterLeftNode
                //set pointers for parentPointer and newCenterLeftNode
                TwoFourTreeItem newCenterLeftNode = new TwoFourTreeItem(payLoad.value1);
                newCenterLeftNode.parent = parentPointer;
                parentPointer.centerLeftChild = newCenterLeftNode;

                //sets children in correct spots, hopefully
                ChilrenSetter(newCenterLeftNode,payLoad);

              //  tempError = findingCorrectTraversalAfterSplit(newCenterLeftNode, payLoad, payLoad.value2, value);

                /// updated
                tempError = findingCorrectTraversalAfterSplit(newCenterLeftNode, payLoad, parentPointer.value2, value);

                if (tempError == null){
                    System.out.println("Error 4");
                    return null;
                }
                else return tempError;

                /*return findingCorrectTraversalAfterSplit(newCenterLeftNode, payLoad, payLoad.value2, value);
                 */
            }
            else if(payLoad.value2 > parentPointer.value2){

                parentPointer.value3 = payLoad.value2;
                parentPointer.values =3;

                //set center to centerLeft of parentPointer
                parentPointer.centerLeftChild = parentPointer.centerChild;
                parentPointer.centerChild = null;

                //create a new node for CenterRight and set pointers
                TwoFourTreeItem newCenterRightNode = new TwoFourTreeItem(payLoad.value1);
                newCenterRightNode.parent = parentPointer;
                parentPointer.centerRightChild = newCenterRightNode;

                //set children
                ChilrenSetter(newCenterRightNode,payLoad);

                tempError = findingCorrectTraversalAfterSplit(newCenterRightNode, payLoad, parentPointer.value3, value);

                if(tempError == null){
                    System.out.println("Error 5");
                    return null;
                }

                else return tempError;
              //  return findingCorrectTraversalAfterSplit(newCenterRightNode, payLoad, parentPointer.value3, value);
            }
        }

            System.out.println("PayLoad is null");
        return null;
    }

    //purpose is to put children of payload in the correct spot
    public boolean ChilrenSetter(TwoFourTreeItem newLeftNode,TwoFourTreeItem payLoad){

        //System.out.println("ChilrenSetter called");
        if(newLeftNode == null){
            System.out.println("newLeftNode is null");
            return false;
        }
        if(payLoad == null){
            System.out.println("payLoad is null");
            return false;
        }
        //first place newLeftNode children
        //move left child of payload to newLeft
        //set the parents of children as well
        if(payLoad.leftChild != null){
            newLeftNode.leftChild = payLoad.leftChild;
            newLeftNode.leftChild.parent = newLeftNode;
        }

        if(payLoad.centerLeftChild != null){
            newLeftNode.rightChild = payLoad.centerLeftChild;
            newLeftNode.rightChild.parent = newLeftNode;
        }

        //now move payLoad children
        payLoad.leftChild = payLoad.centerRightChild;
        payLoad.centerLeftChild = null;
        payLoad.centerRightChild = null;

        // right doesn't need to be moved, it's in its correct spot
        payLoad.value1= payLoad.value3;
        payLoad.value2=0;
        payLoad.value3=0;
        payLoad.values=1;

        if(newLeftNode.leftChild != null || newLeftNode.rightChild != null){
            newLeftNode.isLeaf = false;
        }else{
            newLeftNode.isLeaf = true;
        }

        if(payLoad.leftChild != null || payLoad.rightChild != null){
            payLoad.isLeaf = false;
        }else{
            payLoad.isLeaf = true;
        }
        return true;
    }

    //after the split I need to know where im going to assign tracker in the addValue function
    public TwoFourTreeItem findingCorrectTraversalAfterSplit
            (TwoFourTreeItem left,TwoFourTreeItem right, int previousMiddle, int value){



        // previousMiddle is value2 of 3 node that's being split up and going up
        // if value is less than previousMiddle i want to return left so now tracker can point to that
        if(value < previousMiddle){
            if(left == null){
                System.out.println("Left is null");
            }
            return left;
        }
        // if value is greater than previousMiddle i want to return right so now tracker can point to that
        if(value > previousMiddle){
            if(right == null){
                System.out.println("Right is null");
            }
            return right;
        }
        return null; // assuming for duplicates but i think i dont have to worry for that
    }
}