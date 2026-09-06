package ca.concordia.encs.citydata.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ca.concordia.encs.citydata.core.contracts.IOperation;
import ca.concordia.encs.citydata.core.implementations.AbstractOperation;

/**
 * Filters GeoJSON FeatureCollection features by distance to a given point.
 * A feature is kept when its geometry centroid is within radiusMeters.
 *
 * @author Aboolfazl Rezaei
 * @since 2026-02-25
 */
public class GeoJsonFilterOperation extends AbstractOperation<String> implements IOperation<String> {

	private static final double EARTH_RADIUS_METERS = 6371000.0;

	private Double centerLongitude;
	private Double centerLatitude;
	private Double radiusMeters;

	public void setCenterLongitude(Double centerLongitude) {
		this.centerLongitude = centerLongitude;
	}

	public void setCenterLatitude(Double centerLatitude) {
		this.centerLatitude = centerLatitude;
	}

	public void setRadiusMeters(Double radiusMeters) {
		this.radiusMeters = radiusMeters;
	}

	@Override
	public ArrayList<String> apply(ArrayList<String> inputs) {
		this.validateParameters();
		final ArrayList<String> filteredOutputs = new ArrayList<>();

		for (String input : inputs) {
			if (input == null || input.isBlank()) {
				continue;
			}

			final JsonElement element = JsonParser.parseString(input);
			if (element.isJsonObject()) {
				final JsonObject object = element.getAsJsonObject();
				filteredOutputs.add(filterElementIfFeatureCollection(object).toString());
			} else if (element.isJsonArray()) {
				final JsonArray resultArray = new JsonArray();
				for (JsonElement child : element.getAsJsonArray()) {
					if (child.isJsonObject()) {
						resultArray.add(filterElementIfFeatureCollection(child.getAsJsonObject()));
					} else {
						resultArray.add(child);
					}
				}
				filteredOutputs.add(resultArray.toString());
			} else {
				filteredOutputs.add(input);
			}
		}
		return filteredOutputs;
	}

	private JsonObject filterElementIfFeatureCollection(JsonObject inputObject) {
		if (!inputObject.has("type") || !"FeatureCollection".equalsIgnoreCase(inputObject.get("type").getAsString())
				|| !inputObject.has("features") || !inputObject.get("features").isJsonArray()) {
			return inputObject;
		}

		final JsonArray inputFeatures = inputObject.getAsJsonArray("features");
		final JsonArray filteredFeatures = new JsonArray();

		for (JsonElement featureElement : inputFeatures) {
			if (!featureElement.isJsonObject()) {
				continue;
			}
			final JsonObject feature = featureElement.getAsJsonObject();
			final Coordinate centroid = getFeatureCentroid(feature);
			if (centroid == Coordinate.NONE) {
				continue;
			}

			final double distance = haversineMeters(this.centerLatitude, this.centerLongitude, centroid.latitude,
					centroid.longitude);
			if (distance <= this.radiusMeters) {
				filteredFeatures.add(feature);
			}
		}

		final JsonObject output = new JsonObject();
		for (Map.Entry<String, JsonElement> entry : inputObject.entrySet()) {
			if (!"features".equals(entry.getKey())) {
				output.add(entry.getKey(), entry.getValue());
			}
		}
		output.add("features", filteredFeatures);
		return output;
	}

	private Coordinate getFeatureCentroid(JsonObject feature) {
		if (feature == null || !feature.has("geometry") || !feature.get("geometry").isJsonObject()) {
			return Coordinate.NONE;
		}
		return getGeometryCentroid(feature.getAsJsonObject("geometry"));
	}

	private Coordinate getGeometryCentroid(JsonObject geometry) {
		if (geometry == null || !geometry.has("type") || !geometry.get("type").isJsonPrimitive()) {
			return Coordinate.NONE;
		}
		final List<Coordinate> coordinates = new ArrayList<>();
		collectCoordinatesFromGeometry(geometry, coordinates);

		if (coordinates.isEmpty()) {
			return Coordinate.NONE;
		}
		return averageCoordinates(coordinates);
	}

	private void collectCoordinatesFromGeometry(JsonObject geometry, List<Coordinate> collector) {
		if (geometry == null || !geometry.has("type") || !geometry.get("type").isJsonPrimitive()) {
			return;
		}
		final String geometryType = geometry.get("type").getAsString();

		if ("GeometryCollection".equalsIgnoreCase(geometryType) && geometry.has("geometries")
				&& geometry.get("geometries").isJsonArray()) {
			for (JsonElement child : geometry.getAsJsonArray("geometries")) {
				if (child.isJsonObject()) {
					collectCoordinatesFromGeometry(child.getAsJsonObject(), collector);
				}
			}
			return;
		}

		if (geometry.has("coordinates")) {
			collectCoordinatesRecursive(geometry.get("coordinates"), collector);
		}
	}

	private void collectCoordinatesRecursive(JsonElement coordinatesElement, List<Coordinate> collector) {
		if (coordinatesElement == null || !coordinatesElement.isJsonArray()) {
			return;
		}
		final JsonArray array = coordinatesElement.getAsJsonArray();
		if (isCoordinatePair(array)) {
			collector.add(new Coordinate(array.get(1).getAsDouble(), array.get(0).getAsDouble()));
			return;
		}

		for (JsonElement child : array) {
			collectCoordinatesRecursive(child, collector);
		}
	}

	private boolean isCoordinatePair(JsonArray array) {
		return array.size() >= 2 && array.get(0).isJsonPrimitive() && array.get(1).isJsonPrimitive()
				&& array.get(0).getAsJsonPrimitive().isNumber() && array.get(1).getAsJsonPrimitive().isNumber();
	}

	private Coordinate averageCoordinates(List<Coordinate> coordinates) {
		double lat = 0.0;
		double lon = 0.0;
		for (Coordinate coordinate : coordinates) {
			lat += coordinate.latitude;
			lon += coordinate.longitude;
		}
		return new Coordinate(lat / coordinates.size(), lon / coordinates.size());
	}

	private double haversineMeters(double latitudeA, double longitudeA, double latitudeB, double longitudeB) {
		final double latitudeDelta = Math.toRadians(latitudeB - latitudeA);
		final double longitudeDelta = Math.toRadians(longitudeB - longitudeA);
		final double base = Math.pow(Math.sin(latitudeDelta / 2.0), 2)
				+ Math.cos(Math.toRadians(latitudeA)) * Math.cos(Math.toRadians(latitudeB))
						* Math.pow(Math.sin(longitudeDelta / 2.0), 2);
		return 2.0 * EARTH_RADIUS_METERS * Math.asin(Math.sqrt(base));
	}

	private void validateParameters() {
		if (this.centerLatitude == null || this.centerLongitude == null || this.radiusMeters == null) {
			throw new IllegalArgumentException(
					"GeoJsonFilterOperation requires centerLatitude, centerLongitude and radiusMeters.");
		}
		if (this.centerLatitude < -90.0 || this.centerLatitude > 90.0) {
			throw new IllegalArgumentException("centerLatitude must be in range [-90, 90].");
		}
		if (this.centerLongitude < -180.0 || this.centerLongitude > 180.0) {
			throw new IllegalArgumentException("centerLongitude must be in range [-180, 180].");
		}
		if (this.radiusMeters <= 0.0) {
			throw new IllegalArgumentException("radiusMeters must be > 0.");
		}
	}

	private static final class Coordinate {
		static final Coordinate NONE = new Coordinate(Double.NaN, Double.NaN);

		private final double latitude;
		private final double longitude;

		private Coordinate(double latitude, double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
		}
	}
}
