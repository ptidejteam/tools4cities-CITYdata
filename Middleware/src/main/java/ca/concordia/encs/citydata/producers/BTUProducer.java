package ca.concordia.encs.citydata.producers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.security.core.context.SecurityContextHolder;

import ca.concordia.encs.citydata.core.implementations.CSVProducer;
import ca.concordia.encs.citydata.core.utils.RequestOptions;
import ca.concordia.encs.citydata.services.DatasetAccessService;

/**
 * This producer reads Flow data from a CSV source, processes it line by line, and produces a result set for the data 
 * readings for further operations. It stores all non-empty lines, optionally applies a configured operation on the data, and 
 * makes the processed results available to consumers.
 * @author Peter Yefi, Vinicius Mioto, Tahereh Bijani,  Mohamed Jendoubi  
 * @date: 2026-06-27
 * @author: Minette Z. Fixed the producer by adding the required constructor from CSVProducer to properly initialize the inherited base producer (CSVProducer)
 * @date: 2026-06-29
 * This producer has been refactored to follow AbstractProducer's new logic.
 * @author Minette Zongo
 * @since 2026-08-03
 */

public class BTUProducer extends CSVProducer {

	private String metadataPath;

	public BTUProducer(String filePath) {
		super(filePath);
	}

	public BTUProducer(final String filePath, final RequestOptions fileOptions) {
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
		reader.readLine();
		String line;
		while ((line = reader.readLine()) != null) {
			String trimmed = line.trim();
			if (!trimmed.isEmpty()) {
				csvLines.add(trimmed);
			}
		}
		return csvLines;
	}
}