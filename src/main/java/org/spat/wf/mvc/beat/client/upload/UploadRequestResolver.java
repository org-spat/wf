package org.spat.wf.mvc.beat.client.upload;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.spat.wf.log.ILogger;
import org.spat.wf.log.LoggerFactory;

public class UploadRequestResolver {

	protected final ILogger logger = LoggerFactory.getLogger(getClass());

	private final DiskFileItemFactory fileItemFactory;

	private final FileUpload fileUpload;

	private MultipartParsingResult parsingResult;

	/**
	 * Standard Servlet spec context attribute that specifies a temporary
	 * directory for the current web application, of type
	 * <code>java.io.File</code>.
	 */
	public static final String TEMP_DIR_CONTEXT_ATTRIBUTE = "javax.servlet.context.tempdir";

	private static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";

	public UploadRequestResolver() {

		this.fileItemFactory = new DiskFileItemFactory();
		this.fileUpload = new ServletFileUpload(getFileItemFactory());
	}

	public UploadRequestResolver(ServletContext servletContext) {
		this();
		getFileItemFactory().setRepository(getTempDir(servletContext));
	}

	public MultipartParsingResult resolveMultipart(
			final HttpServletRequest request) throws Exception {

		MultipartParsingResult parsingResult = parseRequest(request);
		return parsingResult;
	}

	public MultipartParsingResult getParsingResult() {
		return this.parsingResult;
	}

	/**
	 * Parse the given servlet request, resolving its multipart elements.
	 * 
	 * @param request
	 *            the request to parse
	 * @return the parsing result
	 * @throws MultipartException
	 *             if multipart resolution failed.
	 */
	
	protected MultipartParsingResult parseRequest(HttpServletRequest request)
			throws Exception {
		String encoding = determineEncoding(request);
		FileUpload fileUpload = prepareFileUpload(encoding);
		try {
			List<FileItem> fileItems = ((ServletFileUpload) fileUpload)
					.parseRequest(request);
			return parseFileItems(fileItems, encoding);
		} catch (Exception ex) {
			throw new Exception("Could not parse multipart servlet request", ex);
		}
	}

	/**
	 * Parse the given List of Commons FileItems into a Spring
	 * MultipartParsingResult, containing Spring MultipartFile instances and a
	 * Map of multipart parameter.
	 * 
	 * @param fileItems
	 *            the Commons FileIterms to parse
	 * @param encoding
	 *            the encoding to use for form fields
	 * @return the Spring MultipartParsingResult
	 * @see CommonsMultipartFile#CommonsMultipartFile(org.apache.commons.fileupload.FileItem)
	 */
	protected MultipartParsingResult parseFileItems(List<FileItem> fileItems,
			String encoding) {
		Map<String, LinkedList<RequestFile>> multipartFiles = new LinkedHashMap<String, LinkedList<RequestFile>>();
		Map<String, String[]> multipartParameters = new HashMap<String, String[]>();

		// Extract multipart files and multipart parameters.
		for (FileItem fileItem : fileItems) {
			if (fileItem.isFormField()) {
				String value;
				//Without MediaType encoding
				String partEncoding = encoding;
				if (partEncoding != null) {
					try {
						value = fileItem.getString(partEncoding);
					} catch (UnsupportedEncodingException ex) {
						if (logger.isWarnEnabled()) {
							logger.warn("Could not decode multipart item '"
									+ fileItem.getFieldName()
									+ "' with encoding '" + partEncoding
									+ "': using platform default");
						}
						value = fileItem.getString();
					}
				} else {
					value = fileItem.getString();
				}
				String[] curParam = multipartParameters.get(fileItem
						.getFieldName());
				if (curParam == null) {
					// simple form field
					multipartParameters.put(fileItem.getFieldName(),
							new String[] { value });
				} else {
					// array of simple form fields
					String[] newParam = addStringToArray(curParam,
							value);
					multipartParameters.put(fileItem.getFieldName(), newParam);
				}
			} else {
				// multipart file field
				RequestFile file = new RequestFile(fileItem);
				if(multipartFiles.containsKey(file.getName())) {
					LinkedList<RequestFile> fileList = multipartFiles.get(file.getName());
					fileList.add(file);
				} else {
					LinkedList<RequestFile> fileList = new LinkedList<RequestFile>();
					fileList.add(file);
					multipartFiles.put(file.getName(), fileList);
				}
				
				if (logger.isDebugEnabled()) {
					logger.debug("Found multipart file [" + file.getName()
							+ "] of size " + file.getSize()
							+ " bytes with original filename ["
							+ file.getOriginalFilename() + "], stored "
							+ file.getStorageDescription());
				}
			}
		}
		return new MultipartParsingResult(multipartFiles, multipartParameters);
	}

