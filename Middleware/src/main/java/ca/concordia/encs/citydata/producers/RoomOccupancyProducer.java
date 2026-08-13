package ca.concordia.encs.citydata.producers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.security.core.context.SecurityContextHolder;
import ca.concordia.encs.citydata.core.implementations.CSVProducer;
import ca.concordia.encs.citydata.core.utils.RequestOptions;
import ca.concordia.encs.citydata.services.DatasetAccessService;

/**
 * This producer reads an sensor data from a CSV file, extracts all data lines, and provides them as input to the potential further 
 * operations
 * @author Minette Zongo M., Sikandar Ejaz
 * @date: 2025-10-04, 2026-08-04
 * 
 */

public class RoomOccupancyProducer extends CSVProducer {
	
	String metadataPath;
	
	public RoomOccupancyProducer(String filePath) {
		super(filePath);
	}
	
	public RoomOccupancyProducer(final String filePath, final RequestOptions fileOptions ) {
		super(filePath, fileOptions);
	}
	
	public void setMetadataPath(String metadataPath) {
		this.metadataPath = metadataPath;
	}

	@Override
	protected void beforeFetch() {
		if (metadataPath != null) {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			new DatasetAccessService().checkAuthorisationForPath(username, metadataPath);
		}
	}
	
	@Override
	protected ArrayList<String> parseLines(BufferedReader reader) throws IOException {
		ArrayList<String> csvLines = new ArrayList<>();
		StringBuilder preview = new StringBuilder();
	    reader.readLine(); // skip header row
	    String line;
	    while ((line = reader.readLine()) != null) {
	        if (preview.length() < 500) {
	            preview.append(line).append(System.lineSeparator());
	        }
	        String trimmed = line.trim();
	        if (!trimmed.isEmpty()) {
	            csvLines.add(trimmed);
	        }
	    }
	    System.out.println(preview.substring(0, Math.min(500, preview.length())));
	    return csvLines;
	}
	
}

//			// Split by newlines
//			String[] lines = csvString.split("\\R");
//
//			ArrayList<String> csvLines = new ArrayList<>();
//
//			// Skip header (line 0) and process data lines
//			for (int i = 1; i < lines.length; i++) {
//				String line = lines[i].trim();
//				if (!line.isEmpty()) {
//					csvLines.add(line);
//				}
//			}
//
//			this.setResult(csvLines);
//
//			this.applyOperation();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new MiddlewareException.DatasetNotFound("Error processing occupancy CSV data: " + e.getMessage());
//		}
//	}
