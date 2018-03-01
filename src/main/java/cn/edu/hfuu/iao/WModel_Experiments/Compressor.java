package cn.edu.hfuu.iao.WModel_Experiments;

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
    Path folder = outputFolder;
    try {
      folder = folder.toAbsolutePath();
      folder = folder.toRealPath();
    } catch (final Throwable error) {
      error.printStackTrace();
    }
    final Path destFolder = folder.getParent();
    final String archiveName = folder.getFileName().toString()//
        + ".tar.xz"; //$NON-NLS-1$
    final Path archivePath = destFolder.resolve(archiveName);

    try {
      final ProcessBuilder pb = new ProcessBuilder();

      System.out.println(//
          "Trying to build archive '"//$NON-NLS-1$
              + archivePath + "' from folder '" + //$NON-NLS-1$
              folder + "' into folder '" + //$NON-NLS-1$
              destFolder + '\'');
      System.out.flush();
      System.err.flush();

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

      System.out.println(//
          "Finished building archive '"//$NON-NLS-1$
              + archivePath + "' from folder '" + //$NON-NLS-1$
              outputFolder + "' into folder '" + //$NON-NLS-1$
              destFolder + "', return code " + result);//$NON-NLS-1$
    } catch (final Throwable error) {
      System.err.println(//
          "Failed to build archive '"//$NON-NLS-1$
              + archivePath + "' from folder '" + //$NON-NLS-1$
              folder + "' into folder '" + //$NON-NLS-1$
              destFolder + //
              "', maybe you do not have the tar command installed.");//$NON-NLS-1$
      error.printStackTrace();
    }
  }
}
