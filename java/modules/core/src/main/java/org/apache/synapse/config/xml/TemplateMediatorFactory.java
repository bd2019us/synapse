/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.SynapseException;
import org.apache.synapse.mediators.template.TemplateMediator;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
/**
 * Factory class for Template configuration as follows
 * <template name="simple_func">
	    <parameter name="p1"/>
        <parameter name="p2"/>*
        <mediator/>+
    </template>
 */
public class TemplateMediatorFactory extends AbstractListMediatorFactory {
    private static final QName TEMPLATE_Q
            = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "template");
    private TemplateMediator templateTemplateMediator;
    private OMElement templateElem;
    /**
     * Element  QName Definitions
     */
    public static final QName PARAMETER_Q = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "parameter");


    protected Mediator createSpecificMediator(OMElement elem, Properties properties) {
        templateTemplateMediator = new TemplateMediator();
        templateElem = elem;
        OMAttribute nameAttr = templateElem.getAttribute(ATT_NAME);
        OMAttribute errorHandlerAttr = templateElem.getAttribute(ATT_ONERROR);
        if (nameAttr != null) {
            templateTemplateMediator.setName(nameAttr.getAttributeValue());
            if (errorHandlerAttr != null) {
                templateTemplateMediator.setErrorHandler(errorHandlerAttr.getAttributeValue());
            }
            processAuditStatus(templateTemplateMediator, templateElem);
            initParameters();
            addChildren(templateElem, templateTemplateMediator, properties);
        } else {
            String msg = "A EIP template should be a named mediator .";
            log.error(msg);
            throw new SynapseException(msg);
        }
        return templateTemplateMediator;
    }

    private void initParameters() {
        Iterator subElements = templateElem.getChildElements();
        Collection<String> paramNames = new ArrayList<String>();
        while (subElements.hasNext()) {
            OMElement child = (OMElement) subElements.next();
            if (child.getQName().equals(PARAMETER_Q)) {
                OMAttribute paramNameAttr = child.getAttribute(ATT_NAME);
                if (paramNameAttr != null) {
                    paramNames.add(paramNameAttr.getAttributeValue());
                }
                child.detach();
            }
        }
        templateTemplateMediator.setParameters(paramNames);
    }

    public QName getTagQName() {
        return TEMPLATE_Q;
    }

}