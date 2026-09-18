package ca.concordia.encs.citydata.producers;

import ca.concordia.encs.citydata.core.implementations.XLSXProducer;
import ca.concordia.encs.citydata.core.utils.RequestOptions;

/**
 * XLSX equivalent of CSVCalibrationProducer.
 *
 * Worksheet rows are exposed as comma-separated strings, allowing this
 * producer to use the same operations as CSVCalibrationProducer.
 */
public class XLSXCalibrationProducer extends XLSXProducer {

    public XLSXCalibrationProducer(String filePath) {
        super(filePath);
    }

    public XLSXCalibrationProducer(
            String filePath,
            RequestOptions fileOptions) {
        super(filePath, fileOptions);
    }
}