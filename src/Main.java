import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Main {
	
	private int numMarcos;
	
	private int numPaginas;
	
	private int numRef;
	
	private Integer[][] TP;
		
	private Integer[] ram;
	
	
	public Main() {
		
	}
	
	public static void main(String [] args) {
		try {
			File archivo = new File("data/referencias8_16_75.txt");
			FileReader fr = new FileReader (archivo);
			BufferedReader br = new BufferedReader(fr);
			
			int numMarcos = Integer.parseInt(br.readLine());
			System.out.println("El número de marcos de página es: " + numMarcos);
			int numPaginas = Integer.parseInt(br.readLine());
			System.out.println("El número de páginas es: " + numPaginas);
			int numRef = Integer.parseInt(br.readLine());
			System.out.println("El número de referencias es: " + numRef);
			
			int [] refId = new int[numRef];
			String [] refBit = new String[numRef];
			
			for(int i=0; i<numRef; i++) {
				String s[] = br.readLine().split(",");
				refId[i] = Integer.parseInt(s[0]);
				refBit[i] = s[1];
			}
			
			NRU nru1 = new NRU(numMarcos, numRef, refId, refBit, true);
			nru1.start();
			NRU nru2 = new NRU(numMarcos, numRef, refId, refBit, false);
			nru2.start();
			
		} catch(Exception e) {
			
		}
	}
}
