package org.apache.tika.parser.textInsight;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.logging.Logger;

import com.google.gson.Gson;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.AbstractParser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.apache.commons.io.IOUtils;

import org.apache.tika.parser.ttr.TTRParser;
import org.apache.tika.parser.ner.corenlp.CoreNLPNERecogniser;


public class TextInsightParser extends AbstractParser {
	private static final long serialVersionUID = 1L;
	private static final Set<MediaType> SUPPORTED_TYPES = Collections.singleton(MediaType.application("text-insight"));
    
    public Set<MediaType> getSupportedTypes(ParseContext context) {
    	return SUPPORTED_TYPES;
    }
    
    private static final Logger LOG = Logger.getLogger(TextInsightParser.class.getName());
    
    public void parse(
            InputStream stream, ContentHandler handler,
            Metadata metadata, ParseContext context)
            throws IOException, SAXException, TikaException {
    	
    	TTRParser ttr = new TTRParser();
        CoreNLPNERecogniser cnlp = new CoreNLPNERecogniser();
        
        Map<String, Map<String,Integer>> coreNLPEntities;
        Map<String,Integer> extratedDates;
        Map<Integer,Integer> parsedDates;
        
        Pattern yearPattern = Pattern.compile("[0-9]{4}");
        
    	// Read file contents into string
    	StringWriter writer = new StringWriter();
    	try{
    		IOUtils.copy(stream, writer, "UTF-8");
    	} catch (IOException e){
    		LOG.warning("Unable to copy input stream to string writer");
    	}
    	
    	
		String fileText = writer.toString();
		//		String fileText = ttr.extract(stream);
		
		// Stanford coreNLP Extract Name Entities
		coreNLPEntities = cnlp.recogniseWithCounts(fileText);
		// extratedDates = coreNLPEntities.get("DATE");
		
		// Parse Dates
//		if( extratedDates != null ){
//			for (Map.Entry<String, Integer> date : extratedDates.entrySet()) {
//				Matcher m = yearPattern.matcher(date.getKey());
//				if (m.find()) {
//					Integer year = Integer.parseInt(m.group(1));
//					parsedDates.put(year, date.getValue());
//				}
//			}
//		}
		
		
		// Extract locations
		
		
		// Extract Concepts
		
		
		// Write data 
        Gson gson = new Gson();
//        metadata.add("dates", gson.toJson(parsedDates));
        metadata.add("insights", gson.toJson(coreNLPEntities));
    }
}
