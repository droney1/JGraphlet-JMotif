package graphs;
import java.io.*;
import java.util.*;

import randomgraph.RandomGraphToolBox;


public class GraphIO {
	
	/**
	 * [not used] obtain file names of data files from a configuration file.
	 * @param dataCfgFile
	 * @return
	 */
	public static String[] getDataFileNamesFromConfig(String dataCfgFile){
		String[] files = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(dataCfgFile));
			String line = null;
			String[] lineComp = null;
			ArrayList<String[]> nameComponents = new ArrayList<String[]>();
			String inDir = "";
			int numFile = 1;
			
			while((line =br.readLine())!=null){
				if(line.startsWith("//")||line.startsWith("#")|| line.isEmpty()) continue;	// "//" means comment text, skip
				lineComp = line.split("\\s?,?\\s+");
				if(lineComp[0].equals("inDir") && lineComp.length>1) inDir = lineComp[1];
				else{
					//process data set file name components
					nameComponents.add(lineComp);
					numFile *= lineComp.length;
				}
			}
			// create filenames
			int idx = 0;
			files = new String[numFile];
			Arrays.fill(files,  inDir+"/");
			int outRepeat = 1, inRepeat = numFile;
			for(String[] comps: nameComponents){
				idx = 0;
				inRepeat /= comps.length;
				for(int i = 0; i < outRepeat; i++){
					for(String s: comps){
						for(int j=0; j< inRepeat; j++){
							files[idx++] += s;
						}
					}
				}
				outRepeat *= comps.length;	//increase outter loop for comps.length.
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return files;
	}
	
