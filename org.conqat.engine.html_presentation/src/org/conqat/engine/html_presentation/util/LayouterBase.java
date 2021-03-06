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
package org.conqat.engine.html_presentation.util;

import org.conqat.engine.commons.ConQATProcessorBase;
import org.conqat.engine.core.core.AConQATAttribute;
import org.conqat.engine.core.core.AConQATParameter;
import org.conqat.engine.core.core.ConQATException;
import org.conqat.engine.html_presentation.BundleContext;
import org.conqat.engine.html_presentation.PageDescriptor;
import org.conqat.lib.commons.date.DateUtils;
import org.conqat.lib.commons.html.HTMLWriter;

/**
 * Base class for layouters.
 * 
 * @author $Author: kinnen $
 * @version $Rev: 41751 $
 * @ConQAT.Rating GREEN Hash: EFE5FA52179C138F0AF225EB116D0E52
 */
public abstract class LayouterBase extends ConQATProcessorBase {

	/** Value used for the description that implies reuse of the name. */
	private static final String COPY_DESCRIPTION = "_COPY_NAME_";

	/** The writer used for generating the body html. */
	protected HTMLWriter writer;

	/** Result description. */
	private String description;

	/** Group id. */
	private String groupId;

	/** Result name. */
	private String name;

	/** Returns the name set by {@link #setInfo(String, String, String)} */
	public String getName() {
		return name;
	}

	/** {@inheritDoc} */
	@Override
	public final PageDescriptor process() throws ConQATException {

		PageDescriptor page = new PageDescriptor(getDescription(), getName(),
				getGroupId(), getSummary(), getIconName(), getFilename());

		writer = page.getWriter();

		writer.addComment("Generated by " + getClass().getSimpleName() + " @ "
				+ DateUtils.getNow());
		setUp();
		layoutPage();
		writer.close();
		return page;
	}

	/**
	 * Template method that deriving classes can override to perform
	 * initialization tasks. The method is guaranteed to be called after the
	 * ConQAT parameters have been set by the driver and before the
	 * {@link #layoutPage()} method gets called.
	 * 
	 * Default implementation does nothing
	 */
	protected void setUp() {
		// do nothing
	}

	/** {@ConQAT.Doc} */
	@AConQATParameter(name = "info", minOccurrences = 1, maxOccurrences = 1, description = "Result information.")
	public void setInfo(
			@AConQATAttribute(name = "name", description = "Result name (short)") String name,
			@AConQATAttribute(name = "description", defaultValue = COPY_DESCRIPTION, description = "Result description (long)") String description,
			@AConQATAttribute(name = "groupId", description = "Id of the group this result belongs to. "
					+ "This is used to group pages in the navigation frame.", defaultValue = "Main") String groupId) {

		this.name = name;

		if (COPY_DESCRIPTION.equals(description)) {
			this.description = name;
		} else {
			this.description = description;
		}
		this.groupId = groupId;
	}

	/**
	 * Returns the summary for this page. The default implementation just
	 * returns <code>null</code>.
	 */
	protected Object getSummary() {
		return null;
	}

	/** Returns the description set by {@link #setInfo(String, String, String)} */
	protected String getDescription() {
		return description;
	}

	/** Returns <code>&lt;processor name&gt;.html</code>. */
	protected String getFilename() {
		return BundleContext.getInstance().getHtmlPresentationManager()
				.getAbbreviation(getProcessorInfo().getName())
				+ ".html";
	}

	/** Returns the group id set by {@link #setInfo(String, String, String)} */
	protected String getGroupId() {
		return groupId;
	}

	/**
	 * Template method for definining the icon file name. Must return filename
	 * without path. Rationale is that layouters of same type will create pages
	 * with the same icon.
	 */
	protected abstract String getIconName();

	/**
	 * Does the actual work in layouting the page. This gets called in the
	 * beginning of the {@link #process()} method. The implementing class should
	 * write into the {@link #writer}.
	 */
	protected abstract void layoutPage() throws ConQATException;

}