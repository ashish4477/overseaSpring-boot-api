package com.bearcode.ovf.model.questionnaire;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Sep 18, 2007
 * Time: 4:42:32 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@Table(name = "pdf_fillings")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class PdfFilling extends Related {
    private static final long serialVersionUID = 6985701263753390835L;

    @Column(name = "name")
    private String name = "";

    @Column(name = "text")
    private String text = "";

    @Column(name = "in_pdf_name")
    private String inPdfName = "";


    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getInPdfName() {
        return inPdfName;
    }

    public void setInPdfName( String inPdfName ) {
        this.inPdfName = inPdfName;
    }

    public String getText() {
        return text;
    }

    public void setText( String text ) {
        this.text = text;
    }

}
