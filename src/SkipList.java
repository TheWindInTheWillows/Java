import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class SkipList{
	
	SkipNode head = null;
	private int MAXLEVEL = 1;
	private final int MIN = 0x80000000;
//	private final int MAX = 0x7fffffff;
	
	public static void main(String[] args) {
		new SkipList().createFromFile();
	}
	
	public SkipList() {
		head = getLink(MIN);
		MAXLEVEL = head.level;
	}
	
	//Product a link using a number and return the top node;
	public SkipNode getLink(int num){
		SkipNode top = new SkipNode(num);
		SkipNode sp = top;
		for(int i=1;i<top.level;i++){
			sp.down = new SkipNode(top.data,top.level-i);
			sp = sp.down;
		}
		sp.down = top;
		return top;
	}
	
	//read numbers from file and init the SkipList
	public void createFromFile(){
		String pathname = System.getProperty("user.dir");
		String filename = pathname+"\\src\\nums.dat";
		String line = null;
		try {
			FileInputStream fis = new FileInputStream(new File(filename));
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			while((line = br.readLine()) != null){
				int num = Integer.parseInt(line);
				insert(num);
			}
			br.close();
			isr.close();
			fis.close();
		} catch (FileNotFoundException e) {
			System.err.println("Cannot find this file");
		} catch (IOException e) {
			System.err.println("Open file error!");
		}
	}
	
	public void insert(int num) {
		SkipNode top = getLink(num);
		
		//Add nodes of head when the number of new link's nodes are bigger;
		if(top.level > MAXLEVEL){
			MAXLEVEL = top.level;
			SkipNode sn = head;
			for(int i = sn.level+1;i<=MAXLEVEL;i++){
				SkipNode p = new SkipNode(MIN);
				p.level = i;
				p.down = sn;
				sn = p;
			}
			head = sn;
		}
		
		//Link every node in it's level.
		SkipNode sn = top;
		while(sn.level != 1){
			linkLevel(sn);
			sn =sn.down;
		}
		linkLevel(sn);
	}
	
	//Find the right position of a node and link it with it's pre-node and post-node in it's level.
	public void linkLevel(SkipNode sn){
		SkipNode  pre = getPreNode(sn.data, sn.level);
		sn.right = pre.right;
		pre.right = sn;
//		sn = sn.down;
	}
		
	//Find and return the right pre-node in a center level of a number. CORE!
	public SkipNode getPreNode(int num,int level)
	{
		SkipNode sn = head;
		for (int i = sn.level; i >= level; i--){
			while(sn.right != null && sn.right.data < num){
				sn = sn.right;
			}
			if(sn.level == level) return sn;
			if(sn.level != 1)sn = sn.down;
		}
		return null;
	}
	
	//To delete a node.The value returned 1 for successfully and 0 for not found. 
	int delete(int num){
		SkipNode sn = search(num);
		if(sn == null) return 0;
		sn = sn.down;
		for(int i=sn.level;i>=1;i--){
			SkipNode pre = getPreNode(num, i);
			pre.right = sn.right;
			sn = sn.down;
		}
		return 0;
	}
	
	//To search a number's position and return its bottom node or return null. 
	SkipNode search(int num){
		SkipNode sn = getPreNode(num, 1);
		if(sn.right != null && sn.right.data == num){
			return sn.right;
		}
		return null;
	}
}

class SkipNode{
	int data;
	int level = 0;
	SkipNode right = null;
	SkipNode down = null;
	SkipNode left = null;
	
	//Use for top node
	public SkipNode(int dat) {
		this.data = dat;
	    this.level = getLevel();
	}
	
	public SkipNode(int num,int level){
		this.data = num;
		this.level = level;
	}
	
	private static int getLevel()
	{
		int level = 1;
		while(new Random().nextInt() % 2 == 0){
			level++;
		}
		return level;
	}
}