package bio.overture.backup.postgres2s3.service;

import bio.overture.backup.postgres2s3.properties.BackupProperties;
import bio.overture.backup.postgres2s3.properties.DBProperties;
import bio.overture.backup.postgres2s3.util.CommandRunner;
import bio.overture.backup.postgres2s3.util.FileIO;
import lombok.NonNull;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static bio.overture.backup.postgres2s3.util.FileIO.calculateFileMd5;
import static bio.overture.backup.postgres2s3.util.FileIO.checkFileExists;
import static bio.overture.backup.postgres2s3.util.FileIO.compressGzipFile;
import static bio.overture.backup.postgres2s3.util.FileIO.getExtension;
import static java.lang.String.format;
import static java.nio.file.Files.move;
import static java.util.Objects.isNull;

@Service
public class DBDumpService {

 private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

 private final BackupProperties backupProperties;
 private final DBProperties dbProperties;
 private final CommandRunner pgDumpCommandRunner;

 @Autowired
 public DBDumpService(@NonNull CommandRunner pgDumpCommandRunner,
     @NonNull DBProperties dbProperties,
     @NonNull BackupProperties backupProperties ) {
  this.dbProperties = dbProperties;
  this.pgDumpCommandRunner = pgDumpCommandRunner;
  this.backupProperties = backupProperties;
 }

 public Path dumpDb() throws IOException, InterruptedException {
  val tmpFile = Paths.get("/tmp/"+System.currentTimeMillis()+".sql");
  val gzipTmpFile = Paths.get(tmpFile.toString()+".gz");
  val pgDumpEnvVars = Map.of("PGPASSWORD", dbProperties.getPassword());
  pgDumpCommandRunner.executeArgs(pgDumpEnvVars, "-U", dbProperties.getUser(), "-d" ,
      dbProperties.getName(), "-h", dbProperties.getHost(), "-p", dbProperties.getPort(), ">" , tmpFile.toString());
  checkFileExists(tmpFile);
  compressGzipFile(tmpFile, gzipTmpFile);
  return renameOutputFile(gzipTmpFile);
 }

 private Path renameOutputFile(Path gzipTmpFile) throws IOException {
  val md5 = calculateFileMd5(gzipTmpFile);
  val now = LocalDateTime.now();
  val dateString = DTF.format(now);
  val parentDir = isNull(gzipTmpFile.getParent()) ? Paths.get("./") : gzipTmpFile.getParent();
  val newPath = parentDir.resolve(format("%s_%s_%s.%s", backupProperties.getPrefix(), dateString, md5, getExtension(gzipTmpFile)));
  move(gzipTmpFile, newPath);
  return newPath;
 }

}
