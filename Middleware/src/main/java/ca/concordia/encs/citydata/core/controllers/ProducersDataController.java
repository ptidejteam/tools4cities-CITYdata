package ca.concordia.encs.citydata.core.controllers;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.concordia.encs.citydata.producers.TXTGeoLocationProducer;


/**
 * This class is to access all available producers listed in this class
 *
 * @author Minette Zongo
 * @since 2026-08-13
 */

@RestController
@RequestMapping("/data")
public class ProducersDataController {
	
//	@GetMapping(value = "/cities15000", produces = MediaType.APPLICATION_JSON_VALUE)
//	public String getGeoNamesCities() {
//		final GeoNamesCitiesProducer producer =
//				new GeoNamesCitiesProducer("../data/geolocation/cities15000.txt");
//		producer.fetch();
//		return producer.toString();
//	}
	@GetMapping(value = "/cities15000", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getGeoNamesCities(@RequestParam(defaultValue = "false") boolean structured) {
		TXTGeoLocationProducer producer = new TXTGeoLocationProducer("../data/geolocation/cities15000.txt");
		producer.setStructured(structured);
		producer.fetch();
		return producer.toString();
	}
	
	@GetMapping(value = "/codes", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getGeoCodes(@RequestParam(defaultValue = "false") boolean structured) {
		TXTGeoLocationProducer producer = new TXTGeoLocationProducer("../data/geolocation/admin2Codes.txt");
		producer.setStructured(structured);
		producer.fetch();
		return producer.toString();
	}
	
//	public ResponseEntity<Resource> getGeoNamesCitiesRaw() throws IOException {
//		Path path = Paths.get("../data/geolocation/cities15000.txt");
//		Resource file = new UrlResource(path.toUri());
//		return ResponseEntity.ok()
//				.contentType(MediaType.TEXT_PLAIN)
//				.body(file);
//	}
//	
//	@GetMapping(value = "/Codes", produces = MediaType.TEXT_PLAIN_VALUE)
//	public ResponseEntity<Resource> getAdminCodesRaw() throws IOException {
//		Path path = Paths.get("../data/geolocation/admin2Codes.txt");
//		Resource file = new UrlResource(path.toUri());
//		return ResponseEntity.ok()
//				.contentType(MediaType.TEXT_PLAIN)
//				.body(file);
//	}
//	
//	@GetMapping(value = "/componentCurves", produces = MediaType.TEXT_PLAIN_VALUE)
//	public ResponseEntity<Resource> getCodesRaw() throws IOException {
//		Path path = Paths.get("../data/energy_systems/curves/component_curves.json");
//		Resource file = new UrlResource(path.toUri());
//		return ResponseEntity.ok()
//				.contentType(MediaType.TEXT_PLAIN)
//				.body(file);
//	}
	
}
