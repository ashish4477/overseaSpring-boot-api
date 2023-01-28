package com.bearcode.ovf.tools.pendingregistration;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.Date;

/**
 * Date: 20.10.14
 * Time: 23:02
 *
 * @author Leonid Ginzburg
 */
@Component
public class OnlineDataTransferCache {

    @Cacheable(value = "dataTransferStatus", key = "#id")
    public DataPreparationStatus createStatus( String id ) {
        DataPreparationStatus status = new DataPreparationStatus();
        status.setId( id );
        return status;
    }

    @CachePut(value = "dataTransferStatus", key = "#status.id")
    public DataPreparationStatus updateStatus( DataPreparationStatus status ) {
        return status;
    }


    public String createStatusId( ) {
        StringBuilder sb = new StringBuilder();
        sb.append( new Date() ).append( Math.random() );
        return DigestUtils.md5DigestAsHex( sb.toString().getBytes() );
    }

    @CachePut(value = "PvrDataCsv", key = "#id")
    public byte[] createCsvData(byte[] csv, String id ) {
        return csv;
    }

    @Cacheable(value = "PvrDataCsv", key = "#id")
    public byte[] getCsvData( String id) {
        return new byte[0];
    }
}
