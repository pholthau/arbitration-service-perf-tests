/**
 * ============================================================
 *
 * This file is a part of the
 * rsb-java-performance-test-client project
 *
 * Copyright (C) 2015 CoR-Lab, Bielefeld University
 *
 * This program is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation;
 * either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * The development of this software was supported by:
 *   CoR-Lab, Research Institute for Cognition and Robotics
 *     Bielefeld University
 *
 * ============================================================
 */
package de.citec.csra;

import rsb.Factory;
import rsb.Informer;
import rsb.Scope;
import rsb.converter.DefaultConverterRepository;
import rsb.converter.ProtocolBufferConverter;
import rsb.performance.test.RuntimeInformation;
import rsb.performance.test.TestCase;
import rsb.performance.test.TestPhase;
import rsb.performance.test.actions.data.StaticData;
import rsb.performance.test.actions.rsb.DynamicEvent;
import rsb.performance.test.actions.rsb.InformerAction;
import rsb.performance.test.actions.rsb.ParticipantValue;
import rsb.performance.test.actions.rsb.ScopeValue;
import rsb.performance.test.actions.timing.FixedRateTiming;
import rsb.performance.test.parameters.Parameter;
import rsb.performance.test.parameters.ParameterProduct;
import rsb.performance.test.parameters.values.DoubleRange;
import rsb.performance.test.repository.TestCasePlugin;
import rst.communicationpatterns.ResourceAllocationType.ResourceAllocation;

/**
 * @author jwienke
 */
@TestCasePlugin
public final class ResponseTimeTest extends TestCase {

	private static final Parameter<Double> FREQUENCY = new Parameter<>("frequency", Double.class);
	private final Informer<Object> informer;

	public ResponseTimeTest(final String name, final RuntimeInformation runtimeInformation) throws Exception {
		super(name, runtimeInformation,
				new ParameterProduct(new ParameterProduct.ParameterRange<Double>(FREQUENCY, new DoubleRange(1.0, 100.0, 10.0))));

		DefaultConverterRepository.getDefaultConverterRepository().addConverter(new ProtocolBufferConverter<>(ResourceAllocation.getDefaultInstance()));

		this.informer = Factory.getInstance().createInformer("/coordination/allocation/");
		this.informer.activate();

		final TestPhase firePhase = new TestPhase(
				"randomrequestphase",
				new FixedRateTiming(
						new InformerAction(
								new StaticData<>(new ParticipantValue<>(this.informer)),
								new DynamicEvent(
										new StaticData<>(new ScopeValue(new Scope("/coordination/allocation/"))),
										StaticData.newClass(ResourceAllocation.class),
										new GenerateResourceAllocationAction()
								)
						),
						FREQUENCY,
						StaticData.newNumber(5000L)
				)
		);

		this.addTestPhase(firePhase);

	}

	@Override
	public void cleanup() throws Exception {
		this.informer.deactivate();
	}

}
