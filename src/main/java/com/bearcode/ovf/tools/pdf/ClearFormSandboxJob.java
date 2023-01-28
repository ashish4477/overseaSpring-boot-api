package com.bearcode.ovf.tools.pdf;

import com.bearcode.ovf.model.questionnaire.PdfFormTrack;
import com.bearcode.ovf.service.PdfFormTrackService;
import org.apache.commons.io.filefilter.AgeFileFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;
import java.util.Calendar;
import java.util.List;

/**
 * Periodically running job to clear old info and files.
 * Removes records from PdfFormTrack and associated files
 *
 * Date: 10.10.14
 * Time: 0:00
 *
 * @author Leonid Ginzburg
 */
//@Component
public class ClearFormSandboxJob {

    @Value("${clearFormSandboxJob.storeMinutes:10}")
    private int storeMinutes;

    @Autowired
    PdfFormTrackService pdfFormTrackService;

    public void clearSandbox() {
        Calendar expiredDate = Calendar.getInstance();
        expiredDate.add( Calendar.MINUTE, -1*storeMinutes );
        // look for old tracks, created earlier than 5 minutes ago
        List<PdfFormTrack> tracks = pdfFormTrackService.findOldTracks( expiredDate.getTime() );
        File formDir = null;
        for ( PdfFormTrack track : tracks ) {
            // remove the track and the file
            if ( !track.getFormFileName().isEmpty() ) {
                File form = new File( track.getFormFileName() );
                if ( form.exists() ) {
                    if ( formDir == null ) {
                        formDir = form.getParentFile();
                    }
                    form.delete();
                }
            }
            pdfFormTrackService.removeFormTrack( track );
        }
        if ( formDir != null ) {
            // look for any old files in sandbox directory
            FileFilter fileFilter = new AgeFileFilter( expiredDate.getTime(), true );
            File[] listFiles = formDir.listFiles( fileFilter );
            for ( File form : listFiles ) {
                form.delete();
            }
        }
    }

    public void setStoreMinutes(int storeMinutes) {
        this.storeMinutes = storeMinutes;
    }
}
