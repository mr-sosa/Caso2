

public class NRU extends Thread{
	
	private int cantidadPaginas;
	private int cantidadFrames;
	private int []paginas;
	private int [][]matriz;
	private int []fallos;
	private int []casoFrames;
	private boolean thread;
	private String []bits;
	private static int [][] TP;
	

	public NRU(int nPag, int nFrame, int[] nPage, String[] nBit, boolean x){
		this.thread = x;
		this.cantidadFrames = nPag;
		this.cantidadPaginas = nFrame;
		this.bits = nBit;
		this.paginas = nPage;
		System.out.println("NRU");
	}

	public void setPaginas(int []paginas) {
		this.paginas = paginas;
	}

	public void setCantidadPaginas(int cantidadPaginas) {
		this.cantidadPaginas = cantidadPaginas;
	}

	public void setCantidadFrames(int cantidadFrames) {
		this.cantidadFrames = cantidadFrames;
	}
	
	public void iniciarxfallos(){
		for(int i=0;i<cantidadPaginas;i++){
			fallos[i]=0;
		}
	}
	
	private void iniciarMatriz(){
		for(int i=0;i<cantidadFrames;i++){
			for(int j=0;j<cantidadPaginas;j++){
				matriz[i][j]=-1;
			}
			TP[i][0] = -1;
			TP[i][1] = 0;
		}
	}
	
	private boolean buscar(int paginaActual){
		boolean encontrado=false;
		for(int i=0;i<cantidadFrames;i++){
			if(paginas[paginaActual]==matriz[i][paginaActual]){
				encontrado=true;
			}
		}
		return encontrado;
	}
	
	private void llenarFila(int paginaActual, int frame){
		
		TP[frame][0]=paginaActual;
		if(bits[paginaActual].equals("r")) {
			TP[frame][1]=2;
		}
		else if(bits[paginaActual].equals("m")) {
			TP[frame][1]=3;
		}
		
		for(int j=paginaActual;j<cantidadPaginas;j++){
			matriz[frame][j]=paginas[paginaActual];
		}
	}
	//este m?todo me devolver? el frame que fue el mas antiguo en ser usado
	private int menosUsado(int paginaActual){
		int frame=0;
		
		for(int i=0;i<cantidadFrames;i++){
			casoFrames[i]=TP[i][1];
		}
		
		for (int i=0;i < cantidadFrames; i++) {
			if(casoFrames[i]<casoFrames[frame]){
				frame=i;
			}
		}
		return frame;
	}
	
	private void modificar(int paginaActual){
		boolean encontradoFrameLibre=false;
		int i;
		for(i=0;i<cantidadFrames;i++){
			if(matriz[i][paginaActual]==-1){
				encontradoFrameLibre=true;
				break;
			}
		}
		
		if(!encontradoFrameLibre){
			llenarFila(paginaActual, menosUsado(paginaActual));
		}else{
			llenarFila(paginaActual, (i));
		}
		
		fallos[paginaActual]=1;
		
	}

	private void modificarBit(int paginaActual){
		int i;
		for(i=0;i<cantidadFrames;i++){
			if(TP[paginaActual][0] == paginas[paginaActual]){
				String s = bits[paginaActual];
				if(s.equals("r")) {
					TP[paginaActual][1] = 2;
				}else {
					TP[paginaActual][1] = 3;
				}
				break;
			}
		}
		
	}
	public synchronized void nru(){
		matriz=new int [cantidadFrames][cantidadPaginas];
		fallos= new int [cantidadPaginas];
		casoFrames= new int[cantidadFrames];
		TP = new int [cantidadFrames][2];
		iniciarxfallos();
		iniciarMatriz();
		//Recorremos todas las paginas
		try {
			for(int j=0;j<cantidadPaginas;j++){
				if(j%(cantidadFrames/2) == 0) {
					Thread.yield();
				}
				if(!buscar(j)){
					modificar(j);
				} else {
					modificarBit(j);
				}
				Thread.sleep(1);
			}
		}catch(Exception e) {
			
		}
		mostrarMatriz();
	}
	
	public void mostrarMatriz(){
		int cantidadFallos=0;
		for(int i=0;i<cantidadFrames;i++){
			for(int j=0;j<cantidadPaginas;j++){
				if(matriz[i][j]==-1){
					System.out.print(" ");//para que no se muestre el -1 en la matriz
				}else
				System.out.print(""+matriz[i][j]);
			}
			System.out.println();
		}
		
		for(int i=0;i<cantidadPaginas;i++){
			if(fallos[i]==1){
				cantidadFallos++;
			}
			System.out.print(""+fallos[i]);
		}
		System.out.println("\n\nFallos encontrados: "+cantidadFallos);
	}

	
	public synchronized void actualizarBitsRM() {
		try {
			boolean x = true;
			while(x) {
				wait();
				for(int i=0; i<cantidadFrames;i++) {
					if(TP[i][1] == 3 || TP[i][1] == 1) {
						TP[i][1] = 1;
					} else {
						TP[i][1]=0;
					}
				}
				Thread.sleep(20);
				notifyAll();
			}
				
		}catch(Exception e) {
		}
	}
	
	public void run() {
		if(thread) {
			nru();			
		}else {
			actualizarBitsRM();
		}
	}
}