	/**
	 * obtain data filenames from a configuration file that store the list of filename.
	 * The list of filenames are used for batch process of experiments
	 * @param fileList
	 * @return
	 */
	public static String[] getDataFileNamesFromFileList(String fileList){
		String[] files = null;
		try{
			BufferedReader br = new BufferedReader(new FileReader(fileList));
			List<String> ls = new LinkedList<String>();
			String line = null;
			while((line = br.readLine())!=null) {
				if(line.length()==0 || line.startsWith("#")||line.startsWith("//")) continue;
				ls.add(line);
			}
			files = new String[ls.size()];
			int idx = 0;
			for(String s: ls) files[idx++] = s;
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return files;
	}
	
	/**
	 * for batch experiments
	 * obtain a list of commands for experiment
	 * @param cfg
	 * @return
	 */
	public static String[][] getExperimentCommandsFromCfgFile(String cfg){
		String[][] res = null;
		ArrayList<String[]> ls = new ArrayList<String[]>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(cfg));
			String line = null;
			while((line = br.readLine())!= null){
				if(line.startsWith("//")|| line.startsWith("#")|| line.length() == 0) continue;
				ls.add(line.split("\\s?,?\\s+"));
			}
			res = new String[ls.size()][];
			int idx = 0;
			for(String[] l: ls) res[idx++] = l;
			br.close();
		}catch(Exception e){
			e.printStackTrace();
			res = new String[0][1];
		}
		return res;
	}
	
	public static int[][] getAdjacencyMatrixFromFile(String fileName){
		int[][] m = null;
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(fileName));
			String line = null;
			String[] data = null;
			int N = -1, node = 0;
			while((line = br.readLine()) != null){
				if(line.startsWith("#")|| line.startsWith("//")|| line.isEmpty() ) continue;
				data = line.split("\\s?[\\s,]+");
				if(N == -1){
					N = data.length;
					m = new int[N][N];
				}
				for(int i=0; i<N; i++){
					m[node][i] = Integer.parseInt(data[i]);
				}
				++node;
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return m;
	}
	
	public static int[][] getMatrixFromFile(String fileName){
		int[][] m = null;
		BufferedReader br = null;
		LinkedList<int[]> edgeList= new LinkedList<int[]>();
		int[] edge = null;
		try{
			br = new BufferedReader(new FileReader(fileName));
			String line = null;
			String[] data = null;
			while((line = br.readLine()) != null){
				if(line.startsWith("#")|| line.startsWith("//")|| line.isEmpty() ) continue;
				data = line.split("\\s?[\\s,]+");
				edge = new int[data.length];
				for(int i = 0; i< edge.length; ++i){
					edge[i] = Integer.parseInt(data[i]);
				}
				edgeList.add(edge);
			}
			br.close();
			m = new int[edgeList.size()][];
			int idx = 0;
			for(int[] e: edgeList){
				m[idx] = e;
				++idx;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return m;
	}
	
	public static int[][] convertAdjacentMatrixToEdgeList(int[][] m){
		int numEdge = 0;
		for(int[] row: m){
			for(int i: row){
				if(i==1) numEdge++;
			}
		}
		int[][] edges = new int[numEdge][2];
		numEdge = 0;
		for(int i = 0; i< m.length; i++){
			for(int j = 0; j < m[i].length; j++){
				if(m[i][j] >0 && i != j){
					edges[numEdge][0] = i;
					edges[numEdge][1] = j;
					++numEdge;
				}
			}
		}
		return edges;
	}

	public static void outputMatrix(String fileName, double[][] m){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
			StringBuilder sb = new StringBuilder();
			for(int r= 0; r < m.length; r++){
				for(double d: m[r]) sb.append(d+"\t");
				sb.setLength(sb.length() - 1);
				bw.append(sb.toString());
				sb.setLength(0);
				if(r<m.length -1) bw.newLine();
			}
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void outputMatrix(String fileName, int[][] m){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
			StringBuilder sb = new StringBuilder();
			for(int r= 0; r < m.length; r++){
				for(int d: m[r]) sb.append(d+"\t");
				sb.setLength(sb.length() - 1);
				bw.append(sb.toString());
				sb.setLength(0);
				if(r<m.length -1) bw.newLine();
			}
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void outputMatrix(String fileName, long[][] m){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
			StringBuilder sb = new StringBuilder();
			for(int r= 0; r < m.length; r++){
				for(long d: m[r]) sb.append(d+" ");
				sb.setLength(sb.length() - 1);
				bw.append(sb.toString());
				sb.setLength(0);
				if(r<m.length -1) bw.newLine();
			}
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void convertAdjMatFileToEdgeList(String fileName){
		int[][] m = getAdjacencyMatrixFromFile(fileName);
		int[][] edges = convertAdjacentMatrixToEdgeList(m);
		fileName = fileName.substring(0, fileName.lastIndexOf("_adj")) + "EdgeList.txt";
		outputMatrix(fileName, edges);
	}
	
	public static void convertStrIDToIntInFile(String fileName, boolean isEgo){
		try{
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			int idx = fileName.lastIndexOf('.');
			String outfile = fileName.substring(0, idx) + (isEgo?"Ego":"") + "Formated.txt";
			BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
			String line = null;
			String[] data = null;
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			int s = 0, t = 0;
			while((line = br.readLine()) != null){
				if(line.startsWith("//") || line.startsWith("#") || line.isEmpty()) continue;
				data = line.split("\\s?[,\\s]+");
				if(map.containsKey(data[0])) s = map.get(data[0]);
				else{
					s = map.size() + 1;
					map.put(data[0], s);
				}
				if(map.containsKey(data[1])) t = map.get(data[1]);
				else{
					t = map.size() + 1;
					map.put(data[1], t);
				}
				bw.write(s + "\t" + t+"\n");
			}
			if(isEgo){
				s = map.size() + 1;
				for(int i: map.values())
					bw.write(s + "\t" + i+"\n");
			}
			br.close();
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * collapse temporal network into static network
	 * @param tEdges	array of temporal edges with integer time stamp. tEdge[i] = int[]{source, target, time_stamp};
	 * @return
	 */
	public static int[][] getStaticGraphEdgesFromTemporalEdges(int[][] tEdges){
		HashSet<Long> edgeSet = new HashSet<Long>();
		HashMap<Integer, Integer> map = new HashMap<Integer,Integer>();
		long edgeCode = -1;
		for(int[] e: tEdges){
			if(!map.containsKey(e[0])) map.put(e[0], map.size());
			if(!map.containsKey(e[1])) map.put(e[1], map.size());
			edgeCode = RandomGraphToolBox.getEdgeKey(map.get(e[0]), map.get(e[1]));
			edgeSet.add(edgeCode);
		}
		int T = edgeSet.size();
		int[][] edges = new int[T][];
		int t = 0;
		for(long l: edgeSet){
			edges[t] = new int[2];
			RandomGraphToolBox.getEdgeFromKey(l, edges[t]);
			++t;
		}
		return edges;
	}
	
	public static int[][] getStaticEdgesFromEdgesFromSnapshots(int[][][] tEdges, int beg, int end){
		HashSet<Long> edgeSet = new HashSet<Long>();
		HashMap<Integer, Integer> map = new HashMap<Integer,Integer>();
		long edgeCode = -1;
		int[][] edges = null;
		for(int i = beg; i<end; ++i){
			edges = tEdges[i];
			for(int[] e: edges){
				if(!map.containsKey(e[0])) map.put(e[0], map.size());
				if(!map.containsKey(e[1])) map.put(e[1], map.size());
				edgeCode = RandomGraphToolBox.getEdgeKey(map.get(e[0]), map.get(e[1]));
				edgeSet.add(edgeCode);
			}
		}
		int T = edgeSet.size();
		edges = new int[T][];
		int t = 0;
		for(long l: edgeSet){
			edges[t] = new int[2];
			RandomGraphToolBox.getEdgeFromKey(l, edges[t]);
			++t;
		}
		return edges;
	}
	
	public static void convertTemporalNetToStaticNet(String tFileName, String sFileName){
		int[][] tempGraph = getMatrixFromFile(tFileName);
		int[][] staticGraph = getStaticGraphEdgesFromTemporalEdges(tempGraph);
		Arrays.sort(staticGraph, new Comparator<int[]>(){
			public int compare(int[] a, int[] b){
				if(a[0] == b[0]) return a[1] - b[1];
				else return a[0] - b[0];
			}
		});
		System.out.println("\toutput file: "+ sFileName);
		outputMatrix(sFileName, staticGraph);
	}
	
	public static int[] edgesToNodeList(int[][] edges){
		int[] res = new int[edges.length * 2];
		int idx = 0;
		for(int[] e: edges){
			res[idx] = e[0];
			++idx;
			res[idx] = e[1];
			++idx;
		}
		return res;
	}
	
	/**
	 * read a csv file and return a string[] list
	 * @param fileName
	 * @return
	 */
	public static List<String[]> getDataListFromCSV(String fileName){
		List<String[]> res =  new LinkedList<String[]>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String[] data = null;
			String line = null;
			while((line = br.readLine())!=null){
				if(line.isEmpty()||line.startsWith("//") || line.startsWith("#")) continue;
				data = line.split("\\s?,?\\s+");
				if(data == null || data.length == 0 || data[0].isEmpty()) continue;
				res.add(data);
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * read a csv and convert it to matrix long[][]
	 * @param input
	 * @return
	 */
	public static long[][] convertCSVFileFormatLong(String input){
		List<String[]> ls = getDataListFromCSV(input);
		long[][] res = new long[ls.size()][];
		int idx = 0, idx2 = 0;;
		for(String[] strs : ls){
			long[] tmp = new long[strs.length];
			idx2=  0;
			for(String s: strs){
				tmp[idx2] = Long.parseLong(s);
				++idx2;
			}
			res[idx] = tmp;
			++idx;
		}
		return res;
	}
}