	/**
	 * Determine an appropriate FileUpload instance for the given encoding.
	 * <p>
	 * Default implementation returns the shared FileUpload instance if the
	 * encoding matches, else creates a new FileUpload instance with the same
	 * configuration other than the desired encoding.
	 * 
	 * @param encoding
	 *            the character encoding to use
	 * @return an appropriate FileUpload instance.
	 */
	protected FileUpload prepareFileUpload(String encoding) {
		FileUpload fileUpload = getFileUpload();
		FileUpload actualFileUpload = fileUpload;

		// Use new temporary FileUpload instance if the request specifies
		// its own encoding that does not match the default encoding.
		if (encoding != null
				&& !encoding.equals(fileUpload.getHeaderEncoding())) {
			actualFileUpload = new ServletFileUpload(getFileItemFactory());
			actualFileUpload.setSizeMax(fileUpload.getSizeMax());
			actualFileUpload.setHeaderEncoding(encoding);
		}

		return actualFileUpload;
	}

	/**
	 * Determine the encoding for the given request. Can be overridden in
	 * subclasses.
	 * <p>
	 * The default implementation checks the request encoding, falling back to
	 * the default encoding specified for this resolver.
	 * 
	 * @param request
	 *            current HTTP request
	 * @return the encoding for the request (never <code>null</code>)
	 * @see javax.servlet.ServletRequest#getCharacterEncoding
	 * @see #setDefaultEncoding
	 */
	protected String determineEncoding(HttpServletRequest request) {
		String encoding = request.getCharacterEncoding();
		if (encoding == null) {
			encoding = getDefaultEncoding();
		}
		return encoding;
	}

	protected String getDefaultEncoding() {
		String encoding = getFileUpload().getHeaderEncoding();
		if (encoding == null) {
			encoding = DEFAULT_CHARACTER_ENCODING;
		}
		return encoding;
	}

	/**
	 * Return the underlying
	 * <code>org.apache.commons.fileupload.disk.DiskFileItemFactory</code>
	 * instance. There is hardly any need to access this.
	 * 
	 * @return the underlying DiskFileItemFactory instance
	 */
	public DiskFileItemFactory getFileItemFactory() {
		return this.fileItemFactory;
	}

	/**
	 * Return the temporary directory for the current web application, as
	 * provided by the servlet container.
	 * 
	 * @param servletContext
	 *            the servlet context of the web application
	 * @return the File representing the temporary directory
	 */
	private static File getTempDir(ServletContext servletContext) {
		
		return (File) servletContext.getAttribute(TEMP_DIR_CONTEXT_ATTRIBUTE);
	}

	public void setDefaultEncoding(String defaultEncoding) {
		this.fileUpload.setHeaderEncoding(defaultEncoding);
	}

	/**
	 * Return the underlying
	 * <code>org.apache.commons.fileupload.FileUpload</code> instance. There is
	 * hardly any need to access this.
	 * 
	 * @return the underlying FileUpload instance
	 */
	public FileUpload getFileUpload() {
		return this.fileUpload;
	}

	/**
	 * Holder for a Map of Spring MultipartFiles and a Map of multipart
	 * parameters.
	 */
	public static class MultipartParsingResult {

//		private final MultiValueMap<String, RequestFile> multipartFiles;
		private final Map<String, LinkedList<RequestFile>> multipartFiles;

		private final Map<String, String[]> multipartParameters;

		/**
		 * Create a new MultipartParsingResult.
		 * 
		 * @param mpFiles
		 *            Map of field name to MultipartFile instance
		 * @param mpParams
		 *            Map of field name to form field String value
		 */
		public MultipartParsingResult(
				Map<String, LinkedList<RequestFile>> mpFiles,
				Map<String, String[]> mpParams) {
			this.multipartFiles = new LinkedHashMap<String, LinkedList<RequestFile>>(Collections.unmodifiableMap(mpFiles));
			this.multipartParameters = mpParams;
		}

		/**
		 * Return the multipart files as Map of field name to MultipartFile
		 * instance.
		 */
		public Map<String, LinkedList<RequestFile>> getMultipartFiles() {
			return this.multipartFiles;
		}

		/**
		 * Return the multipart parameters as Map of field name to form field
		 * String value.
		 */
		public Map<String, String[]> getMultipartParameters() {
			return this.multipartParameters;
		}
	}
	public static String[] addStringToArray(String[] array, String str) {
		
	if (array == null || array.length == 0) {
		return new String[] {str};
	}
	String[] newArr = new String[array.length + 1];
	System.arraycopy(array, 0, newArr, 0, array.length);
	newArr[array.length] = str;
	return newArr;
}
}
