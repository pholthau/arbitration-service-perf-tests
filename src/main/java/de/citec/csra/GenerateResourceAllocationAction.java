/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.csra;

import java.util.Random;
import java.util.UUID;
import rsb.performance.test.actions.ActionSupport;
import rsb.performance.test.parameters.ParameterSet;
import rsb.performance.test.util.ExecutionException;
import rsb.performance.test.util.SpecificationException;
import rst.communicationpatterns.ResourceAllocationType.ResourceAllocation;
import rst.communicationpatterns.ResourceAllocationType.ResourceAllocation.Initiator;
import rst.communicationpatterns.ResourceAllocationType.ResourceAllocation.Policy;
import rst.communicationpatterns.ResourceAllocationType.ResourceAllocation.Priority;
import static rst.communicationpatterns.ResourceAllocationType.ResourceAllocation.State.REQUESTED;
import rst.timing.IntervalType.Interval;
import rst.timing.TimestampType.Timestamp;

/**
 *
 * @author pholthau
 */
public class GenerateResourceAllocationAction extends ActionSupport<Object> {


	private static final Random RANDOM = new Random();
	private static final int DELAY = 5000;
	private static final int DURATION = 5000;
	private static final String[] RESOURCES = {
		"a", "b", "c", "d", "e", "f", "g", "h",
		"i", "j", "k", "l", "m", "n", "o", "p",
		"q", "r", "s", "t", "u", "v", "w", "x"
	};

	@Override
	public Object execute(final ParameterSet parameters) throws	SpecificationException, ExecutionException, InterruptedException {
		return ResourceAllocation.newBuilder().
				setId(UUID.randomUUID().toString()).
				setState(REQUESTED).
				setInitiator(Initiator.values()[RANDOM.nextInt(Initiator.values().length)]).
				setPolicy(Policy.values()[RANDOM.nextInt(Policy.values().length)]).
				setPriority(Priority.values()[RANDOM.nextInt(Priority.values().length)]).
				setSlot(
					Interval.newBuilder().
						setBegin(Timestamp.newBuilder().setTime(System.currentTimeMillis() + RANDOM.nextInt(DELAY))).
						setEnd(Timestamp.newBuilder().setTime(System.currentTimeMillis() + RANDOM.nextInt(DURATION)))
				).
				addResourceIds(RESOURCES[RANDOM.nextInt(RESOURCES.length)]).
				addResourceIds(RESOURCES[RANDOM.nextInt(RESOURCES.length)]).
				addResourceIds(RESOURCES[RANDOM.nextInt(RESOURCES.length)]).
				build();
	}

}
