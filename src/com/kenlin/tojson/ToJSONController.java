package com.kenlin.tojson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ToJSONController implements InitializingBean {
    private CSVFormat		csvformat = CSVFormat.DEFAULT;
    private static ConcurrentHashMap<String, StringWriter> cache = null;
    private ObjectMapper	mapper = null;
    private static String	LOGFMT = "{'remoteaddr':'%s', 'id':'%s', 'status':'%s'}";
    private Logger			logger = null;

	private void audit(String remoteAddr, String id, String status) {
    	logger.info(LOGFMT, remoteAddr, id, status);
    }

	@Override
	public void afterPropertiesSet() throws Exception {
	    cache = new ConcurrentHashMap<String, StringWriter>();
	    mapper = new ObjectMapper();
	    
		String packageName = this.getClass().getPackage().getName();
	    logger = LogManager.getFormatterLogger(packageName);
	}

	public static void addCORSHeaders(HttpServletResponse resp) {
		resp.addHeader("Access-Control-Allow-Origin", "*");
		resp.addHeader("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE");
		resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
	}

	/**
	 * @see http://commons.apache.org/proper/commons-csv/apidocs/index.html?org/apache/commons/csv/CSVFormat.html
	 * @param instream
	 * @param hasheader
	 * @throws IOException
	 */
	private StringWriter csvToJSON(InputStream instream, boolean hasheader) throws IOException {
	    StringWriter	writer = new StringWriter();
    	BufferedReader	reader = new BufferedReader(new InputStreamReader(instream));
	    CSVParser		parser = new CSVParser(reader, csvformat);
	    JsonGenerator	gen = null;

	    try {
		    gen = new JsonFactory().createGenerator(writer);
		    gen.writeStartObject();
		    gen.writeFieldName("data");

		    gen.writeStartObject();
		    gen.writeFieldName("items");
		    gen.writeStartArray();
		    
		    CSVRecord	header = null;
			int			n = 0;
			for (CSVRecord record : parser) {
	    		// The first record is the header.
	    		if (n == 0 && hasheader) {
	    			header = record;
	    			
				// All subsequent records are data records.
				} else {
					gen.writeStartObject();
					for (int i = 0; i < record.size(); i++) {
						// Field
						try {
							gen.writeFieldName(header.get(i));
						} catch (Exception e) {
							gen.writeFieldName(Integer.toString(i));
						}
						// Value
						String val = record.get(i);
						try {
							long valLong = Long.parseLong(val);
							gen.writeNumber(valLong);
						} catch (NumberFormatException e1) {
							try {
								float valFloat = Float.parseFloat(val);
								gen.writeNumber(valFloat);
							} catch (NumberFormatException e2) {
								gen.writeString(val);
							}
						}
					}
					gen.writeEndObject();
				}
	
				n++;
			}

			gen.writeEndArray();	// items
			gen.writeEndObject();

			gen.writeEndObject();	// data

			gen.close();
	    } catch (JsonGenerationException e1) {
	    	e1.printStackTrace();
	    } catch (Exception e2) {
	    	e2.printStackTrace();
	    } finally {
	    	if (parser != null) {
	    		parser.close();
	    	}
	    }

		return writer;
	}
	
	// e.g., http://localhost:8080/2jsonj/csv2json/api?url=https://raw.github.com/CityOfPhiladelphia/ppa-data/master/red-light-cameras/red-light-camera-locations.csv
	// @see http://docs.spring.io/spring/docs/3.2.4.RELEASE/spring-framework-reference/html/mvc.html#mvc-config
	// @see https://gist.github.com/kdonald/2012289/raw/363289ee8652823f770ef82f594e9a8f15048090/ExampleController.java
	@RequestMapping(value="/csv2json/api", method=RequestMethod.GET)
	@ResponseBody
	public JsonNode getByID(@RequestParam String url, HttpServletRequest req, HttpServletResponse resp) {
		boolean hasheader = true;

		StringWriter	json = null;
		// See http://hc.apache.org/httpcomponents-client-ga/tutorial/html/fundamentals.html#d5e95
		CloseableHttpClient csvClient = HttpClients.createDefault();
		HttpGet csvGet = new HttpGet(url);
		try {
			CloseableHttpResponse csvResp = csvClient.execute(csvGet);
			try {
				if ((json = cache.get(url)) != null) {
					audit(req.getRemoteAddr(), url, "cached");
				} else {
					HttpEntity csvEntity = csvResp.getEntity();
				    if (csvEntity != null) {
				        InputStream instream = csvEntity.getContent();
				        try {
							// do something useful
				    	    audit(req.getRemoteAddr(), url, "live");
				        	json = csvToJSON(instream, hasheader);
				        } finally {
				        	if (instream != null) {
				        		instream.close();
				        	}
				        }
				    }
					EntityUtils.consume(csvEntity);
					cache.put(url, json);
				}
				addCORSHeaders(resp);
			} catch (Exception e) {
				try {
					return mapper.readTree("{'error': {'errorcode': 404}}");
				} catch (Exception ee) {
					logger.error("mapper.readTree", ee);
				}
			} finally {
				csvResp.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			return mapper.readTree(json.toString());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
