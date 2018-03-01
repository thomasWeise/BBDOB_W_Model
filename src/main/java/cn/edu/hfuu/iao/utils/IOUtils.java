package cn.edu.hfuu.iao.utils;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

/** This class is used to load the input data. */
public final class IOUtils {

  /**
   * Canonicalize the given path
   *
   * @param path
   *          the input path
   * @return the canonical path
   */
  public static final Path canonicalize(final Path path) {
    Path prevPath, currentPath;

    prevPath = path;
    currentPath = prevPath.toAbsolutePath();
    if ((currentPath == null) || (currentPath.equals(prevPath))) {
      currentPath = prevPath;
    } else {
      prevPath = currentPath;
    }

    currentPath = currentPath.normalize();
    if ((currentPath == null) || (currentPath.equals(prevPath))) {
      currentPath = prevPath;
    } else {
      prevPath = currentPath;
    }

    currentPath = currentPath.toAbsolutePath();
    if ((currentPath == null) || (currentPath.equals(prevPath))) {
      currentPath = prevPath;
    } else {
      prevPath = currentPath;
    }

    try {
      currentPath = currentPath.toRealPath();
      if ((currentPath == null) || (currentPath.equals(prevPath))) {
        return prevPath;
      }
      currentPath = prevPath;
    } catch (@SuppressWarnings("unused") final Throwable error) {
      currentPath = prevPath;
    }

    return currentPath;
  }

  /**
   * Make the directories
   *
   * @param path
   *          the path
   * @return the directory path
   * @throws IOException
   *           if i/o fails
   */
  public static final Path makeDirs(final Path path) throws IOException {
    return IOUtils.canonicalize(//
        Files.createDirectories(IOUtils.canonicalize(path)));
  }

  /**
   * <p>
   * Delete a given path recursively in a robust way.
   * </p>
   * <p>
   * We walk the file tree recursively starting at {@code path} and try to
   * delete any file and directory we encounter. If a deletion fails (with
   * an {@link java.io.IOException}), the method does not fail immediately
   * but instead enqueues the exception in an internal list and continues
   * deleting. At the end, all the caught exceptions together are thrown as
   * {@link java.lang.Throwable#addSuppressed(java.lang.Throwable)
   * suppressed} exceptions inside one {@link java.io.IOException}. Of
   * course, if a single file or folder deletion fails inside a folder, the
   * whole folder may not be deleted. However, as many files as possible
   * and as many sub-folders as possible will be removed. The exception
   * thrown in the end will provide a collection of information about what
   * went wrong. If nothing went wrong, no exception is thrown (obviously).
   * </p>
   * <p>
   * Notice that different from some solutions out there in the internet,
   * such as <a href="http://stackoverflow.com/questions/779519">http://
   * stackoverflow.com/questions/779519</a>, we do <em>not</em> try
   * platform-native commands such as {@code rm} or {@code rd}. There are
   * several reasons for this:
   * </p>
   * <ol>
   * <li>The most important one may be that the {@link java.nio.file.Path
   * Path API} is not &quot;platform-bound&quot;. It could be implemented
   * for <a href=
   * "https://docs.oracle.com/javase/7/docs/technotes/guides/io/fsp/zipfilesystemprovider.html"
   * >zip-file systems</a> as well or for memory file systems. If you would
   * have such a path and throw it into a platform-native command such as
   * {@code rd}, all hell could break loose (because that path may map to
   * anything, I think).</li>
   * <li>From an OS command, you may not get detailed error information.
   * </li>
   * <li>Calling an OS command or running a process for each deletion may
   * actually be slower (or faster, who knows, but I like deterministic
   * behavior).</li>
   * <li>Apropos, deterministic: using OS-dependent commands may introduce
   * slight differences in behavior on different OSes.</li>
   * <li>Also, you may only be able to implement OS-specific deletion for a
   * small subset of OSes, such as Linux/Unix and Windows. And you will
   * actually somewhat depend on the versions of these OSes. Maybe the
   * parameters of the commands change in an ever-so-subtle in the future
   * or already differ amongst the many versions of Windows, for example
   * &ndash; at least I wouldn't want to check or maintain that.</li>
   * <li>Finally, I think a Java program should stay within the boundaries
   * of the virtual machine for as long as possible. I would only call
   * outside processes if absolutely necessary. This is just a general
   * design idea: Reduce outside dependencies, rely on the security
   * settings, configuration, and implementation of the JVM.</li>
   * </ol>
   *
   * @param path
   *          the path to delete
   * @throws IOException
   *           if deletion fails
   */
  public static final void delete(final Path path) throws IOException {
    final __DeleteFileTree dft;

    if (path != null) { // if path is null or does not exist, silently exit
      if (Files.exists(path)) { // only now we need to do anything
        dft = new __DeleteFileTree();
        try {
          Files.walkFileTree(path, dft); // recursively delete
        } catch (final IOException ioe) {
          dft._storeException(ioe); // remember any additional exception
        }
        dft._throw(path); // throw an IOException if anything failed
      }
    }
  }

