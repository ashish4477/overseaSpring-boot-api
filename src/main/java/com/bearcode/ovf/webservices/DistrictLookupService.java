package com.bearcode.ovf.webservices;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DistrictLookupService {
	
    public static final String[] EMPTY = new String[] {"", "", ""};

	private static Logger melissaDataLog = LoggerFactory.getLogger("com.bearcode.MelissaData");
    
    private String melissaDataUrl = "";
    private String melissaCacheDir; 
    private long melissaCacheTime = 30L*24L*60L*60L*1000L; // 1 month
    
	public void setMelissaDataUrl(String melissaDataUrl) {
		this.melissaDataUrl = melissaDataUrl;
	}
	
	public void setCacheDir(String cacheDir) {
		cacheDir = FilenameUtils.normalizeNoEndSeparator(cacheDir);
    	if (cacheDir == null) throw new IllegalArgumentException("Invalid District Lookup Service Cache directory: '" + cacheDir + "'");
    	
        File fCacheDir = new File(cacheDir);
        if (fCacheDir.exists()) {
        	melissaDataLog.info("District Lookup Service Cache Directory '" + cacheDir + "' found");
        } else {
        	fCacheDir.mkdir();
        	melissaDataLog.info("District Lookup Service Cache Directory '" + cacheDir + "' has been created");
        }
        //this.votesmartCacheDir = voteSmartCacheDir + File.separator;
		this.melissaCacheDir = cacheDir + File.separator;
	}

	public void setCacheTime(long cacheTimeInMillis) {
		this.melissaCacheTime = cacheTimeInMillis;
	}

	// TODO: Return result also must be cached on our side
	// use md5(address|city|state|zip) as cache key
    @SuppressWarnings("deprecation")
    public String[] findDistrict( String address, String city, String state, String zip ) {
    	if (melissaCacheDir == null) throw new IllegalArgumentException("Invalid District Lookup Service Cache directory");
    	
        if (address == null) { address = ""; } else { address = address.trim(); }
        if (city == null) { city = ""; } else { city = city.trim(); }
        if (state == null) { state = ""; } else { state = state.trim(); }
        if (zip == null) { zip = ""; } else { zip = zip.trim(); }
        
        if (address.length() == 0 && city.length() == 0 && state.length() == 0 && zip.length() == 0)
        	return EMPTY;
    	
        String district = "";
        String zip4 = "";
        if ( zip.indexOf("-") > 0 ) {
            zip4 = zip.substring( zip.indexOf("-")+1 );
            zip = zip.substring( 0, zip.indexOf("-") );
        }
        
        try {
            // log every melissaData request to a special log file
            melissaDataLog.info("District Lookup Service: request {address:'"+address+"', city:'"+city+"', state:'"+state+"', zip:'"+zip+"'");
            
        	String responseString = "";
        	
        	String md5hash = DigestUtils.md5Hex(String.format("%s|%s|%s|%s", address.toLowerCase(), city.toLowerCase(), state.toLowerCase(), zip.toLowerCase()));
        	String fileName = String.format("%s%s.xml", melissaCacheDir, md5hash);
        	File cacheFile = new File(fileName);
        	if (cacheFile.exists() && cacheFile.lastModified() > new Date().getTime() - melissaCacheTime) {
        		// use cached file
        		melissaDataLog.info("District Lookup Service: cache file used '" + fileName + "'");
        		responseString = FileUtils.readFileToString(cacheFile, "UTF-8");
        	} else {
                // process lookup
                HttpClient httpClient = new HttpClient();
                PostMethod post = new PostMethod(melissaDataUrl);
                String testRequest = new StringBuilder()
                        .append("<?xml version=\"1.0\" ?><RecordSet><CustomerID>115932683</CustomerID><Record>")
                        .append("<Address>").append(address).append("</Address>")
                        .append("<City>").append(city).append("</City>")
                        .append("<State>").append(state).append("</State>")
                        .append("<Zip>").append(zip).append("</Zip>")
                        .append("<Plus4>").append(zip4).append("</Plus4>")
                        .append("<CongressionalDistrict/></Record></RecordSet>")
                        .toString();

                post.setRequestBody( testRequest );
                httpClient.executeMethod( post );
                responseString = IOUtils.toString( post.getResponseBodyAsStream(), "UTF-8" );
                
                // save response to the cache file
                FileUtils.writeStringToFile(cacheFile, responseString, "UTF-8");
        		melissaDataLog.info("District Lookup Service: cache file created '" + fileName + "'");
        	}
        	
            if ( responseString != null && responseString.length() > 0 ) {
                Matcher matcher = Pattern.compile(".*<Zip>(\\d+)</Zip><Plus4>(\\d+)</Plus4><CongressionalDistrict>(\\d+)</CongressionalDistrict>.*", Pattern.DOTALL)
                        .matcher( responseString );
                if (matcher.matches()) {
                	zip = matcher.group(1);
                	zip4 = matcher.group(2);
                    district = matcher.group(3);
                    if (district.startsWith("0")) {
                        district = district.substring(1); // remove leading zero
                    }
                }
            }
            
			if (district.length() == 0 && zip.length() == 0 && zip4.length() == 0) return EMPTY;
			
			return new String[]{district, zip, zip4};
			
        } catch (IOException e) {
            melissaDataLog.error(String.format("District Lookup Service Exception for address='%s', city='%s', state='%s', zip='%s'. Exception:\n%s ", 
            		address, city, state, zip, e.toString()));
            return EMPTY;
        }
    }

}
