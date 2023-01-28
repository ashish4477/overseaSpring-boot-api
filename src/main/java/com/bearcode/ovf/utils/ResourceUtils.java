package com.bearcode.ovf.utils;

import java.io.IOException;
import java.io.InputStream;

public class ResourceUtils {
	
	public static boolean exists(final String path) {
		InputStream is = null;
		try {
			is = openResource(path);
			return is != null;
		} finally {
			if (is != null) {
				try {
					is.close();
					is = null;
				} catch (IOException e) { // do nothing
				}
			}
		}
	}

	/**
	 * Opens the resource specified by the path.
	 * @author IanBrown
	 * @param path the path.
	 * @return the resource.
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	public static InputStream openResource(final String path) {
		return ResourceUtils.class.getClassLoader().getResourceAsStream(path);
	}
}
