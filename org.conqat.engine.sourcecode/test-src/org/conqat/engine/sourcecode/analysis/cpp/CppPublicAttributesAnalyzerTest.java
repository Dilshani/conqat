/*-------------------------------------------------------------------------+
|                                                                          |
| Copyright 2005-2011 the ConQAT Project                                   |
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
package org.conqat.engine.sourcecode.analysis.cpp;

import org.conqat.engine.sourcecode.analysis.FindingsTokenTestCaseBase;
import org.conqat.engine.sourcecode.resource.ITokenElement;
import org.conqat.lib.scanner.ELanguage;

/**
 * Test for {@link CppPublicAttributesAnalyzer}.
 * 
 * @author kanis@cqse.eu
 * @author $Author: hummelb $
 * @version $Rev: 39841 $
 * @ConQAT.Rating GREEN Hash: F01F07A62721E70CFCEF05BFB1049EF7
 */
public class CppPublicAttributesAnalyzerTest extends
		FindingsTokenTestCaseBase {

	/** Constructor. */
	public CppPublicAttributesAnalyzerTest() {
		super(CppPublicAttributesAnalyzer.class, ELanguage.CPP);
	}

	/** Test analyzer. */
	public void test() throws Exception {
		ITokenElement element = executeProcessor("PublicAttributes.cpp");

		assertFinding(element, 7);
		assertFinding(element, 41);
		assertFindingCount(element, 2);
	}

}
