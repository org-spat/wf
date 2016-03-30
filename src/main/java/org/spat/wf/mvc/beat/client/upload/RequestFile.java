 /*
 *  Copyright Beijing 58 Information Technology Co.,Ltd.
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.spat.wf.mvc.beat.client.upload;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.spat.wf.log.ILogger;
import org.spat.wf.log.LoggerFactory;


/**
 * 
 * multipart request的封装实现，用于实现文件的上传功能。
 * <p>文件内容可以存储于内存或者临时目录。需要自己实现存储。临时目录会清除。
 * 
 *
 * @author Service Platform Architecture Team (spat@58.com)
 *
 */
public class RequestFile {
	
	protected static final ILogger logger = LoggerFactory.getLogger(RequestFile.class);
	
	private final FileItem fileItem;
	
	private final long size;
	
	RequestFile(FileItem fileItem) {
		this.fileItem = fileItem;
		this.size = this.fileItem.getSize();
	}

	/**
	 * Return the name of the parameter in the multipart form.
	 * @return the name of the parameter (never <code>null</code> or empty)
	 */
	public String getName(){
		return this.fileItem.getFieldName();
	}

	/**
	 * Return the original filename in the client's filesystem.
	 * <p>This may contain path information depending on the browser used,
	 * but it typically will not with any other than Opera.
	 * @return the original filename, or the empty String if no file
	 * has been chosen in the multipart form
	 */
	public String getOriginalFilename(){
		String filename = this.fileItem.getName();
		if (filename == null) {
			// Should never happen.
			return "";
		}
		// check for Unix-style path
		int pos = filename.lastIndexOf("/");
		if (pos == -1) {
			// check for Windows-style path
			pos = filename.lastIndexOf("\\");
		}
		if (pos != -1)  {
			// any sort of path separator found
			return filename.substring(pos + 1);
		}
		else {
			// plain name
			return filename;
		}
	}


	/**
	 * Return the content type of the file.
	 * @return the content type, or <code>null</code> if not defined
	 * (or no file has been chosen in the multipart form)
	 */
	public String getContentType(){
		return this.fileItem.getContentType();
	}

	/**
	 * Return whether the uploaded file is empty, that is, either no file has
	 * been chosen in the multipart form or the chosen file has no content.
	 */
	public boolean isEmpty(){
		return (this.size == 0);
	}

	/**
	 * Return the size of the file in bytes.
	 * @return the size of the file, or 0 if empty
	 */
	public long getSize(){
		return this.size;
	}

	/**
	 * Return the contents of the file as an array of bytes.
	 * @return the contents of the file as bytes, or an empty byte array if empty
	 * @throws IOException in case of access errors (if the temporary store fails)
	 */
	public byte[] getBytes() throws IOException{
		if (!isAvailable()) {
			throw new IllegalStateException("File has been moved - cannot be read again");
		}
		byte[] bytes = this.fileItem.get();
		return (bytes != null ? bytes : new byte[0]);
	}
	
	/**
	 * Return an InputStream to read the contents of the file from.
	 * The user is responsible for closing the stream.
	 * @return the contents of the file as stream, or an empty stream if empty
	 * @throws IOException in case of access errors (if the temporary store fails)
	 */
	public InputStream getInputStream() throws IOException{
		if (!isAvailable()) {
			throw new IllegalStateException("File has been moved - cannot be read again");
		}
		InputStream inputStream = this.fileItem.getInputStream();
		return (inputStream != null ? inputStream : new ByteArrayInputStream(new byte[0]));
	}

	/**
	 * Transfer the received file to the given destination file.
	 * <p>This may either move the file in the filesystem, copy the file in the
	 * filesystem, or save memory-held contents to the destination file.
	 * If the destination file already exists, it will be deleted first.
	 * <p>If the file has been moved in the filesystem, this operation cannot
	 * be invoked again. Therefore, call this method just once to be able to
	 * work with any storage mechanism.
	 * @param dest the destination file
	 * @throws IOException in case of reading or writing errors
	 * @throws IllegalStateException if the file has already been moved
	 * in the filesystem and is not available anymore for another transfer
	 */
	public void transferTo(File dest) throws IOException, IllegalStateException{
		if (!isAvailable()) {
			throw new IllegalStateException("File has already been moved - cannot be transferred again");
		}

		if (dest.exists() && !dest.delete()) {
			throw new IOException(
					"Destination file [" + dest.getAbsolutePath() + "] already exists and could not be deleted");
		}

		try {
			this.fileItem.write(dest);
			if (logger.isDebugEnabled()) {
				String action = "transferred";
				if (!this.fileItem.isInMemory()) {
					action = isAvailable() ? "copied" : "moved";
				}
				logger.debug("Multipart file '" + getName() + "' with original filename [" +
						getOriginalFilename() + "], stored " + getStorageDescription() + ": " +
						action + " to [" + dest.getAbsolutePath() + "]");
			}
		}
		catch (FileUploadException ex) {
			throw new IllegalStateException(ex.getMessage());
		}
		catch (IOException ex) {
			throw ex;
		}
		catch (Exception ex) {
			logger.error("Could not transfer to file", ex);
			throw new IOException("Could not transfer to file: " + ex.getMessage());
		}
	}
	
	/**
	 * Determine whether the multipart content is still available.
	 * If a temporary file has been moved, the content is no longer available.
	 */
	protected boolean isAvailable() {
		// If in memory, it's available.
		if (this.fileItem.isInMemory()) {
			return true;
		}
		// Check actual existence of temporary file.
		if (this.fileItem instanceof DiskFileItem) {
			return ((DiskFileItem) this.fileItem).getStoreLocation().exists();
		}
		// Check whether current file size is different than original one.
		return (this.fileItem.getSize() == this.size);
	}

	
	/**
	 * Return a description for the storage location of the multipart content.
	 * Tries to be as specific as possible: mentions the file location in case
	 * of a temporary file.
	 */
	public String getStorageDescription() {
		if (this.fileItem.isInMemory()) {
			return "in memory";
		}
		else if (this.fileItem instanceof DiskFileItem) {
			return "at [" + ((DiskFileItem) this.fileItem).getStoreLocation().getAbsolutePath() + "]";
		}
		else {
			return "on disk";
		}
	}

}
