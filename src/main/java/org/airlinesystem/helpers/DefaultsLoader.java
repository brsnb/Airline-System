/**
 * DefaultsLoader class
 * 		Loads the default files into a user directory created where the program
 * 		is ran
 */

package org.airlinesystem.helpers;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DefaultsLoader {

	/**
	 * Creates a directory in the current directory that the program is run from. If one already
	 * exists it will delete it and all of its contents to make sure they are "clean".
	 *  
	 * @return Returns the File object representing the created directory
	 */
	public File createNewDefaultDirectoryInUserFilesystem() {
		File _outputDirectory = new File(System.getProperty("user.dir") + "/airlinesystem-defaults");
	
		/*
		 *  Delete the directory if it already exists
		 */
		if(_outputDirectory.exists()) {
			File[] _directoryContents = _outputDirectory.listFiles();
			if(_directoryContents != null) {
				for(File _f : _directoryContents) {
					_f.delete();
				}
			}
		_outputDirectory.delete();
		}
		_outputDirectory.mkdir();
		return _outputDirectory;
	}
	
	/**
	 * Copies the default properties file and the properties description file from the 
	 * resources into the newly created user directory
	 * 
	 * @param dirFile_ The File object representing the destination
	 * @throws IOException
	 */
	public void createDefaultPropertiesInUserFilesystem(File dirFile_) throws IOException {
		InputStream _in = this.getClass().getResourceAsStream("/default.properties");
		File _propFile = new File(dirFile_.getAbsolutePath() + "/default.properties");
		Path _outPath = _propFile.toPath();
		Files.copy(_in, _outPath);
		_in.close();		

		_in = this.getClass().getResourceAsStream("/properties.description");
		_propFile = new File(dirFile_.getAbsolutePath() + "/properties.description");
		_outPath = _propFile.toPath();
		Files.copy(_in, _outPath);
		_in.close();
	}
	
	/**
	 * Copies the default graph file from the resources into the newly created user directory
	 * 
	 * @param dirFile_ The File object representing the destination
	 * @throws IOException
	 */
	public void createDefaultGraphInUserFilesystem(File dirFile_) throws IOException {
		InputStream _in = this.getClass().getResourceAsStream("/default-graph");
		
		File _graphFile = new File(dirFile_.getAbsolutePath() + "/default-graph");
		
		Path _outPath = _graphFile.toPath();
		Files.copy(_in, _outPath);	
		_in.close();
	}

	/**
	 * Copies the default data file from the resources into the newly created user directory
	 * 
	 * @param dirFile_ The File object representing the destination
	 * @throws IOException
	 */
	public void createExampleDataFileInUserFilesystem(File dirFile_) throws IOException {
		InputStream _in = this.getClass().getResourceAsStream("/default-data");
		
		File _dataFile = new File(dirFile_.getAbsolutePath() + "/default-data");
		
		Path _outPath = _dataFile.toPath();
		Files.copy(_in, _outPath);
		_in.close();
	}
	
	/**
	 * Runs the other class methods to create the directory and copy necessary default
	 * files for the program to run.
	 * 
	 * @throws IOException
	 */
	public void createDefaultsInUserFilesystem() throws IOException {
		File _outputDirectory = createNewDefaultDirectoryInUserFilesystem();
		createDefaultPropertiesInUserFilesystem(_outputDirectory);
		createDefaultGraphInUserFilesystem(_outputDirectory);
		createExampleDataFileInUserFilesystem(_outputDirectory);
	}
}
