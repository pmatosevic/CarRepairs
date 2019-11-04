package org.infiniteam.autoservice.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class RegularServiceJob extends ServiceJob {

    private int kilometers;

    private boolean malfunctionsObserved;

}
