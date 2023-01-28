package com.bearcode.ovf.model.migration;

import java.util.Collection;

/**
 * Date: 22.12.11
 * Time: 14:21
 *
 * @author Leonid Ginzburg
 */
public interface RelatedFacade {
    public long getId();
    public String getTitle();
    public Collection<DependencyFacade> getDependencies();
}
