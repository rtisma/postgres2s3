package bio.overture.backup.postgres2s3.util;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.zip.GZIPOutputStream;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.io.Files.toByteArray;
import static java.lang.String.format;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.isDirectory;
import static java.nio.file.Files.isExecutable;
import static java.nio.file.Files.isRegularFile;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Files.walk;
import static java.util.stream.Collectors.toList;

public class FileIO {

  public static Optional<String> statusPathDoesNotExist(@NonNull Path path) {
    if (!exists(path)) {
      return Optional.of(format("The path '%s' does not exist", path));
    }
    return Optional.empty();
  }

  public static Optional<String> statusExecutableFileDoesNotExist(@NonNull Path filepath) {
    return statusFileDoesNotExist(filepath)
        .filter(x -> !isExecutable(filepath))
        .map(x -> format("The file '%s' is not executable", x));
  }

  public static Optional<String> statusFileDoesNotExist(@NonNull Path filepath) {
    return statusPathDoesNotExist(filepath)
        .filter(x -> !isRegularFile(filepath))
        .map(x -> format("The path '%s' is not a file", x));
  }

  public static Optional<String> statusDirectoryDoesNotExist(@NonNull Path dirpath) {
    return statusPathDoesNotExist(dirpath)
        .filter(x -> !isDirectory(dirpath))
        .map(x -> format("The path '%s' is not a directory", x));
  }

  public static void setupDirectory(@NonNull Path dirpath) throws IOException {
    if (!isDirectory(dirpath)) {
      Files.createDirectories(dirpath);
    }
  }

  public static void checkPathExists(@NonNull Path path) throws IOException {
    pathChecker(() -> statusPathDoesNotExist(path));
  }

  public static void checkFileExists(@NonNull Path path) throws IOException {
    pathChecker(() -> statusFileDoesNotExist(path));
  }

  public static void checkExecutableFileExists(@NonNull Path path) throws IOException {
    pathChecker(() -> statusExecutableFileDoesNotExist(path));
  }

  public static void checkDirectoryExists(@NonNull Path path) throws IOException {
    pathChecker(() -> statusDirectoryDoesNotExist(path));
  }

  public static String readFileContent(@NonNull Path filePath) throws IOException {
    return new String(toByteArray(filePath.toFile()));
  }

  public static Stream<Path> streamFilesInDir(@NonNull Path dirPath, boolean recursive)
      throws IOException {
    checkDirectoryExists(dirPath);
    return (recursive ? walk(dirPath) : walk(dirPath, 1)).filter(x -> !isDirectory(x));
  }

  public static List<Path> listFilesInDir(@NonNull Path dirPath, boolean recursive)
      throws IOException {
    return streamFilesInDir(dirPath, recursive).collect(toList());
  }

  @SneakyThrows
  public static String calculateFileMd5(@NonNull Path file) {
    checkFileExists(file);
    // Static getInstance method is called with hashing MD5
    val md = MessageDigest.getInstance("MD5");

    // digest() method is called to calculate message digest
    //  of an input digest() return array of byte
    byte[] messageDigest = md.digest(readAllBytes(file));

    // Convert byte array into signum representation
    val no = new BigInteger(1, messageDigest);

    // Convert message digest into hex value
    String hashtext = no.toString(16);
    while (hashtext.length() < 32) {
      hashtext = "0" + hashtext;
    }
    return hashtext;

  }

  public static Optional<String> getExtension(@NonNull Path file){
    val result = file.getFileName().toString()
        .replaceAll("^\\.\\|^\\/", "")
        .replaceAll("^[^\\.]+\\.", "");
    if (isNullOrEmpty(result)){
      return Optional.empty();
    } else {
      return Optional.of(result);
    }
  }

  public static void compressGzipFile(InputStream inputStream, Path outputGzipFile) throws IOException {
    val fos = new FileOutputStream(outputGzipFile.toFile());
    val gzipOS = new GZIPOutputStream(fos);
    byte[] buffer = new byte[1024];
    int len;
    while((len=inputStream.read(buffer)) != -1){
      gzipOS.write(buffer, 0, len);
    }
    //close resources
    gzipOS.close();
    fos.close();
    inputStream.close();
  }

  public static void compressGzipFile(Path inputFile, Path outputGzipFile) throws IOException {
   val fis = new FileInputStream(inputFile.toFile());
   compressGzipFile(fis, outputGzipFile);
 }

  public static void compressGzipFile(String inputData, Path outputGzipFile) throws IOException {
    val bais = new ByteArrayInputStream(inputData.getBytes());
    compressGzipFile(bais, outputGzipFile);
  }

  private static void pathChecker(Supplier<Optional<String>> statusSupplier) throws IOException {
    val result = statusSupplier.get();
    if (result.isPresent()) {
      throw new IOException(result.get());
    }
  }
}
