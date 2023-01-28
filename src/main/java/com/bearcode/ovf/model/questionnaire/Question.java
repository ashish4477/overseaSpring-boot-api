package com.bearcode.ovf.model.questionnaire;

import com.bearcode.ovf.model.common.DependentRoot;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 21, 2007
 * Time: 6:54:37 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@Table(name = "questions")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Question implements DependentRoot, Serializable {

    private static final long serialVersionUID = -1735848255732587228L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "page_id")
    @NotNull
    private QuestionnairePage page;

    @Column(name = "order")
    @Min(1)
    private int order = 1;
    @Transient
    private int oldOrder;

    @Column(name = "name")
    @NotBlank(message = "{rava.admin.fieldtype.empty_name}")
    private String name = "";

    @Column(name = "title")
    private String title = "";

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private Collection<QuestionVariant> variants;

    // decorations
    private String htmlClassFieldset = "";

    private String htmlClassOption = "";

    @Transient
    private boolean exportToPdf = false;
    @Transient
    private int numberInPdf;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public QuestionnairePage getPage() {
        return page;
    }

    public void setPage( QuestionnairePage page ) {
        this.page = page;
    }

    public int getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public void setOrder( int order ) {
        this.order = order;
    }

    public Collection<QuestionVariant> getVariants() {
        return variants;
    }

    public void setVariants( Collection<QuestionVariant> variants ) {
        this.variants = variants;
    }

    public int getOldOrder() {
        return oldOrder;
    }

    public void setOldOrder( int oldOrder ) {
        this.oldOrder = oldOrder;
    }


    public boolean isExportToPdf() {
        return exportToPdf;
    }

    public void setExportToPdf( boolean exportToPdf ) {
        this.exportToPdf = exportToPdf;
    }

    public int getNumberInPdf() {
        return numberInPdf;
    }

    public void setNumberInPdf( int numberInPdf ) {
        this.numberInPdf = numberInPdf;
    }

    public String getHtmlClassFieldset() {
        return htmlClassFieldset;
    }

    public void setHtmlClassFieldset( String htmlClassFieldset ) {
        this.htmlClassFieldset = htmlClassFieldset;
    }

    public String getHtmlClassOption() {
        return htmlClassOption;
    }

    public void setHtmlClassOption( String htmlClassOption ) {
        this.htmlClassOption = htmlClassOption;
    }

    public QuestionField getKeyField() {
        if ( variants.isEmpty() ) return null;
        QuestionVariant firstVariant = variants.iterator().next();
        if ( firstVariant.getFields().isEmpty() ) return null;
        return firstVariant.getFields().iterator().next();
    }
}
