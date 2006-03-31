package org.apache.synapse.resources.xml;

import org.apache.synapse.mediators.base.ListMediator;
import org.apache.synapse.SynapseEnvironment;
import org.apache.synapse.SynapseMessage;
/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class ResourceMediator extends ListMediator {
    protected String type;
    protected String uriRoot;
    public boolean process(SynapseEnvironment se, SynapseMessage smc) {
        // this is not a inline mediator, It's soule purpose of existence is to populate the ResourceHandler
        return true;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public void setURIRoot(String uriRoot) {
        this.uriRoot = uriRoot;
    }
    public String getURIRoot() {
        return uriRoot;
    }

    
}
