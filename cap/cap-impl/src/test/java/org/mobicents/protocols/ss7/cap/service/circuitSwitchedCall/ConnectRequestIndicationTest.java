/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.GenericNumberCap;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.GenericNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.OriginalCalledNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.RedirectingPartyIDCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.AlertingPatternCapImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.DestinationRoutingAddressImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.NAOliInfoImpl;
import org.mobicents.protocols.ss7.inap.isup.CallingPartysCategoryInapImpl;
import org.mobicents.protocols.ss7.inap.isup.RedirectionInformationInapImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingCategory;
import org.mobicents.protocols.ss7.map.primitives.AlertingPatternImpl;
import org.testng.*;import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class ConnectRequestIndicationTest {

	public byte[] getData1() {
		return new byte[] { 48, 9, (byte) 160, 7, 4, 5, 2, 16, 121, 34, 16 };
	}

	public byte[] getData2() {
		return new byte[] { 48, 17, (byte) 160, 7, 4, 5, 2, 16, 121, 34, 16, (byte) 174, 6, 4, 4, 1, 2, 3, 4 };
	}

	public byte[] getData3() {
		return new byte[] { 48, 75, (byte) 160, 7, 4, 5, 2, 16, 121, 34, 16, (byte) 129, 3, 0, 0, 8, (byte) 134, 6, (byte) 131, 20, 7, 1, 9, 0, (byte) 170, 18,
				48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255, (byte) 156, 1, 10, (byte) 157, 6, (byte) 131, 20, 7, 1, 9,
				0, (byte) 158, 2, 3, 97, (byte) 174, 6, 4, 4, 1, 2, 3, 4, (byte) 159, 55, 0, (byte) 159, 56, 0, (byte) 159, 57, 1, 40 };
	}

	public byte[] getDataGenericNumber() {
		return new byte[] { 1, 2, 3, 4 };
	}

	public byte[] getOriginalCalledPartyID() {
		return new byte[] { -125, 20, 7, 1, 9, 0 };
	}

	public byte[] getCallingPartysCategory() {
		return new byte[] { 10 };
	}

	public byte[] getRedirectingPartyID() {
		return new byte[] { -125, 20, 7, 1, 9, 0 };
	}

	public byte[] getRedirectionInformation() {
		return new byte[] { 3, 97 };
	}

	@Test(groups = { "functional.decode","circuitSwitchedCall"})
	public void testDecode() throws Exception {

		byte[] data = this.getData1();
		AsnInputStream ais = new AsnInputStream(data);
		ConnectRequestIndicationImpl elem = new ConnectRequestIndicationImpl();
		int tag = ais.readTag();
		elem.decodeAll(ais);
		assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().size(), 1);
		assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getInternalNetworkNumberIndicator(), 0);
		assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getNatureOfAddressIndicator(), 2);
		assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getNumberingPlanIndicator(), 1);
		assertTrue(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getAddress().equals("972201"));

		data = this.getData2();
		ais = new AsnInputStream(data);
		elem = new ConnectRequestIndicationImpl();
		tag = ais.readTag();
		elem.decodeAll(ais);
		assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().size(), 1);
		assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getInternalNetworkNumberIndicator(), 0);
		assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getNatureOfAddressIndicator(), 2);
		assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getNumberingPlanIndicator(), 1);
		assertTrue(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getAddress().equals("972201"));
		assertEquals(elem.getGenericNumbers().size(), 1);
		assertTrue(Arrays.equals(elem.getGenericNumbers().get(0).getData(), getDataGenericNumber()));

		data = this.getData3();
		ais = new AsnInputStream(data);
		elem = new ConnectRequestIndicationImpl();
		tag = ais.readTag();
		elem.decodeAll(ais);
		assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().size(), 1);
		assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getInternalNetworkNumberIndicator(), 0);
		assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getNatureOfAddressIndicator(), 2);
		assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getNumberingPlanIndicator(), 1);
		assertTrue(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getAddress().equals("972201"));
		assertEquals(elem.getGenericNumbers().size(), 1);
		assertTrue(Arrays.equals(elem.getGenericNumbers().get(0).getData(), getDataGenericNumber()));
		assertEquals(elem.getAlertingPattern().getAlertingPattern().getAlertingCategory(), AlertingCategory.Category5);
		assertTrue(Arrays.equals(elem.getOriginalCalledPartyID().getData(), getOriginalCalledPartyID()));
		assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
		assertTrue(Arrays.equals(elem.getCallingPartysCategory().getData(), getCallingPartysCategory()));
		assertTrue(Arrays.equals(elem.getRedirectingPartyID().getData(), getRedirectingPartyID()));
		assertTrue(Arrays.equals(elem.getRedirectionInformation().getData(), getRedirectionInformation()));
		assertTrue(elem.getSuppressionOfAnnouncement());
		assertTrue(elem.getOCSIApplicable());
		assertEquals((int)elem.getNAOliInfo().getValue(), 40);
	}

	@Test(groups = { "functional.encode","circuitSwitchedCall"})
	public void testEncode() throws Exception {
		
		ArrayList<CalledPartyNumberCap> calledPartyNumbers = new ArrayList<CalledPartyNumberCap>();
		CalledPartyNumberImpl cpn = new CalledPartyNumberImpl(2, "972201", 1, 2);
		CalledPartyNumberCapImpl calledPartyNumber = new CalledPartyNumberCapImpl(cpn);
		calledPartyNumbers.add(calledPartyNumber);
		DestinationRoutingAddressImpl destinationRoutingAddress = new DestinationRoutingAddressImpl(calledPartyNumbers);
		
		ConnectRequestIndicationImpl elem = new ConnectRequestIndicationImpl(destinationRoutingAddress, null, null, null, null, null, null, null, null, null,
				null, null, null, false, false, false, null, false);
		AsnOutputStream aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

		
		ArrayList<GenericNumberCap> genericNumbers = new ArrayList<GenericNumberCap>();
		GenericNumberCapImpl genericNumberCap = new GenericNumberCapImpl(getDataGenericNumber());
		genericNumbers.add(genericNumberCap);
		elem = new ConnectRequestIndicationImpl(destinationRoutingAddress, null, null, null, null, null, null, null, genericNumbers, null,
				null, null, null, false, false, false, null, false);
		aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

		
		AlertingPatternImpl alertingPattern = new AlertingPatternImpl(AlertingCategory.Category5);
		AlertingPatternCapImpl alertingPatternCap = new AlertingPatternCapImpl(alertingPattern);
		OriginalCalledNumberCapImpl originalCalledPartyID = new OriginalCalledNumberCapImpl(getOriginalCalledPartyID());
		CallingPartysCategoryInapImpl callingPartysCategory = new CallingPartysCategoryInapImpl(getCallingPartysCategory());
		RedirectingPartyIDCapImpl redirectingPartyID = new RedirectingPartyIDCapImpl(getRedirectingPartyID());
		RedirectionInformationInapImpl redirectionInformation = new RedirectionInformationInapImpl(getRedirectionInformation());
		NAOliInfoImpl naoliInfo = new NAOliInfoImpl(40);

		elem = new ConnectRequestIndicationImpl(destinationRoutingAddress, alertingPatternCap, originalCalledPartyID,
				CAPExtensionsTest.createTestCAPExtensions(), null, callingPartysCategory, redirectingPartyID, redirectionInformation, genericNumbers, null,
				null, null, null, false, true, true, naoliInfo, false);
		aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData3()));

//		DestinationRoutingAddressImpl destinationRoutingAddress, AlertingPatternCap alertingPattern,
//		OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions, Carrier carrier, CallingPartysCategoryInap callingPartysCategory,
//		RedirectingPartyIDCap redirectingPartyID, RedirectionInformationInap redirectionInformation, ArrayList<GenericNumberCap> genericNumbers,
//		ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, LocationNumberCap chargeNumber, LegID legToBeConnected, CUGInterlock cugInterlock,
//		boolean cugOutgoingAccess, boolean suppressionOfAnnouncement, boolean ocsIApplicable, NAOliInfo naoliInfo, boolean borInterrogationRequested
	}
}