  /**
   * A {@link java.nio.file.FileVisitor} implementation which deletes a
   * file tree recursively, by first deleting the files and sub-directories
   * in a directory, then the directory itself.
   */
  private static final class __DeleteFileTree
      extends SimpleFileVisitor<Path> {

    /** the exceptions */
    private ArrayList<Throwable> m_exceptions;

    /** the file tree deleter's constructor */
    __DeleteFileTree() {
      super();
    }

    /**
     * store an exception (synchronized just in case)
     *
     * @param t
     *          the exception to store
     */
    synchronized final void _storeException(final Throwable t) {
      if (t != null) {
        if (this.m_exceptions == null) {
          this.m_exceptions = new ArrayList<>();
        }
        this.m_exceptions.add(t);
      }
    }

    /**
     * Throw an {@link java.io.IOException}, if necessary: If we
     * {@link #_storeException(java.lang.Throwable) collected} any
     * exceptions during the file and folder visiting process, we now
     * create and throw an {@link java.io.IOException} which states that
     * errors took place during the deletion of the (entry-point)
     * {@code path} and which has all the collected exceptions as
     * {@link java.lang.Throwable#addSuppressed(java.lang.Throwable)
     * suppressed exceptions}.
     *
     * @param path
     *          the root path
     * @throws IOException
     *           if necessary
     */
    final void _throw(final Path path) throws IOException {
      IOException ioe;
      if ((this.m_exceptions != null)
          && (!(this.m_exceptions.isEmpty()))) {
        ioe = new IOException("Error when deleting '" + path + '\''); //$NON-NLS-1$
        for (final Throwable t : this.m_exceptions) {
          ioe.addSuppressed(t);
        }
        throw ioe;
      }
    }

    /** {@inheritDoc} */
    @Override
    public final FileVisitResult visitFile(final Path file,
        final BasicFileAttributes attrs) {
      try {
        Files.delete(file);
      } catch (final Throwable t) {
        this._storeException(t);
      }
      return FileVisitResult.CONTINUE;
    }

    /** {@inheritDoc} */
    @Override
    public final FileVisitResult visitFileFailed(final Path file,
        final IOException exc) {
      if (exc != null) {
        this._storeException(exc);
      }
      try {
        Files.delete(file);
      } catch (final Throwable t) {
        this._storeException(t);
      }
      return FileVisitResult.CONTINUE;
    }

    /** {@inheritDoc} */
    @Override
    public final FileVisitResult postVisitDirectory(final Path dir,
        final IOException exc) {
      if (exc != null) {
        this._storeException(exc);
      }
      try {
        Files.delete(dir);
      } catch (final Throwable t) {
        this._storeException(t);
      }
      return FileVisitResult.CONTINUE;
    }
  }

}
