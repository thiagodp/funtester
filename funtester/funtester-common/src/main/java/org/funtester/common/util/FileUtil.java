package org.funtester.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * File utilities (touches the file system).
 *
 * @author Thiago Delgado Pinto
 *
 */
public final class FileUtil {

	private FileUtil() {}

	/**
	 * Verifies if a directory exists.
	 *
	 * @param directory	Directory to verify.
	 * @return			<code>true</code> if it exists.
	 */
	public static boolean directoryExists(String directory) {
		return ( new File( directory ) ).exists();
	}

	/**
	 * Returns the current directory.
	 *
	 * @return	Current directory as a <code>File</code>.
	 */
	public static File currentDirectory() {
		String dir = System.getProperty( "user.dir" );
		if ( null == dir ) {
			dir = "/";
		}
		return new File( dir );
	}

	/**
	 * Returns the current directory as string.
	 *
	 * @return	Current directory as a <code>String</code>.
	 */
	public static String currentDirectoryAsString() {
		String dir = currentDirectory().getAbsolutePath();
		// Remove a possible ending dot
		if ( dir.endsWith( "." ) ) {
			return dir.substring( 0, dir.lastIndexOf( "." ) - 1 );
		}
		return dir;
	}

	/**
	 * Return the directory of a file name.
	 *
	 * @param fileName	the file name.
	 * @return			the file directory as string.
	 */
	public static String directoryOfFile(final String fileName) {
		return ( new File( fileName ) ).getParent();
	}

	/**
	 * Return the absolute path of a file. If the file exists, just return it.
	 * Otherwise, try to convert the path to absolute path (from a relative one)
	 * and return it.
	 *
	 * @param filePath		the file to analyze.
	 * @param currentDir	the current directory.
	 * @return				an absolute path.
	 */
	public static String absolutePathOf(
			final String filePath,
			final String currentDir
			) {
		if ( ( new File( filePath ) ).exists() ) {
			return filePath;
		} else {
			return FilePathUtil.toAbsolutePath( filePath, currentDir );
		}
	}

	/**
	 * Copy a file from src to dst.
	 *
	 * @param src
	 * @param dst
	 * @throws IOException
	 */
	public static void copy(File src, File dst) throws IOException {
	    InputStream in = new FileInputStream( src );
	    try {
	        OutputStream out = new FileOutputStream( dst );
	        try {
	            byte[] buf = new byte[ 4096 ];
	            int len;
	            while ( ( len = in.read( buf ) ) > 0) {
	                out.write( buf, 0, len );
	            }
	        } finally {
	            out.close();
	        }
	    } finally {
	        in.close();
	    }
	}

	/**
	 * Return a path as an URI-like syntax.
	 *
	 * @param filePath
	 * @return
	 */
	public static String toURI( String filePath ) {
		return "file:///" + filePath.replaceAll( "\\\\", "/" );
	}
}
