
import java.io.*;
import java.util.Scanner;
public class Guesser {
    public static File f = new File("Guesser.txt"); //creates new file infolder to store object state
    BinaryTree<String> starter = new BinaryTree<String>(); //create new empty tree
    public Guesser() throws IOException, ClassNotFoundException {
        Scanner myObj = new Scanner(System.in); // Create a Scanner object
        System.out.println("Start a new Game or Load?(S/L)");
        String decision = myObj.nextLine(); // Read user input
        if (decision.equals("S")){
            initTree(starter);//populate new tree with initial tree
            //starter.inorderTraverse(); //test to see if tree is structured correct
            game(starter);
        }
        else if (decision.equals("L")){
            starter = loadTree();
            game(starter);
        }
    }
    // constructs initial tree in diagram
    //Also sorry that its so long, I didn't realise 5 levels would be this much :)
    public static void initTree(BinaryTree<String> start){
        //Refer to diagram for explanation
        BinaryTree<String> dog = new BinaryTree<>("Is it a dog?");
        BinaryTree<String> dq = new BinaryTree<>("This animal does not exist");
        BinaryTree<String> Legs4 = new BinaryTree<>("Does it have 4 legs?", dog, dq);
        BinaryTree<String> human = new BinaryTree<>("Is it a human?");
        BinaryTree<String> ape = new BinaryTree<>("Is it an ape");
        BinaryTree<String> talk = new BinaryTree<>("Can it talk?", human, ape);
        BinaryTree<String> hawk = new BinaryTree<>("Is it a Hawk?");
        BinaryTree<String> Penguin = new BinaryTree<>("Is it a Penguin?");
        BinaryTree<String> fly = new BinaryTree<>("Can it fly?", hawk, Penguin);
        BinaryTree<String> cockroaches = new BinaryTree<>("Is it a cockroach");
        BinaryTree<String> lizard = new BinaryTree<>("Is it a lizard");
        BinaryTree<String> insect = new BinaryTree<>("Is it an insect?", cockroaches, lizard);
        BinaryTree<String> legs2 = new BinaryTree<>("Does it have more than two legs?", Legs4, talk);
        BinaryTree<String> bird = new BinaryTree<>("Is it a bird?", fly, insect);
        BinaryTree<String> mammal = new BinaryTree<>("Is it a Mammal?", legs2, bird);
        BinaryTree<String> spiderMan = new BinaryTree<>("Is it spiderman?");
        BinaryTree<String> tedBundy = new BinaryTree<>("Is it ted Bundy?");
        BinaryTree<String> fantasy = new BinaryTree<>("Is it fantasy?",spiderMan, tedBundy);
        BinaryTree<String> zeus = new BinaryTree<>("Is it zeus?");
        BinaryTree<String> caesar = new BinaryTree<>("Is it Julius Caesar?");
        BinaryTree<String> mythology = new BinaryTree<>("Is it rooted in mythology?", zeus, caesar);
        BinaryTree<String> tvShow = new BinaryTree<>("Is it in a tv show?", fantasy, mythology);
        BinaryTree<String> leoD = new BinaryTree<>("Is it Leonardo Di Caprio?");
        BinaryTree<String> messi = new BinaryTree<>("Is it Leo Messi?");
        BinaryTree<String> actor = new BinaryTree<>("Is it an actor?",leoD, messi );
        BinaryTree<String> dad = new BinaryTree<>("Is it your dad?");
        BinaryTree<String> bsf = new BinaryTree<>("Is it your best friend?");
        BinaryTree<String> family = new BinaryTree<>("Are they your family?", dad, bsf);
        BinaryTree<String> famous = new BinaryTree<>("Are they famous?", actor, family);
        BinaryTree<String> real = new BinaryTree<>("Is this person real?", famous, tvShow);
        start.setTree("Is it an animal?(Human(Non-specific person counts as this))", mammal, real);
    }
    public static void game(BinaryTree<String> starter) throws IOException, ClassNotFoundException {
        while(true){
            BinaryNodeInterface<String> currentNode = starter.getRootNode();//initialise current node to root node
            while (! currentNode.isLeaf()){
                Scanner myObj1 = new Scanner(System.in); // Create a Scanner object
                System.out.println(currentNode.getData());//print out question contained in node
                String answer = myObj1.nextLine();
                if (answer.equals("Yes") || answer.equals("yes")){
                    currentNode = currentNode.getLeftChild();//if the user puts yes, the currentnode gets updated to the leftchild of itself
                }
                else if (answer.equals("No") || answer.equals("no")){
                    currentNode = currentNode.getRightChild(); //if the user puts no, the currentnode gets updated to its rightchild
                }
            }
            while (currentNode.isLeaf()) {
                Scanner myObj1 = new Scanner(System.in); // Create a Scanner object
                System.out.println(currentNode.getData());
                String answer = myObj1.nextLine();
                if (answer.equals("Yes") || answer.equals("yes")) {//if guess is correct, user is prompted for continue options
                    Scanner myObj2 = new Scanner(System.in);
                    System.out.println("Tree Guessed Correct!!" + ", Would you Like to Play Again(P), save this tree(S), load another tree(L), or Exit Game(Q)");
                    String choice = myObj2.nextLine();
                    switch (choice) {
                        case "P" -> game(starter);//restarts the game
                        case "S" -> {
                            saveTree(starter);//saves the tree
                            System.exit(0);//exits the program
                        }
                        case "L" -> {
                            starter = loadTree();//makes starter tree = pre saved tree
                            game(starter); //starts game with saved tree
                        }
                        case "Q" -> {
                            System.out.println("Thank you for playing, exiting now.");
                            System.exit(0);//exits program
                        }
                        default -> throw new
                                IllegalStateException("Unexpected value: " + choice);
                    }
                } else if (answer.equals("No") || answer.equals("no")) {
                    Scanner myObj2 = new Scanner(System.in); // Create a Scanner object
                    System.out.println("Distinguishing question: ");
                    String question = myObj2.nextLine();
                    currentNode.setData(question); //sets currentnode in tree to question given by user
                    Scanner myObj3 = new Scanner(System.in); // Create aScanner object
                    System.out.println("Yes answer: ");
                    question = myObj3.nextLine();
                    BinaryNode<String> yesAns = new BinaryNode(question);
                    currentNode.setLeftChild(yesAns);//sets leftchild of tree to yes answer of user
                    Scanner myObj4 = new Scanner(System.in);
                    System.out.println("The No answer:");
                    question = myObj4.nextLine();
                    BinaryNode<String> noAns = new BinaryNode<>(question);
                    currentNode.setRightChild(noAns); //sets rightchild of tree to no answer of user
                }
            }
        }
    }
    public static void saveTree(BinaryTree<String> start) throws IOException {
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(start);//writes object state to file
    }
    public static BinaryTree<String> loadTree() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois= new ObjectInputStream(fis);
        return (BinaryTree<String>) ois.readObject();
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Guesser();
    }
}
