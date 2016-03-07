import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;


public class IOtest {

	public static void main(String[] args) throws Exception {
		String path = System.getProperty("user.dir");
//		System.out.println(path);
		FileInputStream fis = new FileInputStream(new File(path+"\\src\\nums.dat"));
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		String line = "";
		int sum = 0;
		while((line = br.readLine())!=null){
			int num = Integer.parseInt(line);
			sum+=num;
//			System.out.println(num);
		}
		
		
	}

}
