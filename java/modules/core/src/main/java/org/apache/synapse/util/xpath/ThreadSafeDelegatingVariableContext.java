/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.synapse.util.xpath;

import org.jaxen.SimpleVariableContext;
import org.jaxen.UnresolvableException;
import org.jaxen.VariableContext;

public class ThreadSafeDelegatingVariableContext implements VariableContext {
    
    private final ThreadLocal<VariableContext> delegate = new ThreadLocal<VariableContext>();

    public ThreadSafeDelegatingVariableContext() {
        this.delegate.set(new SimpleVariableContext());
    }

    public void setDelegate(VariableContext delegate) {
        this.delegate.set(delegate);
    }

    public VariableContext getDelegate() {
        return this.delegate.get();
    }

    public Object getVariableValue(String namespaceURI, String prefix, String localName)
            throws UnresolvableException {
        
        return delegate.get().getVariableValue(namespaceURI, prefix, localName);
    }
}
