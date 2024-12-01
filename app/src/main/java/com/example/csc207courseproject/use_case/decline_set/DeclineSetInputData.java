package com.example.csc207courseproject.use_case.decline_set;

import com.example.csc207courseproject.entities.SetData;

/**
 * The Input Data for the decline set Use Case.
 */
public class DeclineSetInputData {

    private final String tag;
    private final boolean p1Applies;
    private final boolean p2Applies;
    private final SetData currentSet;

    public DeclineSetInputData(String tag, boolean p1Applies, boolean p2Applies, SetData currentSet) {
        this.tag = tag;
        this.p1Applies = p1Applies;
        this.p2Applies = p2Applies;
        this.currentSet = currentSet;
    }

    public String getTag() {return tag;}

    public boolean getP1Applies() {return p1Applies;}

    public boolean getP2Applies() {return p2Applies;}

    public SetData getCurrentSet() {return currentSet;}

}
