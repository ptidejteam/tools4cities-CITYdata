package ca.concordia.encs.citydata.core.contracts;

import java.util.ArrayList;

import ca.concordia.encs.citydata.core.exceptions.MiddlewareException;

/**
 *
 * The Producer entity is responsible for: - Fetching data - Applying
 * operation on the result - Notify observers when both tasks are done
 * Refactored by adding JPG files management. By default, all the other producers manage 
 * non binary files, only JPGProducer will override isBinary with true
 *
 * @author Gabriel C. Ullmann, Minette Zongo
 * @since 2024-12-01, 2026-09-15
 */

public interface IProducer<E> {

	// 1 - prepare producer
	void addObserver(final IRunner aRunner);

	@SuppressWarnings("rawtypes")
	void setOperation(IOperation operation);

	// 2 - fetch data
	void fetch() throws MiddlewareException;

	// 3 - transform data and notify when done
	void applyOperation();

	void notifyObservers();

	// 4 - output data
	ArrayList<E> getResult();
	
	// Allow JPG files management
	default boolean isBinary() { return false; }
	default String getFileExtension() { return "bin"; }

}