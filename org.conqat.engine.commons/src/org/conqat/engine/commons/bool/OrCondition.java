/*-------------------------------------------------------------------------+
|                                                                          |
| Copyright 2005-2011 The ConQAT Project                                   |
|                                                                          |
| Licensed under the Apache License, Version 2.0 (the "License");          |
| you may not use this file except in compliance with the License.         |
| You may obtain a copy of the License at                                  |
|                                                                          |
|    http://www.apache.org/licenses/LICENSE-2.0                            |
|                                                                          |
| Unless required by applicable law or agreed to in writing, software      |
| distributed under the License is distributed on an "AS IS" BASIS,        |
| WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. |
| See the License for the specific language governing permissions and      |
| limitations under the License.                                           |
+-------------------------------------------------------------------------*/
package org.conqat.engine.commons.bool;

import org.conqat.engine.core.core.AConQATAttribute;
import org.conqat.engine.core.core.AConQATParameter;
import org.conqat.engine.core.core.AConQATProcessor;

/**
 * {@ConQAT.Doc}
 * 
 * @author $Author: pfaller $
 * @version $Rev: 37404 $
 * @ConQAT.Rating GREEN Hash: A2850AFAB8D6ECD5B3489F65B388C6C3
 */
@AConQATProcessor(description = "Returns the disjunction of the input values.")
public class OrCondition extends ConditionBase {

	/** The result. */
	private boolean result = false;

	/** {@ConQAT.Doc} */
	@AConQATParameter(name = "input", minOccurrences = 1, description = "The boolean input.")
	public void addValue(
			@AConQATAttribute(name = "value", description = "true or false") boolean value) {
		result = result || value;
	}

	/** {@inheritDoc} */
	@Override
	protected boolean evaluateCondition() {
		return result;
	}
}