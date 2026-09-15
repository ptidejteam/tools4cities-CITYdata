package ca.concordia.encs.citydata.core.implementations;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ca.concordia.encs.citydata.core.contracts.IProducer;
import ca.concordia.encs.citydata.core.exceptions.MiddlewareException;
import ca.concordia.encs.citydata.core.utils.RequestOptions;

/**
 * JPGProducer : fetches Jpeg images from any source, this one is specific to hub data
 *
 * @author Vinicus Miotto, Minette Zongo
 * @since 2026-09-15
 */

public non-sealed class JPGProducer extends AbstractProducer<byte[]> implements IProducer<byte[]> {

    public JPGProducer(final String filePath, final RequestOptions fileOptions) {
        super(filePath, fileOptions);
    }

    public JPGProducer(final String filePath) {
        super(filePath);
    }

    @Override
    public void fetch() {
        beforeFetch();
       // ArrayList<JsonObject> results = new ArrayList<>();

        try (InputStream inputStream = obtainInputStream()) {

            this.setResult(new ArrayList<>(List.of(inputStream.readAllBytes())));
            this.applyOperation();

        } catch (IOException e) {
            throw new MiddlewareException.DatasetNotFound("Error processing JPG data");
        }  

    }
    
    @Override 
    public boolean isBinary() { 
    	return true; 
    }
    
    @Override 
    public String getFileExtension() { 
    	return "jpg"; 
    }

    // For authorization checks - if the user has the right to access a specific producer. Implemented within the producers
    protected void beforeFetch() {

    }

    protected InputStream obtainInputStream() {
        return this.fetchStream();
    }


}
