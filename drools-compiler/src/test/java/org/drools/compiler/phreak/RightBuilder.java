package org.drools.compiler.phreak;

import org.drools.core.common.InternalFactHandle;
import org.drools.core.common.InternalWorkingMemory;
import org.drools.core.common.PhreakPropagationContext;
import org.drools.core.common.RightTupleSets;
import org.drools.core.reteoo.RightTuple;
import org.drools.core.reteoo.RightTupleSink;
import org.drools.core.reteoo.SegmentMemory;

public class RightBuilder {
    private InternalWorkingMemory      wm;
    private RightTupleSink             sink;
    private RightTupleSets rightTuples;
    private Scenario                   scenario;

    public RightBuilder(Scenario scenario) {
        this.wm = scenario.getWorkingMemory();
        this.scenario = scenario;
        this.sink = scenario.getBetaNode();
        this.rightTuples = scenario.getRightTuples();
    }

    public RightBuilder insert(Object... objects) {
        for (Object object : objects) {
            InternalFactHandle fh = (InternalFactHandle) wm.insert(object);
            RightTuple rightTuple = new RightTuple( fh, sink );
            rightTuple.setPropagationContext( new PhreakPropagationContext() );
            rightTuples.addInsert( rightTuple );
        }
        return this;
    }

    public RightBuilder update(Object... objects) {
        for ( Object object : objects ) {
            InternalFactHandle fh = (InternalFactHandle) wm.insert( object );
            RightTuple rightTuple = fh.getFirstRightTuple();
            rightTuple.setPropagationContext( new PhreakPropagationContext() );
            rightTuples.addUpdate( rightTuple );
        }
        return this;
    }

    public RightBuilder delete(Object... objects) {
        for ( Object object : objects ) {
            InternalFactHandle fh = (InternalFactHandle) wm.insert( object );
            RightTuple rightTuple = fh.getFirstRightTuple();
            rightTuple.setPropagationContext( new PhreakPropagationContext() );
            rightTuples.addDelete( rightTuple );
        }
        return this;
    }

    RightTupleSets get() {
        return this.rightTuples;
    }

    public StagedBuilder result() {
        StagedBuilder stagedBuilder = new StagedBuilder( scenario, null );
        scenario.setExpectedResultBuilder( stagedBuilder );
        return stagedBuilder; 
    }    
    
    public StagedBuilder preStaged(SegmentMemory sm) {
        StagedBuilder stagedBuilder = new StagedBuilder( scenario, sm );
        scenario.addPreStagedBuilder( stagedBuilder );
        return stagedBuilder;        
    }    
    
    public StagedBuilder postStaged(SegmentMemory sm) {
        StagedBuilder stagedBuilder = new StagedBuilder( scenario, sm );
        scenario.addPostStagedBuilder( stagedBuilder );
        return stagedBuilder;        
    } 
    
    public Scenario run() {
        return this.scenario.run();
    }      
}
