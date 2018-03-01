package cn.edu.hfuu.iao.utils;

import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Path;

/** the tar.xz processing */
public class Compressor {

  /**
   * Compress all the files in the output folder
   *
   * @param outputFolder
   *          the folder
   */
  public static final void compress(final Path outputFolder) {
    final Path folder = IOUtils.canonicalize(outputFolder);
    final Path destFolder = folder.getParent();
    final String archiveName = folder.getFileName().toString()//
        + ".tar.xz"; //$NON-NLS-1$
    final Path archivePath = destFolder.resolve(archiveName);

    try {
      final ProcessBuilder pb = new ProcessBuilder();

      ConsoleIO.stdout(//
          "Trying to build archive '"//$NON-NLS-1$
              + archivePath + "' from folder '" + //$NON-NLS-1$
              folder + "' into folder '" + //$NON-NLS-1$
              destFolder + '\'');

      pb.command(//
          "tar", //$NON-NLS-1$
          "-cJf", //$NON-NLS-1$
          archiveName, //
          destFolder.relativize(folder).toString());

      pb.environment().put("XZ_OPT", //$NON-NLS-1$
          "-9e"); //$NON-NLS-1$
      pb.directory(destFolder.toFile());
      pb.redirectErrorStream(true);
      pb.redirectOutput(Redirect.INHERIT);

      final int result = pb.start().waitFor();

      ConsoleIO.stdout(//
          "Finished building archive '"//$NON-NLS-1$
              + archivePath + "' from folder '" + //$NON-NLS-1$
              outputFolder + "' into folder '" + //$NON-NLS-1$
              destFolder + "', return code " + result);//$NON-NLS-1$
    } catch (final Throwable error) {
      ConsoleIO.stderr(//
          "Failed to build archive '"//$NON-NLS-1$
              + archivePath + "' from folder '" + //$NON-NLS-1$
              folder + "' into folder '" + //$NON-NLS-1$
              destFolder + //
              "', maybe you do not have the tar command installed.", //$NON-NLS-1$
          error);
    }
  }
}
