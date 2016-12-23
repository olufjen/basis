package no.basis.felles.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;





import no.basis.felles.model.FileProcess.ReadFile;

/**
 * @author oluf
 *
 * Denne klassen benyttes til File og java 8 Stream operasjoner
 */
public class FileModel {
	private Stream<String> lines;
	private String filePath;
	private List<String> linesFromfile;
	
	public FileModel() {
		super();
		
	}
	
	public List<String> getLinesFromfile() {
		return linesFromfile;
	}

	public void setLinesFromfile(List<String> linesFromfile) {
		this.linesFromfile = linesFromfile;
	}

	public Stream<String> getLines() {
		return lines;
	}
	public void setLines(Stream<String> lines) {
		this.lines = lines;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	/**
	 * createLines
	 * Denne rutinen åpner en file som en Stream
	 */
	public void createLines(){
		try {
			lines = Files.lines(Paths.get(filePath), Charset.defaultCharset());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * readlines
	 * This function Reads a line from a file using a functional interface ReadFile
	 * A functional interface  passes behavior
	 * This function executes the behavior
	 * The behavior of this functional interface is: BufferedReader -> String
	 * @param r a functional interface ReadFile
	 * @return String a line from the file
	 * @throws IOException
	 */
	public String readlines(ReadFile r,BufferedReader br) throws IOException{

			return r.readFile(br); // Processing the buffered reader object

		
	}
	/**
	 * fillLines
	 * This routine removes empty lines from the list
	 * @param list of lines from file
	 * @param p The predicate
	 * @return a nonempty list
	 */
	public <T> List<T> fillLines (List<T> list,Predicate<T> p){
		List<T> results = new ArrayList<>();
		String line = null;
		for(T s:list)
		if (p.test(s)){ // calls the lambda expression
			results.add(s);
		}
		return results;
	}
	/**
	 * sendeListe 
	 * This routine reads a file line by line and places it in a List<String>
	 * @return List<String> lines from a file
	 */
	public List sendeListe(){
/*
 * Define the lambda expression using predicate
 * Keep a string if it is nonempty
 */
		Predicate<String> nonemptyP = (String s) -> (s != null && !s.isEmpty()); // Is executed for the whole list from the call to fillLines!!
/*
 * Define the lambda expression using functional interface ReadFile	
 * The target type is ReadFile which must be a functional interface	
 */
		ReadFile r = (BufferedReader brx) -> brx.readLine(); // Is executed from the routine readlines
		String line = "";
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		linesFromfile = new ArrayList<String>();
		while(line != null ){
			try {
				line = readlines(r, br);
				linesFromfile.add(line);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}			
		}
		List<String> nonEmptylines = fillLines(linesFromfile,nonemptyP);
		return nonEmptylines;
	}
	public  String extract(String s,Function <String,String> f){
		return f.apply(s);
	}
	/**
	 * extractString
	 * This routine extracts a substring form a string  using the Function interface
	 * It finds the last index of a string using separator
	 * @param line The original string
	 * @param separator The separator
	 * @param startindex The startindex
	 * @return the substring
	 */
	public String extractString(String line,char separator,int startindex){
		int index = line.lastIndexOf(separator);
		Function<String,String> f = (String s) -> line.substring(startindex,index);
		return extract(line,f);

	}
	/**
	 * createNewLines
	 * Denne rutinen legger til nye datoer i listen over sendedatoer.
	 * @param startDate
	 * @param endDate
	 * @param linesFromfile
	 * @return
	 */
	public List<String> createNewLines(LocalDate startDate,LocalDate endDate,List<String>linesFromfile){
		DateTimeFormatter formattter = DateTimeFormatter.ofPattern("yy-MM-dd");   
		String startSdato = startDate.format(formattter);
		LocalDate tomorrow = startDate.plus(1, ChronoUnit.DAYS);
		int cdt = 0;
		do
		{
			String kveld = new String(startSdato);
			startSdato = startSdato + "-08:00";
			linesFromfile.add(startSdato);
			kveld = kveld + "-20:00";
			linesFromfile.add(kveld);
			startSdato = tomorrow.format(formattter);
			tomorrow = tomorrow.plus(1, ChronoUnit.DAYS);
			cdt = tomorrow.compareTo(endDate);
		}while(cdt < 1);
		return linesFromfile;
		
	}
	public void writetoFile(Stream fileData){
		Path wiki_path = Paths.get("c:\\ullern\\acem\\radio\\", "translistnew.txt");

        Charset charset = Charset.forName("UTF-8");
 //       String text = "\nfrom java2s.com!";
        try (BufferedWriter writer = Files.newBufferedWriter(wiki_path, charset, StandardOpenOption.APPEND)) {
           // writer.write(text);
            fileData.forEach(text -> writeToFile(writer,text));
            writer.close();
            fileData.close();
        } catch (IOException e) {
            System.err.println(e);
        }
		
	
	}
    private void writeToFile(BufferedWriter fw, Object text) {
    	String tx = "";
    	if (text != null && text.toString() != null)
    		tx = text.toString();
        try {
            fw.write(String.format("%s%n", tx));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
