package com.bearcode.ovf.service;

import com.bearcode.ovf.DAO.PdfFormTrackDAO;
import com.bearcode.ovf.model.questionnaire.PdfFormTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Date: 06.10.14
 * Time: 22:16
 *
 * @author Leonid Ginzburg
 */
@Service
public class PdfFormTrackService {
    @Autowired
    private PdfFormTrackDAO pdfFormTrackDAO;

    public void saveFormTrack( PdfFormTrack track ) {
        pdfFormTrackDAO.makePersistent( track );
    }

    public void removeFormTrack ( PdfFormTrack track ) {
        pdfFormTrackDAO.makeTransient( track );
    }

    public PdfFormTrack findFormTrack( long id ) {
        return pdfFormTrackDAO.findById( id );
    }

    public PdfFormTrack findFormTrackByHash(String hash) {
        List<PdfFormTrack> tracks = pdfFormTrackDAO.findByHash( hash );
        if ( tracks.size() == 1 ) {
            return tracks.get(0);
        }
        return null;
    }

    public List<PdfFormTrack> findOldTracks(Date expiredDateTime) {
        return pdfFormTrackDAO.findOldTracks( expiredDateTime );
    }

    public File findTrackedFile( Long id ) {
        PdfFormTrack track = null;
        if ( id != null ) {
            track = findFormTrack( id );
        }
        File file = null;
        if ( track != null ) {
            file = new File( track.getFormFileName() );
        }
        if ( file == null || !file.exists() || file.length() == 0 ) {
            return  null;
        }
        return file;
    }
}
