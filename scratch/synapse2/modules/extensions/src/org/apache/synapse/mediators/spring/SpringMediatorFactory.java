package org.apache.synapse.mediators.spring;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.apache.synapse.api.Mediator;
import org.apache.synapse.xml.AbstractMediatorFactory;
import org.apache.synapse.xml.Constants;
import org.apache.synapse.SynapseEnvironment;
import org.apache.synapse.SynapseException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMAttribute;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ByteArrayResource;



/**
 *
 * @see org.apache.synapse.mediators.base.builtin.xslt.XSLTProcessor
 * <p> This class configures the Spring mediator type. 
 * <p> The tag looks like this
 * <xmp>
 * <spring:springmediator name="x" bean="beanname">
 * 		<beans> 
 * 			spring config here
 *  	</beans>
 * </xmp>
 * The spring config is inlined (future work to let it be pointed to with an attribute). The bean attribute identifies
 * the bean inside the spring assembly to be used. 
 */
public class SpringMediatorFactory extends AbstractMediatorFactory {
	private static final QName tagName = new QName(Constants.SYNAPSE_NAMESPACE+"/spring", "springmediator");
	public Mediator createMediator(SynapseEnvironment se, OMElement el) {
		SpringMediator sm = new SpringMediator();
		super.setNameOnMediator(se,el,sm);
		
		OMAttribute bean = el.getAttribute(new QName("bean"));
		if (bean == null) throw new SynapseException("missing bean attribute on "+el.toString());
		
		sm.setBeanName(bean.getAttributeValue().trim());
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			baos.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE beans PUBLIC \"-//SPRING//DTD BEAN//EN\" \"http://www.springframework.org/dtd/spring-beans.dtd\">"
							.getBytes());
			XMLStreamWriter xsw = XMLOutputFactory.newInstance().createXMLStreamWriter(baos);
			OMElement beans = null;
			Iterator it = el.getChildElements();
			while (it.hasNext()) {
				OMElement ths = (OMElement)it.next();
				if (ths.getLocalName().toLowerCase().equals("beans")) {
					beans = ths;
					break;
				}
			}
			if (beans==null) throw new SynapseException("<beans> element not found in "+el.toString());
            if (beans.getNamespace() != null)
                xsw.setDefaultNamespace(beans.getNamespace().getName());

            beans.serialize(xsw);
		} catch (Exception e) {
			throw new SynapseException(e);
		} 
		
				
		GenericApplicationContext ctx = new GenericApplicationContext();
		XmlBeanDefinitionReader xbdr = new XmlBeanDefinitionReader(
				ctx);
		xbdr.setValidating(false);
		xbdr.loadBeanDefinitions(new ByteArrayResource(baos.toByteArray()));
		ctx.setClassLoader(se.getClassLoader());
		ctx.refresh();
		sm.setContext(ctx);
		return sm;
		
		
		
	}

	public QName getTagQName() {
		
		return tagName;
	}

}
