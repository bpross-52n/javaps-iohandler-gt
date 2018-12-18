/*
 * Copyright (C) 2007 - 2018 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *       • Apache License, version 2.0
 *       • Apache Software License, version 1.0
 *       • GNU Lesser General Public License, version 3
 *       • Mozilla Public License, versions 1.0, 1.1 and 2.0
 *       • Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * As an exception to the terms of the GPL, you may copy, modify,
 * propagate, and distribute a work formed by combining 52°North WPS
 * GeoTools Modules with the Eclipse Libraries, or a work derivative of
 * such a combination, even if such copying, modification, propagation, or
 * distribution would otherwise violate the terms of the GPL. Nothing in
 * this exception exempts you from complying with the GPL in all respects
 * for all of the code used other than the Eclipse Libraries. You may
 * include this exception and its grant of permissions when you distribute
 * 52°North WPS GeoTools Modules. Inclusion of this notice with such a
 * distribution constitutes a grant of such permissions. If you do not wish
 * to grant these permissions, remove this paragraph from your
 * distribution. "52°North WPS GeoTools Modules" means the 52°North WPS
 * modules using GeoTools functionality - software licensed under version 2
 * or any later version of the GPL, or a work based on such software and
 * licensed under the GPL. "Eclipse Libraries" means Eclipse Modeling
 * Framework Project and XML Schema Definition software distributed by the
 * Eclipse Foundation and licensed under the Eclipse Public License Version
 * 1.0 ("EPL"), or a work based on such software and licensed under the EPL.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.wps.io.test.datahandler.generator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.n52.javaps.gt.io.data.binding.complex.GTVectorDataBinding;
import org.n52.javaps.gt.io.datahandler.generator.GML3BasicGenerator;
import org.n52.javaps.gt.io.datahandler.parser.GML3BasicParser;
import org.n52.javaps.io.DecodingException;
import org.n52.javaps.io.EncodingException;
import org.n52.javaps.test.AbstractTestCase;
import org.n52.shetland.ogc.wps.Format;

public class GML3BasicGeneratorTest extends AbstractTestCase {

    @Inject
    private GML3BasicParser theParser;

    @Inject
    private GML3BasicGenerator dataHandler;

    @Test
    public void testParser() {

        InputStream input = getResource("spearfish_restricted_sites_gml3.xml");

        Format format = new Format("text/xml; subtype=gml/3.1.1", StandardCharsets.UTF_8,
                "http://schemas.opengis.net/gml/3.1.1/base/feature.xsd");

        GTVectorDataBinding theBinding = null;
        try {
            theBinding = (GTVectorDataBinding) theParser.parse(null, input, format);
        } catch (IOException | DecodingException e) {
            fail(e.getMessage());
        }

        assertNotNull(theBinding);

        try {
            InputStream resultStream = dataHandler.generate(null, theBinding, format);

            GTVectorDataBinding parsedGeneratedBinding = (GTVectorDataBinding) theParser.parse(null, resultStream,
                    format);

            Assert.assertNotNull(parsedGeneratedBinding.getPayload());
            Assert.assertTrue(theBinding.getPayload().size() == theBinding.getPayload().size());
            Assert.assertTrue(parsedGeneratedBinding.getPayloadAsShpFile().exists());
            Assert.assertTrue(!parsedGeneratedBinding.getPayload().isEmpty());

        } catch (IOException | EncodingException | DecodingException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

    }

}