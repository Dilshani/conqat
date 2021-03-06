
package cern.lhc.omc.conqat.python.pylint;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.conqat.engine.commons.execution.ProcessExecutorBase;
import org.conqat.engine.core.core.AConQATAttribute;
import org.conqat.engine.core.core.AConQATParameter;
import org.conqat.engine.core.core.AConQATProcessor;
import org.conqat.engine.core.core.APipelineSource;
import org.conqat.engine.core.core.ConQATException;

import cern.lhc.omc.conqat.python.BundleContext;

/**
 * {@ConQAT.Doc}
 * 
 * @author vimaier
 */
@AConQATProcessor(description = "Processor to execute PyLint on a module or package. "
		+ "PyLint has to be installted, see http://docs.pylint.org/installation.html .")
public class PyLintExecutor extends ProcessExecutorBase {

	/** Path to module or package. Input parameter for Processor. */
	private Path moduleOrPackage;
	private boolean enable;

	/** {@ConQAT.Doc} */
	@AConQATParameter(name = "output-file", description = "Name of the file containing the PyLints output", minOccurrences = 1, maxOccurrences = 1)
	public void setOutputFile(
			@AConQATAttribute(name = "value", description = "Ouput file path") @APipelineSource String outputFile) {
		this.outputFile = outputFile;
	}

	/** {@ConQAT.Doc} */
	@Override
	@AConQATParameter(name = "arg", description = "Additional arguments passed to PyLint. "
			+ "This can be used to fine-tune the execution of PyLint. The options"
			+ "are documented at http://docs.pylint.org/features.html.")
	public void addArgument(
			@AConQATAttribute(name = "value", description = "Argument value") String argument) {
		super.addArgument(argument);
	}
	
	/** {@ConQAT.Doc} */
	@AConQATParameter(name = "enable", description = "Decides wheter script will be executed(true) or not(false).")
	public void addEnable(
			@AConQATAttribute(name = "value", description = "Argument value", defaultValue="true") boolean enable) {
		this.enable = enable;
	}

	/** {@ConQAT.Doc} */
	@AConQATParameter(name = "input", description = "Path to module or package", minOccurrences = 1, maxOccurrences = 1)
	public void setInput(
			@AConQATAttribute(name = "path", description = "Path to module or package") String pathToModuleOrPackage) 
			throws ConQATException {
		moduleOrPackage = Paths.get(pathToModuleOrPackage);
		if(Files.notExists(moduleOrPackage)) {
			throw new ConQATException("Module or package doesn't exist: "+ moduleOrPackage);
		}			
	}


	/** Get arguments required to execute PyLint. */
	protected List<String> getArguments() {
		ArrayList<String> result = new ArrayList<String>();
		result.add(getPyLintScriptLocation());
		result.add(PyLintReportReader.MESSAGE_TEMPLATE);
		result.addAll(arguments);
		result.add(moduleOrPackage.toString());

		return result;
	}
	

	/** {@inheritDoc} */
	@Override
	protected List<String> getCommand() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("python");
		result.add(PyLintExecutor.getPyLintRunnerLocation());
		result.addAll(getArguments());

		return result;
	}
	
	/** {@inheritDoc} */
	@Override
	public String process() throws ConQATException {
		if(enable)
			super.process();
		return outputFile;
	}
	

	/** {@inheritDoc} */
	@Override
	protected void logStdOut(String stdOut) {
		try {
			FileUtils.writeStringToFile(new File(outputFile), stdOut);
		} catch (IOException e) {
			getLogger().error("Could not save PyLint output to file", e);
		}
		super.logStdOut(stdOut);
	}
	
	/** Returns the location of the pylint_runnter.py file. */
	/* package */static String getPyLintRunnerLocation() {
		BundleContext bundleContext = BundleContext.getInstance();
		String absPath = bundleContext.getResourceManager().getAbsoluteResourcePath("pylint_runner.py");

		return absPath;				
	}
	
	/** Returns the location of the pylint script file within resources. */
	/* package */static String getPyLintScriptLocation() {
		BundleContext bundleContext = BundleContext.getInstance();
		String osDependentPath = "pylint/pylint/bin/pylint";
		if(isWindowsOs())
			osDependentPath += ".bat";
		String absPath = bundleContext.getResourceManager()
				.getAbsoluteResourcePath(osDependentPath);
		
		return absPath;				
	}

	private static String OS = System.getProperty("os.name").toLowerCase();	
	private static boolean isWindowsOs() {
		return (OS.indexOf("win") >= 0);
	}
	

}