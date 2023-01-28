package com.bearcode.ovf.actions.questionnaire.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.actions.questionnaire.admin.forms.MigrationContext;
import com.bearcode.ovf.service.MigrationService;
import com.bearcode.ovf.tools.MigrationDealer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Date: 19.12.11
 * Time: 21:22
 *
 * @author Leonid Ginzburg
 */
@Controller
public class MigrationController extends BaseController {

    @Autowired
    private MigrationDealer migrationDealer;

    @Autowired
    private MigrationService migrationService;

    public MigrationController() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/QuestionnaireMigration.jsp" );
        setPageTitle( "Questionnaire Migration" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @ModelAttribute("migrationContext")
    public MigrationContext getContext() {
        return new MigrationContext();
    }

    @ModelAttribute("databaseConflict")
    public boolean checkConflict() {
        return migrationService.checkConflicts();
    }

    @RequestMapping(value = "/admin/QuestionnaireMigration.htm", method = RequestMethod.GET)
    public String handleGetMigration( HttpServletRequest request,
                                      ModelMap model ) {
        return buildModelAndView( request, model );
    }


    @RequestMapping(value = "/admin/QuestionnaireMigration.htm", method = RequestMethod.POST)
    public String handlePostMigration( HttpServletRequest request,
                                       ModelMap model,
                                       @RequestParam("migrationZip") MultipartFile zipFile,
                                       @ModelAttribute("migrationContext") MigrationContext context ) {
        try {

            if ( !zipFile.isEmpty() ) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();

                if ( zipFile.getOriginalFilename().matches( ".*\\.json" ) ) {
                    out.write( zipFile.getBytes() );

                } else {
                    ZipInputStream zipIn = new ZipInputStream( zipFile.getInputStream() );
                    ZipEntry entry = zipIn.getNextEntry();

                    byte[] buff = new byte[1024];
                    int len;
                    while ( (len = zipIn.read( buff )) > 0 ) {
                        out.write( buff, 0, len );
                    }
                    zipIn.close();
                }

                Gson gson = new Gson();
                context = gson.fromJson( out.toString(), MigrationContext.class );

                migrationDealer.doImport( context );
                if ( context.getMessages().isEmpty() ) {
                    context.getMessages().add( "Migration has been successfully finished." );
                } else {
                    context.getMessages().add( "Migration has been finished with some issues." );
                }
            }
        } catch ( Exception e ) {
            logger.error( "Can't complete migration. ", e );
            context.getMessages().add( "Migration process was not finished. " + e.getMessage() );
        }
        model.put( "migrationContext", context );
        return buildModelAndView( request, model );
    }

    @RequestMapping(value = "/admin/GetMigrationFile.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<byte[]> doExport( @ModelAttribute("migrationContext") MigrationContext context,
                                            @RequestParam(value = "fileType", required = false, defaultValue = "zip") String fileType ) throws Exception {

        migrationDealer.doExport( context );

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson( context );

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        SimpleDateFormat df = new SimpleDateFormat( "yyyy_MM_dd" );
        String flowName = context.getPageType() == null ? "all" : context.getPageType().name().toLowerCase();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( new MediaType( "application", "force-download" ) );

        if ( fileType.equalsIgnoreCase( "json" ) ) {
            out.write( json.getBytes() );

            headers.add( "Content-Disposition", String.format( "attachment; filename=migration_%s_%s.json", flowName, df.format( new Date() ) ) );
        } else {
            ZipOutputStream zip = new ZipOutputStream( out );

            zip.putNextEntry( new ZipEntry( "migrationContext.json" ) );
            zip.write( json.getBytes() );
            zip.closeEntry();

            zip.close();
            out.close();

            headers.add( "Content-Disposition", String.format( "attachment; filename=migration_%s_%s.zip", flowName, df.format( new Date() ) ) );
            headers.add( "Content-Transfer-Encoding", "binary" );
        }


        return new ResponseEntity<byte[]>( out.toByteArray(), headers, HttpStatus.CREATED );
    }

    @RequestMapping(value = "/admin/QuestionnaireMigration.htm", method = RequestMethod.POST, params = "clearConflicts")
    public String clearConflicts( HttpServletRequest request, ModelMap model ) {
        migrationService.deleteConflicts();
        model.put( "databaseConflict", checkConflict() );
        return buildModelAndView( request, model );
    }
}
